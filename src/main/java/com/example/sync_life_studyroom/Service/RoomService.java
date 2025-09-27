package com.example.sync_life_studyroom.Service;

import com.example.sync_life_studyroom.Entity.Reservation;
import com.example.sync_life_studyroom.Entity.Role;
import com.example.sync_life_studyroom.Entity.Room;
import com.example.sync_life_studyroom.Entity.User;
import com.example.sync_life_studyroom.Repository.ReservationRepository;
import com.example.sync_life_studyroom.Repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public Room createRoom(Room room, User user) {
        if(user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한 없음");
        }
        return roomRepository.save(room);
    }

    public List<Map<String, Object>> listRoomsWithReservations(LocalDate date) {
        List<Room> rooms = roomRepository.findAll();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Map<String, Object>> result = new ArrayList<>();
        for(Room room : rooms) {
            List<Reservation> reservations = reservationRepository.findByRoomAndDate(room.getId(), startOfDay, endOfDay);
            Map<String, Object> map = new HashMap<>();
            map.put("roomId", room.getId());
            map.put("name", room.getName());
            map.put("reservations", reservations.stream().map(r -> Map.of(
                    "startAt", r.getStartAt(),
                    "endAt", r.getEndAt()
            )).toList());
            result.add(map);
        }
        return result;
    }
}
