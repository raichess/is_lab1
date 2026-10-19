package com.raichess.service;

import com.raichess.error.ApiException;
import com.raichess.model.Coordinates;
import com.raichess.repository.CoordinatesRepository;
import com.raichess.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CoordinatesService {

    private final CoordinatesRepository coordinates;
    private final PersonRepository persons;

    public CoordinatesService(CoordinatesRepository coordinates, PersonRepository persons) {
        this.coordinates = coordinates;
        this.persons = persons;
    }

    public List<Coordinates> findAll() {
        return coordinates.findAllByOrderById();
    }

    public Coordinates create(Coordinates entity) {
        entity.setId(null);
        return coordinates.save(entity);
    }

    public void delete(int id) {
        Coordinates entity = coordinates.findById(id)
                .orElseThrow(() -> new ApiException(404, "Объект с id=" + id + " не найден"));
        long used = persons.countByCoordinatesId(id);
        if (used > 0) {
            throw new ApiException(409, "Нельзя удалить: объект связан с Person (" + used + " шт.)");
        }
        coordinates.delete(entity);
    }
}
