package com.together.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Getter
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;

    private String description;

    @Embedded
    private Location location;

    private Activity activity;

    public Event(String name, String description, Location location, Activity activity) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.activity = activity;
    }
}
