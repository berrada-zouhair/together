package com.together.controller;

import com.together.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.HashSet;

import static com.together.domain.Activity.GUITAR;
import static com.together.domain.Activity.JAVA;
import static com.together.domain.Gender.MAN;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_create_user_and_get_it() {
        User user = new User("firstName", "lastName", now(), MAN, "city",
                new HashSet<>(asList(JAVA, GUITAR)), "");
        URI uri = restTemplate.postForLocation("/user", user);
        assertThat(uri).isNotNull();
        User createdUser = restTemplate.getForObject(uri, User.class);
        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    public void should_have_status_404_when_requesting_non_existing_user() {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity("/user/9999", User.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}
