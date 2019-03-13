package com.together.repository;

import com.together.domain.Event;
import org.springframework.data.repository.Repository;

public interface EventRepository extends Repository<Event, Long> {

    Event save(Event event);

    Event findById(Long eventId);
}
