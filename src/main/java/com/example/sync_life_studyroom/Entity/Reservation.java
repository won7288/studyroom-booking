package com.example.sync_life_studyroom.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Room room;

    private Long userId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public Reservation(Room room, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        this.room = room;
        this.userId = userId;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
