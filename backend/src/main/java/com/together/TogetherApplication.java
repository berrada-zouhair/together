package com.together;

import com.together.domain.Event;
import com.together.domain.Gender;
import com.together.domain.Location;
import com.together.domain.User;
import com.together.service.EventService;
import com.together.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static com.together.domain.Activity.*;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;

@SpringBootApplication
public class TogetherApplication {

	@AllArgsConstructor
	@Component
	@Profile("!test")
	class EventInitializer implements CommandLineRunner {

		private EventService eventService;
		private UserService userService;

		private void insertEvents() {
			Event event1 = new Event("Foot quartier Puteaux",
					"Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression." +
							" Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500",
					now().plusMinutes(40), new Location(0D, 0D), FOOTBALL);

			Event event2 = new Event("Java coding session",
					"Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression." +
							" Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500",
					now().plusHours(1), new Location(0D, 0D), JAVA);

			Event event3 = new Event("Cours apprentissage guitar",
					"Le Lorem Ipsum est simplement du faux texte employé dans la composition et la mise en page avant impression." +
							" Le Lorem Ipsum est le faux texte standard de l'imprimerie depuis les années 1500",
					now().plusHours(2).plusMinutes(15), new Location(0D, 0D), GUITAR);

			List<Event> events = asList(event1, event2, event3);
			User owner = userService.get(1L);
			events.forEach(event -> {
				event.setOwner(owner);
				eventService.add(event);
			});
		}

		private void insertUser() {
			User user = new User("Zouhair", "BERRADA", LocalDate.now(), Gender.MAN, "Paris",
					new HashSet<>(asList(JAVA, FOOTBALL, GUITAR)), "");
			userService.create(user);
		}

		@Override
		public void run(String... args) {
			insertUser();
			insertEvents();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(TogetherApplication.class, args);
	}

}
