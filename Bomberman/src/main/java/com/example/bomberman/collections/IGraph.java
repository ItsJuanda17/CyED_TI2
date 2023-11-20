package com.example.bomberman.collections;

import java.util.ArrayList;
import java.util.Queue;

public interface IGraph<K, V> {
    boolean addVertex(K key, V value);
    boolean removeVertex(K key);
    boolean addEdge(K source, K destination, int weight);
    boolean removeEdge(K source, K destination);
    boolean hasEdge(K source, K destination);
    Queue<Vertex<K,V>> bfs(K key);
    void dfs(K key);
    String print();
    ArrayList<V> getVertices();
    Vertex<K, V> getVertex(K key);

}