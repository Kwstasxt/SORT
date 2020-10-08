package com.mthree.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mthree.models.Trader;

public interface TraderRepository extends JpaRepository<Trader, Integer> {

}
