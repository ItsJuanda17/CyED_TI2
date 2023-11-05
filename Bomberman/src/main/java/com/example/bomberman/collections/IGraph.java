package com.example.bomberman.collections;

public interface IGraph<K, V> {
    boolean addVertex(K key, V value);
    boolean removeVertex(K key);
    boolean addEdge(K source, K destination, int weight);
    boolean removeEdge(K source, K destination);
    boolean hasEdge(K source, K destination);
    void bfs(K key);
    void dfs(K key);
    String print();

}