package com.example.kinesiscommercesystemsample.inventory.consumer.mom.inventory;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.example.kinesiscommercesystemsample.common.messaging.inventory.mom.processor.AbstractInventoryRecordProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryRecordProcessorFactory implements IRecordProcessorFactory {

	@Autowired
	AbstractInventoryRecordProcessor inventoryRecordProcessor;

	@Override
	public IRecordProcessor createProcessor() {
		return inventoryRecordProcessor;
	}
}
