package com.together.controller;

import com.together.domain.Comment;
import com.together.domain.Event;
import com.together.domain.Location;
import com.together.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.together.domain.Activity.*;
import static com.together.domain.Gender.MAN;
import static com.together.utils.UrlUtils.extractIdFromUri;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void should_create_event_and_get_it() throws Exception {
        User owner = new User("firstName", "lastName", now(), MAN, "city", new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        assertThat(uri).isNotNull();
        String ownerId = extractIdFromUri(uri);
        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/events?owner=" + ownerId,
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
        ResponseEntity<User> responseEntity = restTemplate.getForEntity("/events/9999", User.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void should_fail_to_create_event_when_owner_is_not_existing() throws Exception {
        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);
        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/events?owner=9999",
                event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void should_add_participants_to_event() throws Exception {
        User owner = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/events?owner=" + ownerId, event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String eventLocation = responseEntity.getHeaders().get(LOCATION).get(0);

        User participant1 = new User("firstName1", "lastName1", now(), MAN, "city1",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        uri = restTemplate.postForLocation("/user", participant1);
        String participantId1 = extractIdFromUri(uri);

        User participant2 = new User("firstName2", "lastName2", now(), MAN, "city2",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        uri = restTemplate.postForLocation("/user", participant2);
        String participantId2 = extractIdFromUri(uri);

        restTemplate.postForLocation(eventLocation + "/participant?userId=" + participantId1, null);
        restTemplate.postForLocation(eventLocation + "/participant?userId=" + participantId2, null);

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
        User owner = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/events?owner=" + ownerId, event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String eventLocation = responseEntity.getHeaders().get(LOCATION).get(0);

        responseEntity = restTemplate.postForEntity(eventLocation + "/participant?userId=9999", null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

    }

    @Test
    public void should_have_status_400_when_adding_participant_to_a_not_existing_event() throws Exception {
        User user = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", user);
        String userId = extractIdFromUri(uri);

        ResponseEntity responseEntity = restTemplate.postForEntity("/events/9999/participant?userId=" + userId, null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

    }

    @Test
    public void should_add_liker_to_an_event() throws Exception {
        User owner = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);
        String eventLocation = restTemplate.postForLocation("/events?owner=" + ownerId, event, Void.class).toString();

        User liker = new User("firstName2", "lastName2", now(), MAN, "city2",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        uri = restTemplate.postForLocation("/user", liker);
        String participantId = extractIdFromUri(uri);

        restTemplate.postForLocation(eventLocation + "/participant?userId=" + participantId, null);

        restTemplate.postForLocation(eventLocation + "/liker?userId=" + participantId, null);
        restTemplate.postForLocation(eventLocation + "/liker?userId=" + ownerId, null);

        event.addLiker(owner);
        event.addLiker(liker);

        Event storedEvent = restTemplate.getForObject(eventLocation, Event.class);
        Set<User> likers = storedEvent.getLikers();
        assertThat(likers.size()).isEqualTo(2);
        assertThat(likers).containsExactlyInAnyOrder(owner, liker);
    }

    @Test
    public void should_have_status_400_when_adding_liker_to_a_not_existing_event() throws Exception {
        User user = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", user);
        String userId = extractIdFromUri(uri);

        ResponseEntity responseEntity = restTemplate.postForEntity("/events/9999/liker?userId=" + userId, null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

    }

    @Test
    public void should_have_status_400_when_adding_not_existing_liker_to_an_event() throws Exception {
        User owner = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/events?owner=" + ownerId, event, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(CREATED);
        String eventLocation = responseEntity.getHeaders().get(LOCATION).get(0);

        responseEntity = restTemplate.postForEntity(eventLocation + "/liker?userId=9999", null, Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void should_add_comments_to_an_event() throws Exception {
        User owner = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        uri = restTemplate.postForLocation("/events?owner=" + ownerId, event, Void.class);
        String eventId = extractIdFromUri(uri);

        Comment comment1 = new Comment(LocalDateTime.now(), "text1");
        comment1.setOwner(owner);
        Comment comment2 = new Comment(LocalDateTime.now(), "text2");
        comment2.setOwner(owner);

        restTemplate.postForLocation(uri.toString() + "/comment?owner=" + ownerId, comment1);
        restTemplate.postForLocation(uri.toString() + "/comment?owner=" + ownerId, comment2);

        event.addComment(comment1);
        event.addComment(comment2);

        Event storedEvent = restTemplate.getForObject("/events/" + eventId, Event.class);
        Set<Comment> comments = storedEvent.getComments();

        assertThat(comments.size()).isEqualTo(2);
        assertThat(comments
                .stream()
                .map(Comment::getText)
                .collect(toList())).containsExactlyInAnyOrder("text1", "text2");
        assertThat(event.getComments()).isEqualTo(storedEvent.getComments());
    }

    @Test
    public void should_have_status_400_when_comment_owner_or_event_are_not_existing() throws Exception {
        User owner = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        uri = restTemplate.postForLocation("/events?owner=" + ownerId, event, Void.class);
        String eventId = extractIdFromUri(uri);

        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity("/events/99999/comment?owner=" + ownerId, new Comment(), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

        responseEntity =
                restTemplate.postForEntity(uri.toString() + "/comment?owner=9999&event=" + eventId, new Comment(), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void should_get_todays_events_at_execution_hour_at_0_minutes() {
        User owner = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, FOOTBALL, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", owner);
        assertThat(uri).isNotNull();
        String ownerId = extractIdFromUri(uri);

        List<Event> todaysEvents = new ArrayList<>(2);
        Event event1 = new Event("EventName1", "Description1",
                LocalDateTime.now().plusMinutes(1), new Location(0D, 0D), FOOTBALL);
        Event event2 = new Event("EventName2", "Description2",
                LocalDateTime.now().withMinute(0).withSecond(0), new Location(0D, 0D), GUITAR);
        todaysEvents.add(event1);
        todaysEvents.add(event2);

        List<Event> oldEvents = new ArrayList<>(2);
        Event event3 = new Event("EventName3", "Description3",
                LocalDateTime.now().minusDays(1), new Location(0D, 0D), JAVA);
        Event event4 = new Event("EventName4", "Description4",
                LocalDateTime.now().minusHours(1), new Location(0D, 0D), GUITAR);
        oldEvents.add(event3);
        oldEvents.add(event4);

        List<Event> allEvents = new ArrayList<>(4);
        allEvents.addAll(oldEvents);
        allEvents.addAll(todaysEvents);

        for (Event event : allEvents) {
            restTemplate.postForLocation("/events?owner=" + ownerId,
                    event, Void.class);
        }
        ResponseEntity<List<Event>> responseEntity = restTemplate.exchange(
                "/events?userId=" + ownerId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>() {
                });
        List<Event> body = responseEntity.getBody();
        todaysEvents.forEach(e -> e.setOwner(owner));
        assertThat(body).containsExactlyInAnyOrder(todaysEvents.toArray(new Event[oldEvents.size()]));
    }

    @Test
    public void should_fail_to_get_todays_events_when_user_does_not_exist() {
        ResponseEntity<List<Event>> responseEntity = restTemplate.exchange(
                "/events?userId=99999",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Event>>(){});
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}
