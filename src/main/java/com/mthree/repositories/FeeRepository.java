package com.mthree.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthree.models.Fee;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Integer> {

}
