package com.example.kinesiscommercesystemsample.inventory.consumer.mom.order;

import com.example.kinesiscommercesystemsample.common.messaging.order.entity.OrderMessage;
import com.example.kinesiscommercesystemsample.common.messaging.order.mom.writer.AbstractOrderStreamWriter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class OrderStreamWriter extends AbstractOrderStreamWriter {

	@Value("${application.kinesis.order.stream-name}")
	private String streamName;

	@Override
	public void write(OrderMessage message) {
		super.write(message);
	}
}
