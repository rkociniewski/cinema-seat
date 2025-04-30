package rk.cinema;

import rk.cinema.model.Cinema;
import rk.cinema.model.ClientTask;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Cinema cinema = new Cinema(100);
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            final Random random = new Random();
            IntStream.range(1, 51)
                    .mapToObj(i -> new ClientTask(cinema, random.nextInt(51)))
                    .forEachOrdered(executor::execute);

        } // try-with-resources ensures shutdown

        System.out.println("\nFinal reservation count: " + cinema.getReservedSeatsCount());
    }
}