package com.mthree.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthree.models.Exchange;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {

}
