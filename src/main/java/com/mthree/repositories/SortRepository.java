package com.mthree.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthree.models.Sort;


@Repository
public interface SortRepository extends JpaRepository<Sort, Integer> {

}