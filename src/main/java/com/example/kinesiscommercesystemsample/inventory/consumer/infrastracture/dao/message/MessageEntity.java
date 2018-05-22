package com.example.kinesiscommercesystemsample.inventory.consumer.infrastracture.dao.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

@Table(name = "message")
@Entity
@Getter
@Setter
@ToString
public class MessageEntity {

	@Id
	private String id;
}
