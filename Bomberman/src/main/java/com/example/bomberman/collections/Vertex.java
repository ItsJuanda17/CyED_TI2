package com.example.bomberman.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex<K, V> {
    private final K key;
    private final V value;
    private final List<Edge<K, V>> edges;
    private Color color;
    private int distance;
    private Vertex<K, V> predecessor;

    public Vertex(K key, V value) {
        this.key = key;
        this.value = value;
        this.edges = new ArrayList<>();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Vertex<K, V> getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex<K, V> predecessor) {
        this.predecessor = predecessor;
    }

    public List<Edge<K, V>> getEdges() {
        return edges;
    }

    public void addEdge(Vertex<K, V> destination, int weight) {
        edges.add(new Edge<>(this, destination, weight));
    }

    public void removeEdge(Vertex<K, V> kvVertex) {
        edges.removeIf(edge -> edge.getDestination().equals(kvVertex));
    }

    public boolean hasEdge(Vertex<K, V> kvVertex) {
        for (Edge<K, V> edge : edges) {
            if (edge.getDestination().equals(kvVertex)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex<?, ?> vertex = (Vertex<?, ?>) o;
        return Objects.equals(key, vertex.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }


}
