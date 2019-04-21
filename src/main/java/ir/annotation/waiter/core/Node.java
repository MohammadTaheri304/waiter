package ir.annotation.waiter.core;

import ir.annotation.waiter.core.common.Identity;

import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * An abstract model of a node in a nodes network.
 * <p>
 * Nodes network is a local network of related nodes. Like a graph. Each node on this network have a unique identifier.
 * Nodes can contain other nodes as child nodes, and each node is responsible for 0 to N procedures.
 * Procedures are like {@link java.util.function.Function}s in Java, so can be called to invoke actions.
 * Another important note about this class is that all child nodes and all procedures can be active or inactive.
 * </p>
 *
 * @author Alireza Pourtaghi
 */
public class Node extends Identity {
    /**
     * Child nodes of this node. Child nodes can not have same identifier as current one.
     */
    private final ConcurrentHashMap<Node, Boolean> nodes;

    /**
     * Procedures that can be invoked on this node.
     */
    private final ConcurrentHashMap<Procedure, Boolean> procedures;

    /**
     * Constructor to create an instance of this node.
     *
     * @param identifier The unique identifier of this node.
     * @throws NullPointerException If provided identifier is {@code null}.
     */
    public Node(String identifier) {
        super(identifier);

        this.nodes = new ConcurrentHashMap<>();
        this.procedures = new ConcurrentHashMap<>();
    }

    /**
     * Tries to add provided node as an active child node for this node.
     *
     * @param node New node that should be added as child node for this node.
     * @throws NullPointerException     If provided node is {@code null}.
     * @throws IllegalArgumentException If provided node's identifier is same as this node's identifier.
     */
    public final void addNode(Node node) throws IllegalArgumentException {
        requireNonNull(node);

        if (getIdentifier().equals(node.getIdentifier()))
            throw new IllegalArgumentException("same identifier as current node");

        nodes.putIfAbsent(node, true);
    }

    /**
     * Tries to remove provided node from child nodes.
     *
     * @param node Node that should be deleted from child nodes of current node.
     * @throws NullPointerException If provided node is {@code null}.
     */
    public final void removeNode(Node node) {
        requireNonNull(node);

        nodes.remove(node);
    }

    /**
     * Tries to remove a child node that has same identifier as provide identifier from child nodes.
     *
     * @param nodeIdentifier Identifier of child node that should be deleted.
     * @throws NullPointerException If nodeIdentifier is {@code null}.
     */
    public final void removeNode(String nodeIdentifier) {
        requireNonNull(nodeIdentifier);

        removeNode(new Node(nodeIdentifier));
    }

    /**
     * Tries to add provided procedure as an active procedure for this node.
     *
     * @param procedure New node that should be added as child node for this node.
     * @throws NullPointerException If procedure is {@code null}.
     */
    public final void addProcedure(Procedure procedure) throws IllegalArgumentException {
        requireNonNull(procedure);

        procedures.putIfAbsent(procedure, true);
    }

    /**
     * Tries to remove provided procedure from procedures.
     *
     * @param procedure Procedure that should be deleted from procedures of current node.
     * @throws NullPointerException If provided procedure is {@code null}.
     */
    public final void removeProcedure(Procedure procedure) {
        requireNonNull(procedure);

        procedures.remove(procedure);
    }

    /**
     * Tries to remove a procedure that has same identifier as provide identifier from procedures.
     *
     * @param procedureIdentifier Identifier of procedure that should be deleted.
     * @throws NullPointerException If procedureIdentifier is {@code null}.
     */
    public final void removeProcedure(String procedureIdentifier) {
        requireNonNull(procedureIdentifier);

        removeProcedure(new Procedure(procedureIdentifier));
    }

    @Override
    public final boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }
}
