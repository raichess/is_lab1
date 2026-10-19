package com.raichess.repository;

import com.raichess.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    List<Location> findAllByOrderById();
}
