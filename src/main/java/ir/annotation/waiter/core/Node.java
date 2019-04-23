package ir.annotation.waiter.core;

import ir.annotation.waiter.core.common.Identity;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * A model of a node in a nodes network.
 * <p>
 * Nodes network is a local network of related nodes, like a graph.
 * Nodes can contain other nodes as child nodes, and each node is responsible for 0 to N procedures.
 * The implementation of nodes network guarantees that all paths are unique.
 * Procedures are {@link java.util.function.Function}s in java, so can be called to invoke actions.
 * Another important note about this class is that all child nodes and all procedures can be active or inactive.
 * </p>
 *
 * @author Alireza Pourtaghi
 */
public class Node extends Identity {
    /**
     * The parent node of current one.
     */
    private Optional<Node> parent;

    /**
     * Child nodes of this node. Child nodes can not have same identifier as current one.
     */
    private final ConcurrentHashMap<Node, Boolean> nodes;

    /**
     * Procedures that can be invoked on this node.
     */
    private final ConcurrentHashMap<Procedure, Boolean> procedures;

    /**
     * Constructor to create an instance of this node as a root node.
     *
     * @param identifier The unique identifier of this node.
     * @throws NullPointerException If provided identifier is {@code null}.
     */
    public Node(String identifier) {
        super(identifier);

        this.parent = Optional.empty();
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
        addNode(node, true);
    }

    /**
     * Tries to add provided node as a child node for this node.
     * <p>
     * It is important to know that if a node with the same identifier as provided node already exists, the provided node will not be inserted.
     * Because of this reason you can use get and remove methods to check whether an identifier exists or not, or even remove it first, before inserting new one.
     * </p>
     *
     * @param node   New node that should be added as child node for this node.
     * @param active Whether new node should be active or not.
     * @throws NullPointerException     If provided node is {@code null}.
     * @throws IllegalArgumentException If provided node's identifier is same as this node's identifier.
     */
    public final void addNode(Node node, boolean active) throws IllegalArgumentException {
        requireNonNull(node);

        node.setParent(Optional.of(this));

        var parent = node.getParent();
        while (parent.isPresent())
            if (parent.get().getIdentifier().equals(node.getIdentifier()))
                throw new IllegalArgumentException("violates path uniqueness on nodes network");
            else
                parent = parent.get().getParent();

        nodes.putIfAbsent(node, active);
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
     * Returns back a child node of current one if exists.
     *
     * @param nodeIdentifier The identifier of child node to find.
     * @param active         Whether it should be active or not. Acts like search filter.
     * @return An optional value that may or may not contains the desired node.
     */
    public final Optional<Node> getNode(String nodeIdentifier, boolean active) {
        return nodes.entrySet()
                .stream()
                .filter(a -> a.getKey().getIdentifier().equals(nodeIdentifier) && a.getValue().equals(active))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    /**
     * Tries to add provided procedure as an active procedure for this node.
     * <p>
     * It is important to know that if a procedure with the same identifier as provided procedure already exists, the provided procedure will not be inserted.
     * Because of this reason you can use get and remove methods to check whether an identifier exists or not, or even remove it first, before inserting new one.
     * </p>
     *
     * @param procedure New node that should be added as child node for this node.
     * @throws NullPointerException If procedure is {@code null}.
     */
    public final void addProcedure(Procedure procedure) throws IllegalArgumentException {
        addProcedure(procedure, true);
    }

    /**
     * Tries to add provided procedure as a procedure for this node.
     *
     * @param procedure New node that should be added as child node for this node.
     * @param active    Whether new procedure should be active or not.
     * @throws NullPointerException If procedure is {@code null}.
     */
    public final void addProcedure(Procedure procedure, boolean active) throws IllegalArgumentException {
        requireNonNull(procedure);

        procedures.putIfAbsent(procedure, active);
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

    /**
     * Returns back a procedure of current node if exists.
     *
     * @param procedureIdentifier The identifier of procedure to find.
     * @param active              Whether procedure should be active or not. Acts like search filter.
     * @return An optional value that may or may not contains the desired procedure.
     */
    public final Optional<Procedure> getProcedure(String procedureIdentifier, boolean active) {
        return procedures.entrySet()
                .stream()
                .filter(a -> a.getKey().getIdentifier().equals(procedureIdentifier) && a.getValue().equals(active))
                .findFirst()
                .map(Map.Entry::getKey);
    }

    private Optional<Node> getParent() {
        return parent;
    }

    private void setParent(Optional<Node> parent) {
        this.parent = parent;
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
