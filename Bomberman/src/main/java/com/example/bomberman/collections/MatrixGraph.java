package com.example.bomberman.collections;

import java.util.*;

public class MatrixGraph<K, V> implements IGraph<K, V>{
    
    @Override
    public boolean addVertex(K key, V value) {
        return false;
    }

    @Override
    public boolean removeVertex(K key) {
        return false;
    }

    @Override
    public boolean addEdge(K source, K destination, int weight) {
        return false;
    }

    @Override
    public boolean removeEdge(K source, K destination) {
        return false;
    }

    @Override
    public boolean hasEdge(K source, K destination) {
        return false;
    }

    @Override
    public void bfs(K key) {

    }

    @Override
    public void dfs(K key) {

    }

    @Override
    public String print() {
        return null;
    }
}
