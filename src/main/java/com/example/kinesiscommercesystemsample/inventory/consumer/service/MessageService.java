package com.example.kinesiscommercesystemsample.inventory.consumer.service;

import com.example.kinesiscommercesystemsample.common.exception.ProceededMessageException;
import com.example.kinesiscommercesystemsample.inventory.consumer.infrastracture.dao.message.MessageDao;
import com.example.kinesiscommercesystemsample.inventory.consumer.infrastracture.dao.message.MessageEntity;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Throwable.class)
public class MessageService {


	@Autowired
	private MessageDao messageDao;


	public void checkMessageConsistency(String messageId) {

		val messageEntityOptional = messageDao.selectById(messageId);
		if (messageEntityOptional.isPresent()) {
			throw new ProceededMessageException(String.format("message_id %s has already been proceeded.", messageId));
		}
	}

	public void recordMessage(String messageId) {

		val messageEntity = new MessageEntity();
		messageEntity.setId(messageId);
		messageDao.insert(messageEntity);
	}
}
