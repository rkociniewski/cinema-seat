package rk.powermilk.cinema;

import lombok.extern.slf4j.Slf4j;
import rk.powermilk.cinema.model.Cinema;
import rk.powermilk.cinema.model.ClientTask;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Entry point for the Cinema Reservation application.
 * <p>
 * Initializes a cinema with a default number of seats and runs multiple concurrent client tasks
 * that attempt to reserve or cancel seats.
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        Cinema cinema = new Cinema();
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            final Random random = new Random();
            IntStream.range(1, 100)
                .mapToObj(i -> new ClientTask(cinema, random.nextInt(100) + 1))
                .forEachOrdered(executor::execute);

        } // try-with-resources ensures shutdown

        log.info("Final reservation count: {}", cinema.getReservedSeatsCount());
    }
}
