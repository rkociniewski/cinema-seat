package rk.cinema.model;

import rk.cinema.error.IllegalSeatReservedException;

import java.util.UUID;

public record ClientTask(Cinema cinema, int seatNumber) implements Runnable {
    private static final ThreadLocal<String> clientId = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    @Override
    public void run() {
        String id = clientId.get();
        try {
            System.out.println("Client " + id + " is checking seat: " + seatNumber);

            if (cinema.isSeatAvailable(seatNumber)) {
                boolean reserved = cinema.reserveSeat(seatNumber, id);
                System.out.println("Client " + id + (reserved ? " reserved seat " : " couldn't reserve seat ") + seatNumber);
            } else {
                boolean canceled = cinema.cancelReservation(seatNumber, id);
                System.out.println("Client " + id + (canceled ? " canceled reservation for seat " : " couldn't cancel reservation for seat ") + seatNumber);
            }
        } catch (IllegalSeatReservedException e) {
            System.err.println("Client " + id + " error: " + e.getMessage());
        }
    }
}