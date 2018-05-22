package com.example.kinesiscommercesystemsample.inventory.consumer.mom.purchase;

import com.example.kinesiscommercesystemsample.common.messaging.purchase.entity.PurchaseMessage;
import com.example.kinesiscommercesystemsample.common.messaging.purchase.mom.writer.AbstractPurchaseStreamWriter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PurchaseStreamWriter extends AbstractPurchaseStreamWriter {

	@Value("${application.kinesis.purchase.stream-name}")
	private String streamName;

	@Override
	public void write(PurchaseMessage message) {
		super.write(message);
	}
}
