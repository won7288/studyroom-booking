package com.example.sync_life_studyroom.Service;

import com.example.sync_life_studyroom.Entity.Reservation;
import com.example.sync_life_studyroom.Entity.Role;
import com.example.sync_life_studyroom.Entity.Room;
import com.example.sync_life_studyroom.Entity.User;
import com.example.sync_life_studyroom.Repository.ReservationRepository;
import com.example.sync_life_studyroom.Repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public Reservation createReservation(Long roomId, LocalDateTime startAt, LocalDateTime endAt, User user) {
        if(!startAt.isBefore(endAt)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 시간 범위입니다.");
        }

        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회의실 없음")
        );

        try {
            Reservation reservation = new Reservation(room, user.getId(), startAt, endAt);
            return reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 예약된 시간입니다.");
        }
    }

    @Transactional
    public void cancelReservation(Long reservationId, User user) {
        Reservation res = reservationRepository.findById(reservationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약 없음")
        );

        if(user.getRole() != Role.ADMIN && !res.getUserId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한 없음");
        }

        reservationRepository.delete(res);
    }
}
