package com.together.service;

import com.together.domain.Activity;
import com.together.domain.Event;
import com.together.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Long add(Event event) {
        return eventRepository.save(event).getId();
    }

    public Event get(Long eventId) {
        return eventRepository.findById(eventId);
    }

    public List<Event> get(LocalDateTime dateTime, Activity... activities) {
        return eventRepository.findByDateGreaterThanEqualAndActivityIn(dateTime, activities);
    }
}
