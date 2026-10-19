package com.raichess.controller;

import com.raichess.model.Location;
import com.raichess.service.LocationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService service;

    public LocationController(LocationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Location> list() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Location> create(@Valid @RequestBody Location entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
