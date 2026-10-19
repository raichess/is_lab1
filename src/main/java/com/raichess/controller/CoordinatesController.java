package com.raichess.controller;

import com.raichess.model.Coordinates;
import com.raichess.service.CoordinatesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
public class CoordinatesController {

    private final CoordinatesService service;

    public CoordinatesController(CoordinatesService service) {
        this.service = service;
    }

    @GetMapping
    public List<Coordinates> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Coordinates> create(@Valid @RequestBody Coordinates entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
