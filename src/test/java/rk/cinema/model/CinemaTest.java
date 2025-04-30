package rk.cinema.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CinemaTest {
    private final Cinema cinema = new Cinema(50);
    private String client1Id = "TEST_CLIENT_1";
    private String client2Id = "TEST_CLIENT_2";

    @Test
    void shouldReservedEmptySeat(){
        boolean result = cinema.reserveSeat(1, client1Id);
        Assertions.assertTrue(result);
    }
}