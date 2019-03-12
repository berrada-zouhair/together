package com.together.service;

import com.together.domain.Activity;
import com.together.domain.Event;
import com.together.domain.Location;
import com.together.repository.EventRepository;
import com.together.service.EventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventTestService {

    @Mock
    private EventRepository eventRepository;

    @Test
    public void addEvent() {
        Location location = new Location(0D, 0D);
        Event event = new Event("EventName", "Description", LocalDateTime.now(), location, Activity.FOOTBALL);
        EventService eventService = new EventService(eventRepository);
        eventService.add(event);
        verify(eventRepository, times(1)).save(event);
    }
}
