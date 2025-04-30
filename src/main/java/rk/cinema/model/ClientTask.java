package rk.cinema.model;

import java.util.Random;
import java.util.UUID;

public class ClientTask implements Runnable {
    private final String clientId = UUID.randomUUID().toString();
    private final Cinema cinema;
    private final Random random = new Random();

    public ClientTask(Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        int seatNumber = random.nextInt(100) + 1;
        System.out.println("Client " + clientId + " is checking seat: " + seatNumber);

        if (cinema.isSeatAvailable(seatNumber)) {
            boolean reserved = cinema.reserveSeat(seatNumber, clientId);
            System.out.println("Client " + clientId + (reserved ? " reserved seat " : " couldn't reserve seat ") + seatNumber);
        } else {
            boolean canceled = cinema.cancelReservation(seatNumber, clientId);
            System.out.println("Client " + clientId + (canceled ? " canceled reservation for seat " : " couldn't cancel reservation for seat ") + seatNumber);
        }
    }
}
