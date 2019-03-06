package com.together.controller;

import com.together.domain.Event;
import com.together.domain.Location;
import com.together.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDateTime;

import static com.together.domain.Activity.FOOTBALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class EventControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_create_event_and_get_it() throws Exception {
        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL, null);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event",
                event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String location = responseEntity.getHeaders().get(LOCATION).get(0);
        ResponseEntity<Event> eventEntity = restTemplate.getForEntity(location, Event.class);
        assertThat(eventEntity.getStatusCode()).isEqualTo(OK);
        Event storedEvent = eventEntity.getBody();
        assertThat(storedEvent).isEqualTo(event);
    }

    @Test
    public void should_have_status_404_when_requesting_non_existing_event() throws Exception {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity("/event/9999", User.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void should_add_participants_to_event() throws Exception {
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL, null);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event",
                event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String eventLocation = responseEntity.getHeaders().get(LOCATION).get(0);

        User user1 = new User("firstName1", "lastName1", 25, "city", new byte[]{});
        String userUrl1 = restTemplate.postForLocation("/user",
                user1, User.class).toString();
        String[] split = userUrl1.split("/");
        userUrl1 = split[split.length-1];

        User user2 = new User("firstName2", "lastName2", 18, "city", new byte[]{});
        String userUrl2 = restTemplate.postForLocation("/user",
                user2, User.class).toString();
        split = userUrl2.split("/");
        userUrl2 = split[split.length-1];

        System.out.println();

        restTemplate.postForLocation(eventLocation + "/participant", userUrl1);
        restTemplate.postForLocation(eventLocation + "/participant", userUrl2);

        event.addParticipant(user1);
        event.addParticipant(user2);

        ResponseEntity<Event> eventEntity = restTemplate.getForEntity(eventLocation, Event.class);
        assertThat(eventEntity.getStatusCode()).isEqualTo(OK);
        Event storedEvent = eventEntity.getBody();
        assertThat(event.getParticipants().size()).isEqualTo(2);
        assertThat(storedEvent.getParticipants().size()).isEqualTo(2);
        assertThat(storedEvent).isEqualTo(event);
    }
}
