package rk.powermilk.cinema.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Concurrency tests for the {@link Cinema} class.
 * <p>
 * These tests validate thread safety and correctness of concurrent operations like reservation and cancellation.
 */
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class ConcurrentCinemaTest {
    private Cinema cinema;
    private final int threadCount = 100;

    @BeforeEach
    void setUp() {
        cinema = new Cinema();
    }

    @Test
    /**
     * Verifies that when multiple clients attempt to reserve the same seat concurrently,
     * only one succeeds and the rest fail.
     */
    void givenMultipleClients_whenReservingSameSeat_thenOnlyOneSucceeds() throws InterruptedException {
        int seatToReserve = 1;
        List<Boolean> results = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            String clientId = "client-" + i;
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    boolean result = cinema.reserveSeat(seatToReserve, clientId);
                    results.add(result);
                } catch (InterruptedException ignored) {
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();
        executor.shutdown();

        long successCount = results.stream().filter(Boolean::booleanValue).count();
        assertEquals(1, successCount, "Only one reservation should succeed");
    }

    @Test
    /**
     * Verifies that when each client attempts to reserve a different seat concurrently,
     * all succeed without conflicts.
     */
    void givenMultipleClients_whenReservingDifferentSeats_thenAllSucceed() throws InterruptedException {
        List<Boolean> results = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int seatNumber = i + 1;
            String clientId = "client-" + i;
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    boolean result = cinema.reserveSeat(seatNumber, clientId);
                    results.add(result);
                } catch (InterruptedException ignored) {
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();
        executor.shutdown();

        long successCount = results.stream().filter(Boolean::booleanValue).count();
        assertEquals(threadCount, successCount, "All clients should reserve successfully");
    }

    @Test
    /**
     * Verifies that when one client reserves a seat and another tries to cancel it concurrently,
     * only the reserver can successfully perform the action.
     */
    void givenReserverAndIntruder_whenBothActConcurrently_thenOnlyReserverCanCancel() throws InterruptedException {
        int seatNumber = 1;
        String reserverId = "client-RESERVER";
        String intruderId = "client-INTRUDER";

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(2);

        final boolean[] reservationResult = new boolean[1];
        final boolean[] cancellationResult = new boolean[1];

        Thread reserver = new Thread(() -> {
            try {
                start.await();
                reservationResult[0] = cinema.reserveSeat(seatNumber, reserverId);
            } catch (InterruptedException ignored) {
            } finally {
                done.countDown();
            }
        });

        Thread canceller = new Thread(() -> {
            try {
                start.await();
                cancellationResult[0] = cinema.cancelReservation(seatNumber, intruderId);
            } catch (InterruptedException ignored) {
            } finally {
                done.countDown();
            }
        });

        reserver.start();
        canceller.start();
        start.countDown();
        done.await();

        assertTrue(reservationResult[0] || cancellationResult[0], "At least one operation should succeed");
        assertFalse(cancellationResult[0], "Intruder should not be able to cancel");
        assertFalse(cinema.isSeatAvailable(seatNumber), "Seat should remain reserved");
    }

    @Test
    /**
     * Verifies that all clients who reserved their own seats can successfully cancel them concurrently.
     */
    void should_all_clients_cancel_their_own_seats_concurrently() throws InterruptedException {
        IntStream.range(0, threadCount).forEach(i ->
            cinema.reserveSeat(i + 1, "client-" + i));

        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        List<Boolean> results = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int seat = i + 1;
            String clientId = "client-" + i;
            executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    boolean result = cinema.cancelReservation(seat, clientId);
                    results.add(result);
                } catch (InterruptedException ignored) {
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();
        executor.shutdown();

        long canceled = results.stream().filter(Boolean::booleanValue).count();
        assertEquals(100, canceled);
    }
}
