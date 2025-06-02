package rk.powermilk.cinema;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Simple smoke test to verify that the application starts without throwing exceptions.
 */
class MainTest {

    @Test
    void should_run_main_without_exceptions() {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }
}
