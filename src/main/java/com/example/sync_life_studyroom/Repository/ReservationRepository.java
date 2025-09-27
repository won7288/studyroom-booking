package com.example.sync_life_studyroom.Repository;

import com.example.sync_life_studyroom.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT EXISTS (" +
            "SELECT 1 FROM reservation " +
            "WHERE room_id = :roomId AND tstzrange(start_at, end_at) && tstzrange(:startAt, :endAt)" +
            ")", nativeQuery = true)
    boolean findOverlapping(@Param("roomId") Long roomId,
                          @Param("startAt") java.time.LocalDateTime startAt,
                          @Param("endAt") java.time.LocalDateTime endAt);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.startAt >= :startOfDay AND r.startAt < :endOfDay ORDER BY r.startAt")
    List<Reservation> findByRoomAndDate(@Param("roomId") Long roomId,
                                        @Param("startOfDay") LocalDateTime startOfDay,
                                        @Param("endOfDay") LocalDateTime endOfDay);
}
