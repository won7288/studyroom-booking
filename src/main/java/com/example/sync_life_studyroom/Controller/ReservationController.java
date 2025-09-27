package com.example.sync_life_studyroom.Controller;

import com.example.sync_life_studyroom.Entity.Reservation;
import com.example.sync_life_studyroom.Entity.User;
import com.example.sync_life_studyroom.Service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public Reservation createReservation(@RequestBody Map<String,String> body, HttpServletRequest req) {
        User user = (User) req.getAttribute("currentUser");
        Long roomId = Long.parseLong(body.get("roomId"));
        LocalDateTime startAt = LocalDateTime.parse(body.get("startAt"));
        LocalDateTime endAt = LocalDateTime.parse(body.get("endAt"));
        return reservationService.createReservation(roomId, startAt, endAt, user);
    }

    @DeleteMapping("/reservations/{id}")
    public void cancelReservation(@PathVariable Long id, HttpServletRequest req) {
        User user = (User) req.getAttribute("currentUser");
        reservationService.cancelReservation(id, user);
    }
}
