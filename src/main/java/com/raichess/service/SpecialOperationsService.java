package com.raichess.service;

import com.raichess.error.ApiException;
import com.raichess.event.PersonChanged;
import com.raichess.model.Color;
import com.raichess.model.Person;
import com.raichess.repository.PersonRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecialOperationsService {

    private final PersonRepository persons;
    private final ApplicationEventPublisher events;

    public SpecialOperationsService(PersonRepository persons, ApplicationEventPublisher events) {
        this.persons = persons;
        this.events = events;
    }

    public int deleteOneByHeight(float height) {
        int id = persons.deleteOneByHeight(height);
        if (id == 0) {
            throw new ApiException(404, "Нет объектов с height = " + height);
        }
        events.publishEvent(new PersonChanged("deleted", id));
        return id;
    }

    public double sumHeights() {
        return persons.sumHeights();
    }

    public List<Person> heightLowerThan(float height) {
        return persons.findHeightLowerThan(height);
    }

    public double hairColorPercent(Color color) {
        return persons.hairColorPercent(color.name());
    }

    public long eyeColorCount(Color color) {
        return persons.eyeColorCount(color.name());
    }
}
