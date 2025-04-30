package rk.cinema.model;

import lombok.extern.slf4j.Slf4j;
import rk.cinema.error.IllegalSeatReservedException;

import java.util.UUID;

/**
 * Represents a client action that is executed concurrently.
 * <p>
 * Each task attempts to reserve or cancel a specific seat in the cinema.
 * Uses a thread-local client ID to simulate unique users.
 */
@Slf4j
public record ClientTask(Cinema cinema, int seatNumber) implements Runnable {
    /**
     * Thread-local client ID simulating a unique user per thread.
     */
    private static final ThreadLocal<String> clientId = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    @Override
    public void run() {
        String id = clientId.get();
        try {
            log.debug("Client {} is checking seat {}", id, seatNumber);

            if (cinema.isSeatAvailable(seatNumber)) {
                boolean reserved = cinema.reserveSeat(seatNumber, id);
                log.info("Client {}{} seat {}", id, reserved ? " reserved" : " couldn't reserve", seatNumber);
            } else {
                boolean canceled = cinema.cancelReservation(seatNumber, id);
                log.info("Client {}{} reservation for seat {}", id, canceled ? " canceled" : " couldn't cancel", seatNumber);
            }
        } catch (IllegalSeatReservedException e) {
            log.error("Client {} error: {}", id, e.getMessage());
        }
    }
}