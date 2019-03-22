package com.together.repository;

import com.together.domain.Activity;
import com.together.domain.Event;
import org.springframework.data.repository.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends Repository<Event, Long> {

    Event save(Event event);

    Event findById(Long eventId);

    List<Event> findByDateGreaterThanEqualAndActivityIn(LocalDateTime dateTime, Activity... activities);
}
