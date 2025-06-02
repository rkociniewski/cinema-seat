package rk.powermilk.cinema.error;

/**
 * Exception thrown when a seat number is invalid (out of bounds).
 */
public class IllegalSeatReservedException extends ArrayIndexOutOfBoundsException {
    public IllegalSeatReservedException(String message) {
        super(message);
    }
}
