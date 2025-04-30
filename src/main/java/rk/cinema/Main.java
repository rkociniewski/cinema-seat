package rk.cinema;

import rk.cinema.model.Cinema;
import rk.cinema.model.ClientTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Cinema cinema = new Cinema(100);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<ClientTask> tasks = new ArrayList<>();
        IntStream.range(0, 50).mapToObj(i -> new ClientTask(cinema)).forEachOrdered(item -> tasks.add(item));

        tasks.forEach(executor::execute);
        executor.shutdown();
    }
}
