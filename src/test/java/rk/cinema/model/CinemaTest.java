package rk.cinema.model;

import org.junit.jupiter.api.Assertions;
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
    void should_All_Seat_Be_Free() {
        cinema.reserveSeat(1, client1Id);
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    @Test
    void should_One_Seat_Be_Reserved() {
        assertEquals(0, cinema.getReservedSeatsCount());
    }

    @Test
    void should_reserve_empty_seat() {
        assertTrue(cinema.reserveSeat(1, client1Id));
    }

    @Test
    void should_reservation_check_return_false() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.isSeatAvailable(1));
    }

    @Test
    void should_reservation_check_return_true() {
        assertTrue(cinema.isSeatAvailable(1));
    }

    @Test
    void should_not_reserve_already_taken_seat() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.reserveSeat(1, client2Id));
    }

    @Test
    void should_not_cancel_reservation_for_empty_seat() {
        assertFalse(cinema.cancelReservation(1, client1Id));
    }

    @Test
    void should_cancel_reservation_for_taken_seat() {
        cinema.reserveSeat(1, client1Id);
        assertTrue(cinema.cancelReservation(1, client1Id));
    }

    @Test
    void should_not_cancel_reservation_for_non_client_seat() {
        cinema.reserveSeat(1, client1Id);
        assertFalse(cinema.cancelReservation(1, client2Id));
    }

    @Test
    void should_throw_exception_for_illegal_seat_for_reservation() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () -> cinema.reserveSeat(-1, client1Id));
        assertEquals("Seat number -1 is invalid.", exception.getMessage());
    }

    @Test
    void should_throw_exception_for_illegal_seat_for_cancellation() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () -> cinema.cancelReservation(101, client1Id));
        assertEquals("Seat number 101 is invalid.", exception.getMessage());
    }

    @Test
    void should_throw_exception_for_illegal_seat_for_reservation_check() {
        IllegalSeatReservedException exception = assertThrows(IllegalSeatReservedException.class, () -> cinema.isSeatAvailable(1010));
        assertEquals("Seat number 1010 is invalid.", exception.getMessage());
    }

    @Test
    void should_reserve_multiple_seats() {
        IntStream.rangeClosed(1, 5).mapToObj(i -> cinema.reserveSeat(i, client1Id)).forEachOrdered(Assertions::assertTrue);
        assertEquals(5, cinema.getReservedSeatsCount());
    }

    @Test
    void should_not_change_state_when_reserving_same_seat_twice_by_same_client() {
        cinema.reserveSeat(1, client1Id);
        boolean secondTry = cinema.reserveSeat(1, client1Id);
        assertFalse(secondTry);
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    @Test
    void should_not_reserve_when_all_seats_are_taken() {
        IntStream.rangeClosed(1, 5).forEach(i -> cinema.reserveSeat(i, client1Id));
        assertFalse(cinema.reserveSeat(1, client2Id));
        assertEquals(5, cinema.getReservedSeatsCount());
    }

    @Test
    void should_allow_re_reservation_after_cancellation() {
        cinema.reserveSeat(1, client1Id);
        cinema.cancelReservation(1, client1Id);
        assertTrue(cinema.reserveSeat(1, client2Id));
    }
}