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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

import static com.together.domain.Activity.FOOTBALL;
import static com.together.utils.UrlUtils.extractIdFromUri;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class CommentControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_add_comments_to_an_event() throws Exception {
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        uri = restTemplate.postForLocation("/event?owner=" + ownerId, event, Void.class);
        String eventId = extractIdFromUri(uri);

        Comment comment1 = new Comment(LocalDateTime.now(), "text1");
        comment1.setOwner(owner);
        Comment comment2 = new Comment(LocalDateTime.now(), "text2");
        comment2.setOwner(owner);

        restTemplate.postForLocation("/comment?owner=" + ownerId + "&event=" + eventId, comment1);
        restTemplate.postForLocation("/comment?owner=" + ownerId + "&event=" + eventId, comment2);

        event.addComment(comment1);
        event.addComment(comment2);

        Event storedEvent = restTemplate.getForObject("/event/" + eventId, Event.class);
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
        User owner = new User("firstName", "lastName", 39, "city", new byte[]{});
        URI uri = restTemplate.postForLocation("/user", owner);
        String ownerId = extractIdFromUri(uri);

        Event event = new Event("EventName", "Description",
                LocalDateTime.now(), new Location(0D, 0D), FOOTBALL);

        uri = restTemplate.postForLocation("/event?owner=" + ownerId, event, Void.class);
        String eventId = extractIdFromUri(uri);

        ResponseEntity<Void> responseEntity =
                restTemplate.postForEntity("/comment?owner="+ownerId+"&event=9999", new Comment(), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);

        responseEntity =
                restTemplate.postForEntity("/comment?owner=9999&event="+eventId, new Comment(), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}
