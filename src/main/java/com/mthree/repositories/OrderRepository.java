package com.mthree.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mthree.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("UPDATE Order o SET o.quantity=:value WHERE o.id=:oID")
	@Transactional
	@Modifying
	int updateOrderQuantityByID(@Param("oID") int id , @Param("value") int quant);

}
