package com.raichess.repository;

import com.raichess.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {

    long countByCoordinatesId(Integer coordinatesId);

    long countByLocationId(Integer locationId);

    @Query(value = "SELECT delete_one_person_by_height(CAST(?1 AS REAL))", nativeQuery = true)
    Integer deleteOneByHeight(float height);

    @Query(value = "SELECT sum_person_height()", nativeQuery = true)
    Double sumHeights();

    @Query(value = "SELECT * FROM person_lower_than(CAST(?1 AS REAL))", nativeQuery = true)
    List<Person> findHeightLowerThan(float height);

    @Query(value = "SELECT hair_color_percent(CAST(?1 AS VARCHAR))", nativeQuery = true)
    Double hairColorPercent(String color);

    @Query(value = "SELECT eye_color_count(CAST(?1 AS VARCHAR))", nativeQuery = true)
    Long eyeColorCount(String color);
}
