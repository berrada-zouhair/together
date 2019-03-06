package com.together.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
//    @Getter
    private Long id;

    private String firstName;

    private String lastName;

    private int age;

    private String city;

    private byte[] picture;

    public User(String firstName, String lastName, int age, String city, byte[] picture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.city = city;
        this.picture = picture;
    }
}
