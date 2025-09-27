package com.example.sync_life_studyroom;

import com.example.sync_life_studyroom.Entity.Role;
import com.example.sync_life_studyroom.Entity.Room;
import com.example.sync_life_studyroom.Entity.User;
import com.example.sync_life_studyroom.Repository.ReservationRepository;
import com.example.sync_life_studyroom.Repository.RoomRepository;
import com.example.sync_life_studyroom.Service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
@ActiveProfiles("test")
class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testConcurrentReservations() throws InterruptedException {

        Room room = roomRepository.save(new Room("B회의실", "2층", 4));

        User user = new User(1L, Role.USER);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<Future<Boolean>> results = new ArrayList<>();

        LocalDateTime start = LocalDateTime.of(2025, 10, 1, 10, 0);
        LocalDateTime end = start.plusHours(1);

        for (int i = 0; i < threadCount; i++) {
            results.add(executor.submit(() -> {
                latch.countDown();
                latch.await(); // 모든 스레드 동시에 시작
                try {
                    reservationService.createReservation(room.getId(), start, end, user);
                    return true; // 예약 성공
                } catch (Exception e) {
                    return false; // 예약 실패 (중복 등)
                }
            }));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        long successCount = results.stream().filter(f -> {
            try { return f.get(); } catch (Exception e) { return false; }
        }).count();

        System.out.println("성공한 예약 수: " + successCount);
        assert successCount == 1; // DB EXCLUDE 제약으로 1건만 성공
    }
}
