package rk.powermilk.cinema.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import rk.powermilk.cinema.error.IllegalSeatReservedException;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Cinema} class.
 * <p>
 * These tests verify the correctness of reservation, cancellation, and availability logic
 * under single-threaded conditions.
 */
@Timeout(value = 2, unit = TimeUnit.SECONDS)
class CinemaTest {
    private final Cinema cinema = new Cinema(5);
    private final String client1Id = "TEST_CLIENT_1";
    private final String client2Id = "TEST_CLIENT_2";

    /**
     * Verifies that initially, all seats are unreserved.
     */
    @Test
    void givenNoReservations_whenChecked_thenSeatCountIsZero() {
        assertEquals(0, cinema.getReservedSeatsCount());
    }

    /**
     * Verifies that after one reservation, the count of reserved seats is one.
     */
    @Test
    void givenReservation_whenChecked_thenSeatCountIsOne() {
        cinema.reserveSeat(1, client1Id);
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    /**
     * Verifies that a seat can be successfully reserved when it's empty.
     */
    @Test
    void givenEmptySeat_whenReserved_thenReturnsTrue() {
        assertTrue(cinema.reserveSeat(1, client1Id));
    }

    /**
     * Verifies that a reserved seat is reported as unavailable.
     */
    @Test
    void givenReservedSeat_whenCheckedAvailability_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.isSeatAvailable(1));
    }

    /**
     * Verifies that an unreserved seat is reported as available.
     */
    @Test
    void givenUnreservedSeat_whenCheckedAvailability_thenReturnsTrue() {
        assertTrue(cinema.isSeatAvailable(1));
    }

    /**
     * Verifies that a seat already taken by one client cannot be reserved by another.
     */
    @Test
    void givenReservedSeat_whenAnotherClientTriesToReserve_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.reserveSeat(1, client2Id));
    }

    /**
     * Verifies that canceling a seat that was never reserved fails.
     */
    @Test
    void givenUnreservedSeat_whenCancelled_thenReturnsFalse() {
        assertFalse(cinema.cancelReservation(1, client1Id));
    }

    /**
     * Verifies that the client who reserved a seat can cancel it.
     */
    @Test
    void givenReservationByClient_whenCancelled_thenReturnsTrue() {
        cinema.reserveSeat(1, client1Id);
        assertTrue(cinema.cancelReservation(1, client1Id));
    }

    /**
     * Verifies that a client cannot cancel a seat reserved by someone else.
     */
    @Test
    void givenReservationByClient1_whenClient2Cancels_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.cancelReservation(1, client2Id));
    }

    /**
     * Verifies that reserving a seat with an invalid seat number throws an exception.
     */
    @Test
    void givenInvalidSeatNumber_whenReserving_thenThrowsException() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () ->
            cinema.reserveSeat(-1, client1Id));
        assertEquals("Seat number -1 is invalid.", exception.getMessage());
    }

    /**
     * Verifies that canceling a seat with an invalid seat number throws an exception.
     */
    @Test
    void givenInvalidSeatNumber_whenCancelling_thenThrowsException() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () ->
            cinema.cancelReservation(101, client1Id));
        assertEquals("Seat number 101 is invalid.", exception.getMessage());
    }

    /**
     * Verifies that checking availability for an invalid seat throws an exception.
     */
    @Test
    void givenInvalidSeatNumber_whenCheckingAvailability_thenThrowsException() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () ->
            cinema.isSeatAvailable(1010));
        assertEquals("Seat number 1010 is invalid.", exception.getMessage());
    }

    /**
     * Verifies that multiple sequential seat reservations are all successful.
     */
    @Test
    void givenMultipleSeats_whenReserved_thenAllSucceed() {
        IntStream.rangeClosed(1, 5)
            .mapToObj(i -> cinema.reserveSeat(i, client1Id))
            .forEach(result -> assertTrue(result));
        assertEquals(5, cinema.getReservedSeatsCount());
    }

    /**
     * Verifies that a client cannot reserve the same seat more than once.
     */
    @Test
    void givenSeatReservedTwiceBySameClient_whenReservedAgain_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        boolean secondTry = cinema.reserveSeat(1, client1Id);
        assertFalse(secondTry);
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    /**
     * Verifies that no additional reservation is possible once all seats are taken.
     */
    @Test
    void givenAllSeatsReserved_whenAnotherClientTriesToReserve_thenFails() {
        IntStream.rangeClosed(1, 5).forEach(i -> cinema.reserveSeat(i, client1Id));
        assertFalse(cinema.reserveSeat(1, client2Id));
        assertEquals(5, cinema.getReservedSeatsCount());
    }

    /**
     * Verifies that after a reservation is cancelled, another client can reserve the seat.
     */
    @Test
    void givenSeatCancelled_whenAnotherClientReserves_thenSucceeds() {
        cinema.reserveSeat(1, client1Id);
        cinema.cancelReservation(1, client1Id);
        assertTrue(cinema.reserveSeat(1, client2Id));
    }
}
