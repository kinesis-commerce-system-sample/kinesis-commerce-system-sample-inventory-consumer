package com.example.kinesiscommercesystemsample.inventory.consumer.service;

import com.eaio.uuid.UUID;
import com.example.kinesiscommercesystemsample.common.exception.BusinessRuleException;
import com.example.kinesiscommercesystemsample.common.messaging.order.entity.v1.OrderChangeStatusMessage;
import com.example.kinesiscommercesystemsample.common.messaging.purchase.entity.v1.PurchaseChangeStatusMessage;
import com.example.kinesiscommercesystemsample.inventory.consumer.infrastracture.dao.iteminventory.ItemInventoryDao;
import com.example.kinesiscommercesystemsample.inventory.consumer.infrastracture.dao.iteminventory.ItemInventoryEntity;
import com.example.kinesiscommercesystemsample.inventory.consumer.mom.order.OrderStreamWriter;
import com.example.kinesiscommercesystemsample.inventory.consumer.mom.purchase.PurchaseStreamWriter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class InventoryService {

	@Autowired
	private ItemInventoryDao itemInventoryDao;

	@Autowired
	private PurchaseStreamWriter purchaseStreamWriter;

	@Autowired
	private OrderStreamWriter orderStreamWriter;

	public void inbound(String itemId, Integer quantity, String purchaseId) {

		// TODO : 仕入れAPIで有効な purchaseId であるか確認する

		// 既に在庫に商品があれば数量増加してアップデート、無ければインサート
		val currentItemInventoryEntityOptional = itemInventoryDao.selectByItemId(itemId);
		if (currentItemInventoryEntityOptional.isPresent()) {

			val currentItemInventoryId = currentItemInventoryEntityOptional.get().getId();
			val currentQuantity = currentItemInventoryEntityOptional.get().getQuantity();
			val newQuantity = currentQuantity + quantity;

			val newItemInventoryEntity = new ItemInventoryEntity();
			newItemInventoryEntity.setId(currentItemInventoryId);
			newItemInventoryEntity.setQuantity(newQuantity);
			itemInventoryDao.update(newItemInventoryEntity);
		} else {

			val itemInventoryId = new UUID().toString();

			val itemInventoryEntity = new ItemInventoryEntity();
			itemInventoryEntity.setId(itemInventoryId);
			itemInventoryEntity.setItemId(itemId);
			itemInventoryEntity.setQuantity(quantity);
			itemInventoryDao.insert(itemInventoryEntity);
		}

		// 仕入れのStreamに入庫の通知
		val messageId = new UUID().toString();
		val purchaseChangeStatusMessage = new PurchaseChangeStatusMessage();
		purchaseChangeStatusMessage.setMessageId(messageId);
		purchaseChangeStatusMessage.setPurchaseId(purchaseId);
		purchaseChangeStatusMessage.setStatus("inbound");
		purchaseStreamWriter.write(purchaseChangeStatusMessage);

	}

	public void outbound(String itemId, Integer quantity, String orderId) {

		// TODO : 受注APIで有効な orderId であるか確認する

		val currentItemInventoryEntityOptional = itemInventoryDao.selectByItemId(itemId);
		if (!currentItemInventoryEntityOptional.isPresent()) {
			throw new BusinessRuleException("quantity shortabe.");
		}

		val currentItemInventoryId = currentItemInventoryEntityOptional.get().getId();
		val currentQuantity = currentItemInventoryEntityOptional.get().getQuantity();
		val newQuantity = currentQuantity - quantity;
		if (newQuantity < 0) {
			throw new BusinessRuleException("quantity shortabe.");
		}

		val newItemInventoryEntity = new ItemInventoryEntity();
		newItemInventoryEntity.setId(currentItemInventoryId);
		newItemInventoryEntity.setQuantity(newQuantity);
		itemInventoryDao.update(newItemInventoryEntity);

		// 受注のStreamに出庫の通知
		val messageId = new UUID().toString();
		val orderChangeStatusMessage = new OrderChangeStatusMessage();
		orderChangeStatusMessage.setMessageId(messageId);
		orderChangeStatusMessage.setOrderId(orderId);
		orderChangeStatusMessage.setStatus("outbound");
		orderStreamWriter.write(orderChangeStatusMessage);

	}
}
