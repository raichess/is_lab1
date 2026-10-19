package com.raichess.service;

import com.raichess.dto.PersonFilter;
import com.raichess.dto.PersonRequest;
import com.raichess.error.ApiException;
import com.raichess.event.PersonChanged;
import com.raichess.model.Coordinates;
import com.raichess.model.Location;
import com.raichess.model.Person;
import com.raichess.repository.CoordinatesRepository;
import com.raichess.repository.LocationRepository;
import com.raichess.repository.PersonRepository;
import com.raichess.repository.PersonSpecifications;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class PersonService {

    private final PersonRepository persons;
    private final CoordinatesRepository coordinates;
    private final LocationRepository locations;
    private final ApplicationEventPublisher events;

    public PersonService(PersonRepository persons, CoordinatesRepository coordinates,
                         LocationRepository locations, ApplicationEventPublisher events) {
        this.persons = persons;
        this.coordinates = coordinates;
        this.locations = locations;
        this.events = events;
    }

    public Page<Person> find(PersonFilter filter, Pageable pageable) {
        return persons.findAll(PersonSpecifications.byFilter(filter), pageable);
    }

    public Person get(int id) {
        return persons.findById(id)
                .orElseThrow(() -> new ApiException(404, "Person с id=" + id + " не найден"));
    }

    public Person create(PersonRequest r) {
        Person p = new Person();
        apply(p, r);
        persons.save(p);
        events.publishEvent(new PersonChanged("created", p.getId()));
        return p;
    }

    public Person update(int id, PersonRequest r) {
        Person p = get(id);
        apply(p, r);
        events.publishEvent(new PersonChanged("updated", id));
        return p;
    }

    public void delete(int id) {
        persons.delete(get(id));
        events.publishEvent(new PersonChanged("deleted", id));
    }

    private void apply(Person p, PersonRequest r) {
        Coordinates c = coordinates.findById(r.getCoordinatesId())
                .orElseThrow(() -> new ApiException(400, "Координаты с id=" + r.getCoordinatesId() + " не существуют"));
        Location l = locations.findById(r.getLocationId())
                .orElseThrow(() -> new ApiException(400, "Локация с id=" + r.getLocationId() + " не существует"));
        p.setName(r.getName().trim());
        p.setCoordinates(c);
        p.setLocation(l);
        p.setEyeColor(r.getEyeColor());
        p.setHairColor(r.getHairColor());
        p.setHeight(r.getHeight());
        p.setNationality(r.getNationality());
    }
}
