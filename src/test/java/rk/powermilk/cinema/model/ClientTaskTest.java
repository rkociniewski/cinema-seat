package rk.powermilk.cinema.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link ClientTask} class to verify correct reservation behavior.
 */
class ClientTaskTest {

    private Cinema cinema;

    @BeforeEach
    void setUp() {
        cinema = new Cinema(10);
    }

    @Test
    void should_reserve_seat_when_run_is_called() {
        int seatNumber = 1;

        ClientTask task = new ClientTask(cinema, seatNumber);
        task.run();

        assertFalse(cinema.isSeatAvailable(seatNumber));
        assertEquals(1, cinema.getReservedSeatsCount());
    }

    @Test
    void should_not_throw_when_reserving_invalid_seat() {
        int seatNumber = -5;

        ClientTask task = new ClientTask(cinema, seatNumber);

        // Even if seat number is invalid, run() should not throw. It should be internally handled.
        assertDoesNotThrow(task::run);
    }
}
