package com.together.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Getter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "id")
public class Event {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime date;

    @Embedded
    private Location location;

    private Activity activity;

    @OneToOne
    @JoinColumn(name = "owner_id")
    @Setter
    private User owner;

    @ManyToMany
    @JoinTable(name = "event_user",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants;

    public Event(String name, String description, LocalDateTime date, Location location, Activity activity) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.location = location;
        this.activity = activity;
        this.participants = new HashSet<>();
    }

    public void addParticipant(User user) {
        participants.add(user);
    }

}
