package rk.cinema.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

public class Cinema {
    private final Map<Integer, Boolean> seats = new HashMap<>();
    private final StampedLock lock = new StampedLock();

    public Cinema(int numberOfSeats) {
        // false = seat isn't reserved
        IntStream.rangeClosed(1, numberOfSeats).forEach(i -> seats.put(i, false));
    }

    public boolean reserveSeat(int seatNumber) {
        long stamp = lock.writeLock();
        try {
            if (Boolean.TRUE.equals(seats.get(seatNumber))) {
                return false; // The seat is reserved already
            }
            seats.put(seatNumber, true);
            return true;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public boolean cancelReservation(int seatNumber) {
        long stamp = lock.writeLock();
        try {
            if (Boolean.FALSE.equals(seats.get(seatNumber))) {
                return false;  // The seat is free
            }
            seats.put(seatNumber, false);
            return true;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public boolean isSeatAvailable(int seatNumber) {
        long stamp = lock.tryOptimisticRead();
        boolean available = Boolean.FALSE.equals(seats.get(seatNumber));
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                available = Boolean.FALSE.equals(seats.get(seatNumber));
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return available;
    }
}
