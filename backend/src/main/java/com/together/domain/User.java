package com.together.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Getter
@ToString(exclude = "id")
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Gender gender;

    private String city;

    @ElementCollection
    private Set<Activity> activities;

    @Column(length = 100_000)
    private String picture;


    public User(String firstName, String lastName, LocalDate birthDate, Gender gender, String city, Set<Activity> activities, String picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.city = city;
        this.activities = activities;
        this.picture = picture;
    }
}
