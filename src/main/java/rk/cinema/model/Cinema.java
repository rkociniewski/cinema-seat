package rk.cinema.model;

import rk.cinema.error.IllegalSeatReservedException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class Cinema {
    private final Map<Integer, String> seats = new ConcurrentHashMap<>();

    public Cinema(int numberOfSeats) {
        IntStream.rangeClosed(1, numberOfSeats).forEach(i -> seats.put(i, ""));
    }

    private void validateSeatNumber(int seatNumber) {
        if (!seats.containsKey(seatNumber)) {
            throw new IllegalSeatReservedException("Seat number " + seatNumber + " is invalid.");
        }
    }

    public boolean reserveSeat(int seatNumber, String clientId) {
        validateSeatNumber(seatNumber);
        return Objects.equals(seats.computeIfPresent(seatNumber, (k, v) -> v == "" ? clientId : v), clientId);
    }

    public boolean cancelReservation(int seatNumber, String clientId) {
        validateSeatNumber(seatNumber);
        return seats.remove(seatNumber, clientId);
    }

    public boolean isSeatAvailable(int seatNumber) {
        validateSeatNumber(seatNumber);
        return seats.get(seatNumber) == null;
    }

    public long getReservedSeatsCount() {
        return seats.values().stream().filter(Objects::nonNull).count();
    }
}
