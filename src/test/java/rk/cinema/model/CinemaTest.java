package rk.cinema.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import rk.cinema.error.IllegalSeatReservedException;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(value = 2, unit = TimeUnit.SECONDS)
class CinemaTest {
    private final Cinema cinema = new Cinema(5);
    private final String client1Id = "TEST_CLIENT_1";
    private final String client2Id = "TEST_CLIENT_2";

    @Test
    void givenNoReservations_whenChecked_thenSeatCountIsZero() {
        assertEquals(0, cinema.getReservedSeatsCount());
    }

    @Test
    void givenReservation_whenChecked_thenSeatCountIsOne() {
        cinema.reserveSeat(1, client1Id);
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    @Test
    void givenEmptySeat_whenReserved_thenReturnsTrue() {
        assertTrue(cinema.reserveSeat(1, client1Id));
    }

    @Test
    void givenReservedSeat_whenCheckedAvailability_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.isSeatAvailable(1));
    }

    @Test
    void givenUnreservedSeat_whenCheckedAvailability_thenReturnsTrue() {
        assertTrue(cinema.isSeatAvailable(1));
    }

    @Test
    void givenReservedSeat_whenAnotherClientTriesToReserve_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.reserveSeat(1, client2Id));
    }

    @Test
    void givenUnreservedSeat_whenCancelled_thenReturnsFalse() {
        assertFalse(cinema.cancelReservation(1, client1Id));
    }

    @Test
    void givenReservationByClient_whenCancelled_thenReturnsTrue() {
        cinema.reserveSeat(1, client1Id);
        assertTrue(cinema.cancelReservation(1, client1Id));
    }

    @Test
    void givenReservationByClient1_whenClient2Cancels_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.cancelReservation(1, client2Id));
    }

    @Test
    void givenInvalidSeatNumber_whenReserving_thenThrowsException() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () ->
                cinema.reserveSeat(-1, client1Id));
        assertEquals("Seat number -1 is invalid.", exception.getMessage());
    }

    @Test
    void givenInvalidSeatNumber_whenCancelling_thenThrowsException() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () ->
                cinema.cancelReservation(101, client1Id));
        assertEquals("Seat number 101 is invalid.", exception.getMessage());
    }

    @Test
    void givenInvalidSeatNumber_whenCheckingAvailability_thenThrowsException() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () ->
                cinema.isSeatAvailable(1010));
        assertEquals("Seat number 1010 is invalid.", exception.getMessage());
    }

    @Test
    void givenMultipleSeats_whenReserved_thenAllSucceed() {
        IntStream.rangeClosed(1, 5)
                .mapToObj(i -> cinema.reserveSeat(i, client1Id))
                .forEach(result -> assertTrue(result));
        assertEquals(5, cinema.getReservedSeatsCount());
    }

    @Test
    void givenSeatReservedTwiceBySameClient_whenReservedAgain_thenReturnsFalse() {
        cinema.reserveSeat(1, client1Id);
        boolean secondTry = cinema.reserveSeat(1, client1Id);
        assertFalse(secondTry);
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    @Test
    void givenAllSeatsReserved_whenAnotherClientTriesToReserve_thenFails() {
        IntStream.rangeClosed(1, 5).forEach(i -> cinema.reserveSeat(i, client1Id));
        assertFalse(cinema.reserveSeat(1, client2Id));
        assertEquals(5, cinema.getReservedSeatsCount());
    }

    @Test
    void givenSeatCancelled_whenAnotherClientReserves_thenSucceeds() {
        cinema.reserveSeat(1, client1Id);
        cinema.cancelReservation(1, client1Id);
        assertTrue(cinema.reserveSeat(1, client2Id));
    }
}