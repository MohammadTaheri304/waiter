package ir.annotation.waiter.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    @Test(expected = NullPointerException.class)
    public void testNullIdentifier() {
        new Node(null);
    }

    @Test
    public void constructionTest() {
        var waiter = new Node("waiter");

        assertEquals(waiter.getIdentifier(), "waiter");
    }

    @Test
    public void equalsTest() {
        var firstWaiter = new Node("waiter");
        var secondWaiter = new Node("waiter");
        var thirdWaiter = new Node("third waiter");
        var fourthWaiter = new Node("fourth waiter");

        assertEquals(firstWaiter, secondWaiter);
        assertNotEquals(thirdWaiter, fourthWaiter);
    }

    @Test
    public void hashCodeTest() {
        var firstWaiter = new Node("waiter");
        var secondWaiter = new Node("waiter");
        var thirdWaiter = new Node("third waiter");
        var fourthWaiter = new Node("fourth waiter");

        assertEquals(firstWaiter.hashCode(), secondWaiter.hashCode());
        assertNotEquals(thirdWaiter.hashCode(), fourthWaiter.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void addNullNodeTest() {
        var waiter = new Node("waiter");
        waiter.addNode(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSameIdentifierAsChildNodeTest() {
        var waiter = new Node("waiter");
        waiter.addNode(new Node("waiter"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNodeWithSameIdentifierAsAnotherNodeTest() {
        var a = new Node("a");
        var b = new Node("b");
        var c = new Node("c");
        var d = new Node("d");
        var againA = new Node("a");
        a.addNode(b);
        a.addNode(c);
        a.addNode(d);

        c.addNode(againA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNodeWithSameIdentifierAsAnotherNodeSecondTest() {
        var a = new Node("a");
        var b = new Node("b");
        var c = new Node("c");
        var d = new Node("d");
        var againB = new Node("b");
        a.addNode(b);
        a.addNode(c);
        a.addNode(d);

        c.addNode(againB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNodeWithSameIdentifierAsAnotherNodeThirdTest() {
        var a = new Node("a");
        var b = new Node("b");
        var c = new Node("c");
        var d = new Node("d");
        var e = new Node("e");
        var f = new Node("f");
        var g = new Node("g");
        var againA = new Node("a");
        a.addNode(b);
        a.addNode(c);
        a.addNode(d);

        c.addNode(e);
        c.addNode(f);
        c.addNode(g);

        f.addNode(againA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNodeWithSameIdentifierAsAnotherNodeFourthTest() {
        var a = new Node("a");
        var b = new Node("b");
        var c = new Node("c");
        var d = new Node("d");
        var e = new Node("e");
        var f = new Node("f");
        var g = new Node("g");
        var againC = new Node("c");
        a.addNode(b);
        a.addNode(c);
        a.addNode(d);

        c.addNode(e);
        c.addNode(f);
        c.addNode(g);

        f.addNode(againC);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addNodeWithSameIdentifierAsAnotherNodeFifthTest() {
        var a = new Node("a");
        var b = new Node("b");
        var c = new Node("c");
        var d = new Node("d");
        var e = new Node("e");
        var f = new Node("f");
        var g = new Node("g");
        var againD = new Node("d");
        a.addNode(b);
        a.addNode(c);
        a.addNode(d);

        c.addNode(e);
        c.addNode(f);
        c.addNode(g);

        f.addNode(againD);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addNodeWithSameIdentifierAsAnotherNodeSixthTest() {
        var a = new Node("a");
        var b = new Node("b");
        var c = new Node("c");
        var d = new Node("d");
        var e = new Node("e");
        var f = new Node("f");
        var g = new Node("g");
        var againG = new Node("g");
        a.addNode(b);
        a.addNode(c);
        a.addNode(d);

        c.addNode(e);
        c.addNode(f);
        c.addNode(g);

        f.addNode(againG);
    }

    @Test
    public void addNodeTest() {
        var waiter = new Node("waiter");
        var v2 = new Node("v2");
        waiter.addNode(v2);

        var optionalV2 = waiter.getNode(v2.getIdentifier(), true);
        assertTrue(optionalV2.isPresent());
    }

    @Test
    public void addInactiveNodeTest() {
        var waiter = new Node("waiter");
        var v2 = new Node("v2");
        waiter.addNode(v2, false);

        var optionalV2 = waiter.getNode(v2.getIdentifier(), false);
        assertTrue(optionalV2.isPresent());
    }

    @Test
    public void removeNodeTest() {
        var waiter = new Node("waiter");
        var v2 = new Node("v2");
        waiter.addNode(v2);
        waiter.removeNode(v2);

        var optionalV2 = waiter.getNode(v2.getIdentifier(), true);
        assertTrue(optionalV2.isEmpty());
    }

    @Test
    public void removeNodeByIdentifierTest() {
        var waiter = new Node("waiter");
        var v2 = new Node("v2");
        waiter.addNode(v2);
        waiter.removeNode(v2.getIdentifier());

        var optionalV2 = waiter.getNode(v2.getIdentifier(), true);
        assertTrue(optionalV2.isEmpty());
    }

    @Test
    public void getNodeNotExistsTest() {
        var waiter = new Node("waiter");
        var v2 = new Node("v2");
        waiter.addNode(v2);

        assertTrue(waiter.getNode("not exists", true).isEmpty());
    }

    @Test
    public void getNodeFilterTest() {
        var waiter = new Node("waiter");
        var v2 = new Node("v2");
        waiter.addNode(v2);

        assertTrue(waiter.getNode("v2", false).isEmpty());
    }

    @Test
    public void getNodeTest() {
        var waiter = new Node("waiter");
        var v2 = new Node("v2");
        waiter.addNode(v2);

        assertTrue(waiter.getNode("v2", true).isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void addNullProcedureTest() {
        var waiter = new Node("waiter");
        waiter.addProcedure(null);
    }

    @Test
    public void addProcedureTest() {
        var waiter = new Node("waiter");
        var getSettings = new Procedure("getSettings");
        waiter.addProcedure(getSettings);

        var optionalGetSettings = waiter.getProcedure(getSettings.getIdentifier(), true);
        assertTrue(optionalGetSettings.isPresent());
    }

    @Test
    public void addInactiveProcedureTest() {
        var waiter = new Node("waiter");
        var getSettings = new Procedure("getSettings");
        waiter.addProcedure(getSettings, false);

        var optionalGetSettings = waiter.getProcedure(getSettings.getIdentifier(), false);
        assertTrue(optionalGetSettings.isPresent());
    }

    @Test
    public void removeProcedureTest() {
        var waiter = new Node("waiter");
        var getSettings = new Procedure("getSettings");
        waiter.addProcedure(getSettings);
        waiter.removeProcedure(getSettings);

        var optionalGetSettings = waiter.getProcedure(getSettings.getIdentifier(), true);
        assertTrue(optionalGetSettings.isEmpty());
    }

    @Test
    public void removeProcedureByIdentifierTest() {
        var waiter = new Node("waiter");
        var getSettings = new Procedure("getSettings");
        waiter.addProcedure(getSettings);
        waiter.removeProcedure(getSettings.getIdentifier());

        var optionalV2 = waiter.getNode(getSettings.getIdentifier(), true);
        assertTrue(optionalV2.isEmpty());
    }

    @Test
    public void getProcedureNotExistsTest() {
        var waiter = new Node("waiter");
        var getSettings = new Procedure("getSettings");
        waiter.addProcedure(getSettings);

        assertTrue(waiter.getProcedure("not exists", true).isEmpty());
    }

    @Test
    public void getProcedureFilterTest() {
        var waiter = new Node("waiter");
        var getSettings = new Procedure("getSettings");
        waiter.addProcedure(getSettings);

        assertTrue(waiter.getProcedure("getSettings", false).isEmpty());
    }

    @Test
    public void getProcedureTest() {
        var waiter = new Node("waiter");
        var getSettings = new Procedure("getSettings");
        waiter.addProcedure(getSettings);

        assertTrue(waiter.getProcedure("getSettings", true).isPresent());
    }
}
