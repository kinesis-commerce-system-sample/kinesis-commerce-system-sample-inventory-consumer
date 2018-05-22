package com.example.kinesiscommercesystemsample.inventory.consumer.infrastracture.dao.iteminventory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Table(name = "item_inventory")
@Entity
@Getter
@Setter
@ToString
public class ItemInventoryEntity {

	@Id
	private String id;

	private String itemId;

	private Integer quantity;
}
