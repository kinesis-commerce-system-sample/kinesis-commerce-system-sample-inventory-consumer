package com.example.kinesiscommercesystemsample.inventory.consumer.infrastracture.dao.iteminventory;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@ConfigAutowireable
@Dao
public interface ItemInventoryDao {

	@Select
	Optional<ItemInventoryEntity> selectByItemId(String itemId);

	@Insert
	int insert(ItemInventoryEntity itemInventoryEntity);

	@Update(excludeNull = true)
	int update(ItemInventoryEntity itemInventoryEntity);
}
