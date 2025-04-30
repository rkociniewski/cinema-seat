package rk.cinema.error;

public class IllegalSeatReservedException extends ArrayIndexOutOfBoundsException {
    public IllegalSeatReservedException(String message) {
        super(message);
    }
}
