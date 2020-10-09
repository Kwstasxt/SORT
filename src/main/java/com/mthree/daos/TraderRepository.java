package com.mthree.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthree.models.Trader;

@Repository
public interface TraderRepository extends JpaRepository<Trader, Integer> {

}
