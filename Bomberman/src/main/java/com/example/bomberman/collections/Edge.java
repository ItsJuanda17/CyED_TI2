package com.example.bomberman.collections;

public class Edge<K, V> {
    private Vertex<K, V> source;
    private Vertex<K, V> destination;
    private double weight;

    public Edge(Vertex<K, V> source, Vertex<K, V> destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Vertex<K, V> getSource() {
        return source;
    }

    public Vertex<K, V> getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " -> " + destination + " (" + weight + ")";
    }


}
