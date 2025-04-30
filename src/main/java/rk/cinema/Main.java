package rk.cinema;

import rk.cinema.model.Cinema;
import rk.cinema.model.ClientTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Cinema cinema = new Cinema(100);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<ClientTask> tasks = new ArrayList<>();
        IntStream.range(1, 51)
                .mapToObj(i -> new ClientTask(cinema, i))
                .forEachOrdered(tasks::add);

        tasks.forEach(executor::execute);

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nFinal reservation count: " + cinema.getReservedSeatsCount());
    }
}