package com.example.bomberman.collections;

import java.util.*;

public class ListGraph<K, V> implements IGraph<K, V> {
    private Map<K, Vertex<K, V>> adjacencyList;

    public ListGraph() {
        adjacencyList = new HashMap<>();
    }

    @Override
    public boolean addVertex(K key, V value) {
        if (!adjacencyList.containsKey(key)) {
            Vertex<K, V> vertex = new Vertex<>(key, value);
            adjacencyList.put(key, vertex);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeVertex(K key) {
        if (adjacencyList.containsKey(key)) {
            for (K k : adjacencyList.keySet()) {
                adjacencyList.get(k).removeEdge(adjacencyList.get(key));
            }
            adjacencyList.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean addEdge(K source, K destination, int weight) {
        if (adjacencyList.containsKey(source) && adjacencyList.containsKey(destination)) {
            adjacencyList.get(source).addEdge(adjacencyList.get(destination), weight);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEdge(K source, K destination) {
        if (adjacencyList.containsKey(source) && adjacencyList.containsKey(destination)) {
            adjacencyList.get(source).removeEdge(adjacencyList.get(destination));
            return true;
        }
        return false;
    }

    @Override
    public boolean hasEdge(K source, K destination) {
        if (adjacencyList.containsKey(source)) {
            return adjacencyList.get(source).hasEdge(adjacencyList.get(destination));
        }
        return false;
    }

    public Queue<Vertex<K, V>> bfs(K key){
        for(K k: adjacencyList.keySet()){
            Vertex<K, V> u = adjacencyList.get(k);
            u.setColor(Color.WHITE);
            u.setDistance(Integer.MAX_VALUE);
            u.setPredecessor(null);
        }

        Vertex<K, V> s = adjacencyList.get(key);
        if(s == null) return null;
        s.setColor(Color.GRAY);
        s.setDistance(0);
        s.setPredecessor(null);

        Queue<Vertex<K, V>> queue = new LinkedList<>();
        Queue<Vertex<K, V>> path = new LinkedList<>();
        queue.offer(s);
        path.offer(s);

        while (!queue.isEmpty()) {
            Vertex<K, V> u = queue.poll();
            for (Edge<K, V> edge : u.getEdges()) {
                Vertex<K, V> v = edge.getDestination();
                path.offer(u);
                if (v.getColor() == Color.WHITE) {
                    v.setColor(Color.GRAY);
                    v.setDistance(u.getDistance() + 1);
                    v.setPredecessor(u);
                    queue.offer(v);
                    path.offer(v);
                }
            }
            u.setColor(Color.BLACK);
        }
        return path;
    }

    public void dfs(K key){
        for(K k: adjacencyList.keySet()){
            Vertex<K, V> u = adjacencyList.get(k);
            u.setColor(Color.WHITE);
            u.setPredecessor(null);
        }

        int time = 0;

        for (K k : adjacencyList.keySet()) {
            Vertex<K, V> u = adjacencyList.get(k);
            if (u.getColor() == Color.WHITE) {
                dfsVisit(u, time);
            }
        }
    }

    private void dfsVisit(Vertex<K, V> u, int time) {
        time++;
        u.setDistance(time);
        u.setColor(Color.GRAY);
        for (Edge<K, V> edge : u.getEdges()) {
            Vertex<K, V> v = edge.getDestination();
            if (v.getColor() == Color.WHITE) {
                v.setPredecessor(u);
                dfsVisit(v, time);
            }
        }
        u.setColor(Color.BLACK);
        time++;
        u.setDistance(time);
    }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();
        for (K vertex : adjacencyList.keySet()) {
            List<Edge<K, V>> edges = adjacencyList.get(vertex).getEdges();
            sb.append(vertex).append(": ");
            for (Edge<K, V> edge : edges) {
                sb.append(edge.getDestination().getKey()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public ArrayList<V> getVertices() {
        ArrayList<V> vertices = new ArrayList<>();
        int i = 0;
        for (K k : adjacencyList.keySet()) {
            vertices.add(adjacencyList.get(k).getValue());
        }
        return vertices;
    }

    @Override
    public Vertex<K, V> getVertex(K key) {
        return adjacencyList.get(key);
    }
}