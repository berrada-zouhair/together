package com.together.controller;

import com.together.domain.Event;
import com.together.domain.Location;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.together.domain.Activity.FOOTBALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_create_event_and_get_it() throws Exception {
        Event event = new Event("EventName", "Description",
                new Location(0D, 0D), FOOTBALL);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event",
                event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String location = responseEntity.getHeaders().get(LOCATION).get(0);
        ResponseEntity<Event> eventEntity = restTemplate.getForEntity(location, Event.class);
        assertThat(eventEntity.getStatusCode()).isEqualTo(OK);
        Event storedEvent = eventEntity.getBody();
        assertThat(storedEvent.getName()).isEqualTo(event.getName());
        assertThat(storedEvent.getDescription()).isEqualTo(event.getDescription());
        assertThat(storedEvent.getLocation()).isEqualTo(event.getLocation());
        assertThat(storedEvent.getActivity()).isEqualTo(event.getActivity());
    }
}
