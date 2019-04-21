package ir.annotation.waiter.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProcedureTest {

    @Test(expected = NullPointerException.class)
    public void testNullIdentifier() {
        new Procedure(null);
    }

    @Test
    public void constructionTest() {
        var getSettings = new Procedure("getSettings");

        assertEquals(getSettings.getIdentifier(), "getSettings");
    }

    @Test
    public void equalsTest() {
        var firstGetSettings = new Procedure("getSettings");
        var secondGetSettings = new Procedure("getSettings");
        var updateSettings = new Procedure("updateSettings");
        var deleteSettings = new Procedure("deleteSettings");

        assertEquals(firstGetSettings, secondGetSettings);
        assertNotEquals(updateSettings, deleteSettings);
    }

    @Test
    public void hashCodeTest() {
        var firstGetSettings = new Procedure("getSettings");
        var secondGetSettings = new Procedure("getSettings");
        var updateSettings = new Procedure("updateSettings");
        var deleteSettings = new Procedure("deleteSettings");

        assertEquals(firstGetSettings.hashCode(), secondGetSettings.hashCode());
        assertNotEquals(updateSettings.hashCode(), deleteSettings.hashCode());
    }

    @Test
    public void testDefaultImplementation() {
        var getSettings = new Procedure("getSettings");

        assertNotNull(getSettings.apply(new String("Hello")));
    }
}
