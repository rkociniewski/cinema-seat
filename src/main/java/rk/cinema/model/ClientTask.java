package rk.cinema.model;

import java.util.Random;
import java.util.UUID;

public class ClientTask implements Runnable {
    private static final ThreadLocal<String> clientId = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    private final Cinema cinema;
    private final Random random = new Random();

    public ClientTask(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        int seatNumber = random.nextInt(100) + 1; // Take a random seat
        String client = clientId.get();
        System.out.println("Client " + client + " is checking seat: " + seatNumber);

        if (cinema.isSeatAvailable(seatNumber)) {
            boolean reserved = cinema.reserveSeat(seatNumber);
            System.out.println("Client " + client + (reserved ? " reserved a seat " : " couldn't reserve the seat for ") + seatNumber);
        } else {
            boolean canceled = cinema.cancelReservation(seatNumber);
            System.out.println("Client " + client + (canceled ? " cancel a seat reservation " : " couldn't cancel a seat reservation  for ") + seatNumber);
        }
    }
}
