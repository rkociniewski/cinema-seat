package rk.cinema.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

public class Cinema {
    private final Map<Integer, String> seats = new HashMap<>();
    private final StampedLock lock = new StampedLock();

    public Cinema(int numberOfSeats) {
        IntStream.rangeClosed(1, numberOfSeats).forEachOrdered(i -> seats.put(i, null));
    }

    public boolean reserveSeat(int seatNumber, String clientId) {
        long stamp = lock.writeLock();
        try {
            if (seats.get(seatNumber) != null) return false;
            seats.put(seatNumber, clientId);
            return true;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public boolean cancelReservation(int seatNumber, String clientId) {
        long stamp = lock.writeLock();
        try {
            if (!clientId.equals(seats.get(seatNumber))) return false;
            seats.put(seatNumber, null);
            return true;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public boolean isSeatAvailable(int seatNumber) {
        long stamp = lock.tryOptimisticRead();
        boolean available = (seats.get(seatNumber) == null);
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                available = (seats.get(seatNumber) == null);
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return available;
    }

    public long getReservedSeatsCount() {
        long stamp = lock.readLock();
        try {
            return seats.values().stream().filter(v -> v != null).count();
        } finally {
            lock.unlockRead(stamp);
        }
    }
}