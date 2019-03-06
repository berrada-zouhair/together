package com.together.controller;

import com.together.domain.Event;
import com.together.domain.Location;
import com.together.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

import static com.together.domain.Activity.FOOTBALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class EventControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    private String extractUserIdFromUri(URI uri) {
        String uriString = uri.toString();
        String[] split = uriString.split("/");
        return split[split.length - 1];
    }

    @Test
    public void should_create_event_and_get_it() throws Exception {
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", owner);
        assertThat(uri).isNotNull();
        String ownerId = extractUserIdFromUri(uri);
        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event?owner=" + ownerId,
                event, Void.class);
        event.setOwner(owner);
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
    public void should_fail_to_create_event_when_owner_is_not_existing() throws Exception {
        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event?owner=9999",
                event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void should_add_participants_to_event() throws Exception {
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractUserIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event?owner=" + ownerId, event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String eventLocation = responseEntity.getHeaders().get(LOCATION).get(0);

        User participant1 = new User("firstName1", "lastName1", 25, "city1", new byte[]{});
        uri = restTemplate.postForLocation("/user", participant1);
        String participantId1 = extractUserIdFromUri(uri);

        User participant2 = new User("firstName2", "lastName2", 18, "city2", new byte[]{});
        uri = restTemplate.postForLocation("/user", participant2);
        String participantId2 = extractUserIdFromUri(uri);

        restTemplate.postForLocation(eventLocation + "/participant/" + participantId1, null);
        restTemplate.postForLocation(eventLocation + "/participant/" + participantId2, null);

        event.addParticipant(participant1);
        event.addParticipant(participant2);
        event.setOwner(owner);

        ResponseEntity<Event> eventEntity = restTemplate.getForEntity(eventLocation, Event.class);
        assertThat(eventEntity.getStatusCode()).isEqualTo(OK);
        Event storedEvent = eventEntity.getBody();
        assertThat(storedEvent.getOwner()).isEqualTo(owner);
        assertThat(event.getParticipants().size()).isEqualTo(2);
        assertThat(storedEvent.getParticipants().size()).isEqualTo(2);
        assertThat(storedEvent).isEqualTo(event);
    }

    @Test
    public void should_have_status_400_when_adding_not_existing_participant_to_an_event() throws Exception {
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractUserIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event?owner=" + ownerId, event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String eventLocation = responseEntity.getHeaders().get(LOCATION).get(0);

        responseEntity = restTemplate.postForEntity(eventLocation + "/participant/9999", null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

    }

    @Test
    public void should_have_status_400_when_adding_participant_to_a_not_existing_event() throws Exception {
        User user = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", user);
        String userId = extractUserIdFromUri(uri);

        ResponseEntity responseEntity = restTemplate.postForEntity( "/event/9999/participant/" + userId, null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

    }

    @Test
    public void should_add_liker_to_an_event() throws Exception {
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractUserIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);
        String eventLocation = restTemplate.postForLocation("/event?owner=" + ownerId, event, Void.class).toString();

        User liker = new User("firstName2", "lastName2", 18, "city2", new byte[]{});
        uri = restTemplate.postForLocation("/user", liker);
        String participantId = extractUserIdFromUri(uri);

        restTemplate.postForLocation(eventLocation + "/participant/" + participantId, null);

        restTemplate.postForLocation(eventLocation + "/liker/" + participantId, null);
        restTemplate.postForLocation(eventLocation + "/liker/" + ownerId, null);

        event.addLiker(owner);
        event.addLiker(liker);

        Event storedEvent = restTemplate.getForObject(eventLocation, Event.class);
        Set<User> likers = storedEvent.getLikers();
        assertThat(likers.size()).isEqualTo(2);
        assertThat(likers).containsExactlyInAnyOrder(owner, liker);
    }

    @Test
    public void should_have_status_400_when_adding_liker_to_a_not_existing_event() throws Exception {
        User user = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", user);
        String userId = extractUserIdFromUri(uri);

        ResponseEntity responseEntity = restTemplate.postForEntity( "/event/9999/liker/" + userId, null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

    }

    @Test
    public void should_have_status_400_when_adding_not_existing_liker_to_an_event() throws Exception {
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractUserIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/event?owner=" + ownerId, event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String eventLocation = responseEntity.getHeaders().get(LOCATION).get(0);

        responseEntity = restTemplate.postForEntity(eventLocation + "/liker/9999", null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

    }
}
