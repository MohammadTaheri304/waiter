package ir.annotation.waiter.core;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class AsynchronousProcedureTest {

    @Test(expected = NullPointerException.class)
    public void testNullIdentifier() {
        new AsynchronousProcedure<>(null);
    }

    @Test
    public void constructionTest() {
        var getSettings = new AsynchronousProcedure<>("getSettings");

        assertEquals(getSettings.getIdentifier(), "getSettings");
    }

    @Test
    public void equalsTest() {
        var firstGetSettings = new AsynchronousProcedure<>("getSettings");
        var secondGetSettings = new AsynchronousProcedure<>("getSettings");
        var updateSettings = new AsynchronousProcedure<>("updateSettings");
        var deleteSettings = new AsynchronousProcedure<>("deleteSettings");

        assertEquals(firstGetSettings, secondGetSettings);
        assertNotEquals(updateSettings, deleteSettings);
    }

    @Test
    public void hashCodeTest() {
        var firstGetSettings = new AsynchronousProcedure<>("getSettings");
        var secondGetSettings = new AsynchronousProcedure<>("getSettings");
        var updateSettings = new AsynchronousProcedure<>("updateSettings");
        var deleteSettings = new AsynchronousProcedure<>("deleteSettings");

        assertEquals(firstGetSettings.hashCode(), secondGetSettings.hashCode());
        assertNotEquals(updateSettings.hashCode(), deleteSettings.hashCode());
    }

    @Test
    public void testDefaultImplementation() throws InterruptedException, ExecutionException {
        var getSettings = new AsynchronousProcedure<String, String>("getSettings");

        var result = getSettings.apply(Executors.newSingleThreadExecutor(), "Hello").get();
        assertTrue(result.isEmpty());
    }
}