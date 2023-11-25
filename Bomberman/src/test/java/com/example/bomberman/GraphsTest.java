package com.example.bomberman;

import com.example.bomberman.collections.Color;
import com.example.bomberman.collections.IGraph;
import com.example.bomberman.collections.ListGraph;
import com.example.bomberman.collections.MatrixGraph;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphsTest {

    private IGraph<Integer, String> graph;


    // The Graph is Empty
    public void setup1(){
        graph = new ListGraph<>(); // Change this to MatrixGraph to test the other implementation
    }

    // The Graph is not Empty
    public void setup2(){
        graph = new ListGraph<>(); // Change this to MatrixGraph to test the other implementation
        graph.addVertex(1, "A");
        graph.addVertex(2, "B");
        graph.addEdge(1, 2, 5);
    }

    @Test
    public void testAddVertexEmptyGraph() {
        setup1();
        assertTrue(graph.addVertex(1, "A"));
        assertEquals("A", graph.getVertex(1).getValue());
    }

    @Test
    public void testAddVertexNonEmptyGraph() {
        setup2();
        assertFalse(graph.addVertex(1, "C")); // Vertex with key 1 already exists
        assertTrue(graph.addVertex(3, "C"));
        assertEquals("C", graph.getVertex(3).getValue());
    }

    @Test
    public void testRemoveVertexEmptyGraph() {
        setup1();
        assertFalse(graph.removeVertex(1)); // Vertex with key 1 does not exist
    }

    @Test
    public void testRemoveVertexNonEmptyGraph() {
        setup2();
        assertTrue(graph.removeVertex(1));
        assertNull(graph.getVertex(1));
        assertFalse(graph.hasEdge(1, 2)); // Edge between 1 and 2 should also be removed
    }

    @Test
    public void testAddEdgeEmptyGraph() {
        setup1();
        assertFalse(graph.addEdge(1, 2, 5)); // Vertices with keys 1 and 2 do not exist
    }

    @Test
    public void testAddEdgeNonEmptyGraph() {
        setup2();
        assertFalse(graph.addEdge(1, 3, 3)); // Vertex with key 3 does not exist
        assertTrue(graph.addEdge(2, 1, 4));
        assertTrue(graph.hasEdge(2, 1));
        assertEquals(4, graph.getVertex(2).getEdges().get(0).getWeight());
    }

    @Test
    public void testRemoveEdgeEmptyGraph() {
        setup1();
        assertFalse(graph.removeEdge(1, 2)); // Vertices with keys 1 and 2 do not exist

    }

    @Test
    public void testRemoveEdgeNonEmptyGraph() {
        setup2();
        assertFalse(graph.removeEdge(1, 3)); // Vertex with key 3 does not exist
        assertTrue(graph.removeEdge(1, 2));
        assertFalse(graph.hasEdge(1, 2));
    }

    @Test
    public void testHasEdgeNonEmptyGraph() {
        setup2();
        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(2, 1)); // Edge direction is from 1 to 2
    }

    @Test
    public void testHasEdgeEmptyGraph() {
        setup1();
        assertFalse(graph.hasEdge(1, 2)); // Vertices with keys 1 and 2 do not exist
    }

    @Test
    public void testBFSEmptyGraph() {
        setup1();
        assertNull(graph.bfs(1)); // Vertex with key 1 does not exist
    }

    @Test
    public void testBFSNonEmptyGraph() {
        setup2();
        assertEquals(3, graph.bfs(1).size());
        assertEquals("A", graph.bfs(1).peek().getValue());
    }

    @Test
    public void testDFSEmptyGraph() {
        setup1();
        graph.dfs(1); // No exception should be thrown, as vertex with key 1 does not exist
        assertTrue(true);
    }

    @Test
    public void testDFSNonEmptyGraph() {
        setup2();
        graph.dfs(1); // No exception should be thrown
        assertEquals(graph.getVertex(1).getColor(), Color.BLACK);
        assertEquals(graph.getVertex(2).getColor(), Color.BLACK);
    }

    @Test
    public void testGetVerticesEmptyGraph() {
        setup1();
        assertEquals(0, graph.getVertices().size());
    }

    @Test
    public void testGetVerticesNonEmptyGraph() {
        // Test on a non-empty graph
        setup2();
        assertEquals(2, graph.getVertices().size());
        assertTrue(graph.getVertices().contains("A"));
        assertTrue(graph.getVertices().contains("B"));
    }

    @Test
    public void testGetVertex() {
        setup1();
        assertNull(graph.getVertex(1)); // Vertex with key 1 does not exist
    }

    @Test
    public void testGetVertexNonEmptyGraph() {
        setup2();
        assertNotNull(graph.getVertex(1));
        assertEquals("A", graph.getVertex(1).getValue());
    }
}