package com.together.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Gender gender;

    private String city;

    @Column(length = 100_000)
    private String picture;

    public User(String firstName, String lastName, LocalDate birthDate, Gender gender, String city, String picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.city = city;
        this.picture = picture;
    }
}
