package com.together.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode(exclude = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private LocalDateTime now;

    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User owner;

    public Comment(LocalDateTime now, String text) {
        this.now = now;
        this.text = text;
    }
}
