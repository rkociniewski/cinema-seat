package rk.cinema.model;

import rk.cinema.error.IllegalSeatReservedException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static rk.cinema.util.Const.DEFAULT_SEAT_COUNT;
import static rk.cinema.util.Const.UNRESERVED;

/**
 * Manages seat reservations in a cinema using a thread-safe structure.
 * <p>
 * Allows clients to reserve and cancel seats by client ID.
 * Ensures that only the reserving client can cancel their seat.
 */
public class Cinema {
    /**
     * Map of seat numbers to client IDs. A seat is considered free if it maps to UNRESERVED.
     */
    private final Map<Integer, String> seats = new ConcurrentHashMap<>();

    /**
     * Constructs a cinema with a specified number of seats.
     *
     * @param numberOfSeats the number of seats to initialize
     */
    public Cinema(int numberOfSeats) {
        IntStream.rangeClosed(1, numberOfSeats).forEach(i -> seats.put(i, UNRESERVED));
    }

    /**
     * Constructs a cinema with the default number of seats.
     */
    public Cinema() {
        this(DEFAULT_SEAT_COUNT);
    }

    /**
     * Validates whether the given seat number exists.
     *
     * @param seatNumber the seat number to validate
     * @throws IllegalSeatReservedException if the seat number is invalid
     */
    private void validateSeatNumber(int seatNumber) {
        if (!seats.containsKey(seatNumber)) {
            throw new IllegalSeatReservedException("Seat number " + seatNumber + " is invalid.");
        }
    }

    /**
     * Attempts to reserve a seat for the given client.
     *
     * @param seatNumber the seat number to reserve
     * @param clientId   the ID of the client making the reservation
     * @return true if the reservation was successful; false if the seat was already taken
     * @throws IllegalSeatReservedException if the seat number is invalid
     */
    public boolean reserveSeat(int seatNumber, String clientId) {
        validateSeatNumber(seatNumber);
        return seats.replace(seatNumber, UNRESERVED, clientId);
    }

    /**
     * Attempts to cancel a reservation for the given seat and client.
     *
     * @param seatNumber the seat number to cancel
     * @param clientId   the ID of the client attempting the cancellation
     * @return true if the cancellation was successful; false otherwise
     * @throws IllegalSeatReservedException if the seat number is invalid
     */
    public boolean cancelReservation(int seatNumber, String clientId) {
        validateSeatNumber(seatNumber);
        return seats.replace(seatNumber, clientId, UNRESERVED);
    }

    /**
     * Checks if a specific seat is available.
     *
     * @param seatNumber the seat number to check
     * @return true if the seat is available; false if reserved
     * @throws IllegalSeatReservedException if the seat number is invalid
     */
    public boolean isSeatAvailable(int seatNumber) {
        validateSeatNumber(seatNumber);
        return UNRESERVED.equals(seats.get(seatNumber));
    }

    /**
     * Counts the number of currently reserved seats.
     *
     * @return the number of seats that are reserved
     */
    public long getReservedSeatsCount() {
        return seats.values().stream().filter(v -> !UNRESERVED.equals(v)).count();
    }
}