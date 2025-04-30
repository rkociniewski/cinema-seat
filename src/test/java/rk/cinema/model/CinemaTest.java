package rk.cinema.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rk.cinema.error.IllegalSeatReservedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CinemaTest {
    private final Cinema cinema = new Cinema(50);
    private final String client1Id = "TEST_CLIENT_1";
    private final String client2Id = "TEST_CLIENT_2";

    @Test
    void shouldAllSeatBeFree() {
        cinema.reserveSeat(1, client1Id);
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    @Test
    void shouldOneSeatBeReserved() {
        assertEquals(0, cinema.getReservedSeatsCount());
    }

    @Test
    void shouldReserveEmptySeat() {
        Assertions.assertTrue(cinema.reserveSeat(1, client1Id));
    }

    @Test
    void shouldReservationCheckReturnFalse() {
        cinema.reserveSeat(1, client1Id);
        Assertions.assertFalse(cinema.isSeatAvailable(1));
    }

    @Test
    void shouldReservationCheckReturnTrue() {
        Assertions.assertTrue(cinema.isSeatAvailable(1));
    }

    @Test
    void shouldNotReserveAlreadyTakenSeat() {
        cinema.reserveSeat(1, client1Id);
        Assertions.assertFalse(cinema.reserveSeat(1, client2Id));
    }

    @Test
    void shouldNotCancelReservationForEmptySeat() {
        Assertions.assertFalse(cinema.cancelReservation(1, client1Id));
    }

    @Test
    void shouldCancelReservationForTakenSeat() {
        cinema.reserveSeat(1, client1Id);
        Assertions.assertTrue(cinema.cancelReservation(1, client1Id));
    }

    @Test
    void shouldNotCancelReservationForNonClientSeat() {
        cinema.reserveSeat(1, client1Id);
        Assertions.assertFalse(cinema.cancelReservation(1, client2Id));
    }

    @Test
    void shouldThrowExceptionForIllegalSeatForReservation() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () -> cinema.reserveSeat(-1, client1Id));
        assertEquals("Seat number -1 is invalid.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForIllegalSeatForCancellation() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () -> cinema.cancelReservation(101, client1Id));
        assertEquals("Seat number 101 is invalid.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForIllegalSeatForReservationCheck() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () -> cinema.isSeatAvailable(1010));
        assertEquals("Seat number 1010 is invalid.", exception.getMessage());
    }
}