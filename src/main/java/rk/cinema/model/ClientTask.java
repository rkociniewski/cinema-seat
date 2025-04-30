package rk.cinema.model;

import java.util.UUID;

public class ClientTask implements Runnable {
    private final String clientId = UUID.randomUUID().toString();
    private final Cinema cinema;
    private final int seatNumber;

    public ClientTask(Cinema cinema, int seatNumber) {
        this.cinema = cinema;
        this.seatNumber = seatNumber;
    }

    @Override
    public void run() {
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