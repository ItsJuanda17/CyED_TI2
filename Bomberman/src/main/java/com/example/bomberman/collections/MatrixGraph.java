package com.example.bomberman.collections;

import java.util.*;

public class MatrixGraph<K, V> implements IGraph<K, V> {
    private final Map<K, Vertex<K, V>> vertexMap;
    private boolean[][] adjacencyMatrix;

    public MatrixGraph() {
        this.vertexMap = new HashMap<>();
        this.adjacencyMatrix = new boolean[0][0];
    }

    @Override
    public boolean addVertex(K key, V value) {
        if (vertexMap.containsKey(key)) {
            return false;
        }
        Vertex<K, V> newVertex = new Vertex<>(key, value);
        vertexMap.put(key, newVertex);


        if (adjacencyMatrix.length <= vertexMap.size()) {
            expandAdjacencyMatrix();
        }

        return true;
    }

    @Override
    public boolean removeVertex(K key) {
        Vertex<K, V> vertexToRemove = vertexMap.get(key);
        if (vertexToRemove == null) {
            return false;
        }
        vertexMap.remove(key);


        int index = getVertexIndex(vertexToRemove);
        if (index >= 0) {
            removeVertexFromMatrix(index);
        }

        return true;
    }

    @Override
    public boolean addEdge(K source, K destination, int weight) {
        Vertex<K, V> sourceVertex = vertexMap.get(source);
        Vertex<K, V> destinationVertex = vertexMap.get(destination);
        if (sourceVertex == null || destinationVertex == null) {
            return false;
        }

        sourceVertex.addEdge(destinationVertex, weight);
        int sourceIndex = getVertexIndex(sourceVertex);
        int destinationIndex = getVertexIndex(destinationVertex);

        adjacencyMatrix[sourceIndex][destinationIndex] = true;
        return true;
    }

    @Override
    public boolean removeEdge(K source, K destination) {
        Vertex<K, V> sourceVertex = vertexMap.get(source);
        Vertex<K, V> destinationVertex = vertexMap.get(destination);
        if (sourceVertex == null || destinationVertex == null) {
            return false;
        }

        sourceVertex.removeEdge(destinationVertex);
        int sourceIndex = getVertexIndex(sourceVertex);
        int destinationIndex = getVertexIndex(destinationVertex);

        adjacencyMatrix[sourceIndex][destinationIndex] = false;
        return true;
    }

    @Override
    public boolean hasEdge(K source, K destination) {
        Vertex<K, V> sourceVertex = vertexMap.get(source);
        Vertex<K, V> destinationVertex = vertexMap.get(destination);
        if (sourceVertex == null || destinationVertex == null) {
            return false;
        }

        int sourceIndex = getVertexIndex(sourceVertex);
        int destinationIndex = getVertexIndex(destinationVertex);

        return adjacencyMatrix[sourceIndex][destinationIndex];
    }

    @Override
    public void bfs(K key) {

        for (Vertex<K, V> vertex : vertexMap.values()) {
            vertex.setColor(Color.WHITE);
        }

        Vertex<K, V> startVertex = vertexMap.get(key);
        if (startVertex == null) {
            return;
        }

        Queue<Vertex<K, V>> queue = new LinkedList<>();
        startVertex.setColor(Color.GRAY);
        queue.offer(startVertex);

        while (!queue.isEmpty()) {
            Vertex<K, V> currentVertex = queue.poll();
            System.out.print(currentVertex + " ");

            for (Edge<K, V> edge : currentVertex.getEdges()) {
                Vertex<K, V> neighbor = edge.getDestination();
                if (neighbor.getColor() == Color.WHITE) {
                    neighbor.setColor(Color.GRAY);
                    queue.offer(neighbor);
                }
            }

            currentVertex.setColor(Color.BLACK);
        }
    }

    @Override
    public void dfs(K key) {

        for (Vertex<K, V> vertex : vertexMap.values()) {
            vertex.setColor(Color.WHITE);
        }

        Vertex<K, V> startVertex = vertexMap.get(key);
        if (startVertex == null) {
            return;
        }

        Stack<Vertex<K, V>> stack = new Stack<>();
        startVertex.setColor(Color.GRAY);
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            Vertex<K, V> currentVertex = stack.pop();
            System.out.print(currentVertex + " ");

            for (Edge<K, V> edge : currentVertex.getEdges()) {
                Vertex<K, V> neighbor = edge.getDestination();
                if (neighbor.getColor() == Color.WHITE) {
                    neighbor.setColor(Color.GRAY);
                    stack.push(neighbor);
                }
            }

            currentVertex.setColor(Color.BLACK);
        }
    }

    @Override
    public String print() {
        StringBuilder sb = new StringBuilder();
        for (Vertex<K, V> vertex : vertexMap.values()) {
            sb.append(vertex.getKey()).append(" -> ");
            for (Edge<K, V> edge : vertex.getEdges()) {
                sb.append(edge.getDestination().getKey()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private int getVertexIndex(Vertex<K, V> vertex) {
        int index = 0;
        for (K k : vertexMap.keySet()) {
            if (vertexMap.get(k).equals(vertex)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private void expandAdjacencyMatrix() {
        int newSize = adjacencyMatrix.length + 1;
        boolean[][] newMatrix = new boolean[newSize][newSize];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, adjacencyMatrix[i].length);
        }
        adjacencyMatrix = newMatrix;
    }

    private void removeVertexFromMatrix(int index) {
        int newSize = adjacencyMatrix.length - 1;
        boolean[][] newMatrix = new boolean[newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            int row = (i < index) ? i : i + 1;
            for (int j = 0; j < newSize; j++) {
                int col = (j < index) ? j : j + 1;
                newMatrix[i][j] = adjacencyMatrix[row][col];
            }
        }
        adjacencyMatrix = newMatrix;
    }
}
