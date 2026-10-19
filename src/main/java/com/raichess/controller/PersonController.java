package com.raichess.controller;

import com.raichess.dto.PersonFilter;
import com.raichess.dto.PersonRequest;
import com.raichess.model.Person;
import com.raichess.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Person> list(PersonFilter filter, @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return service.find(filter, pageable);
    }

    @GetMapping("/{id}")
    public Person get(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody PersonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public Person update(@PathVariable int id, @Valid @RequestBody PersonRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
