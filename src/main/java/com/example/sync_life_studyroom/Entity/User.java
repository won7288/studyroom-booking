package com.example.sync_life_studyroom.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id; //null 이면 ADMIN
    private Role role;
}
