package com.raichess.repository;

import com.raichess.model.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer> {

    List<Coordinates> findAllByOrderById();
}
