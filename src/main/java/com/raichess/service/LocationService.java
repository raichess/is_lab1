package com.raichess.service;

import com.raichess.error.ApiException;
import com.raichess.model.Location;
import com.raichess.repository.LocationRepository;
import com.raichess.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationService {

    private final LocationRepository locations;
    private final PersonRepository persons;

    public LocationService(LocationRepository locations, PersonRepository persons) {
        this.locations = locations;
        this.persons = persons;
    }

    public List<Location> findAll() {
        return locations.findAllByOrderById();
    }

    public Location create(Location entity) {
        entity.setId(null);
        return locations.save(entity);
    }

    public void delete(int id) {
        Location entity = locations.findById(id)
                .orElseThrow(() -> new ApiException(404, "Объект с id=" + id + " не найден"));
        long used = persons.countByLocationId(id);
        if (used > 0) {
            throw new ApiException(409, "Нельзя удалить: объект связан с Person (" + used + " шт.)");
        }
        locations.delete(entity);
    }
}
