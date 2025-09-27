package com.example.sync_life_studyroom.Controller;

import com.example.sync_life_studyroom.Entity.Room;
import com.example.sync_life_studyroom.Entity.User;
import com.example.sync_life_studyroom.Service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/rooms")
    public Room createRoom(@RequestBody Room room, HttpServletRequest req) {
        User user = (User) req.getAttribute("currentUser");
        return roomService.createRoom(room, user);
    }

    @GetMapping("/rooms")
    public List<Map<String, Object>> listRooms(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return roomService.listRoomsWithReservations(localDate);
    }
}
