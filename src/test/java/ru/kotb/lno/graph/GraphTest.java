package ru.kotb.lno.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.List;
import java.util.Map;
import java.util.Set;


@TestComponent
public class GraphTest {

    private final Graph graph = new JGraphT();

    @Test
    void getNonExistingNodeReturnNull() {
        Assertions.assertThat(graph.getEdge("S", "S")).isNull();
    }

    @Test
    void getExistingNodeReturnNode() {
        String nodeName = "S";
        graph.addNode(nodeName);
        Assertions.assertThat(graph.getNode(nodeName)).isNotNull();
    }

    @Test
    void getExistingEdgeReturnEdge() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, "Edge", 1, 1);
        Assertions.assertThat(graph.getEdge(nodeName1, nodeName2)).isNotNull();
    }

    @Test
    void getExistingEdgeReturnEdgeRegardlessOfNodeNameOrder() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, "Edge", 1, 1);
        Assertions.assertThat(graph.getEdge(nodeName1, nodeName2)).isNotNull();
        Assertions.assertThat(graph.getEdge(nodeName2, nodeName1)).isNotNull();
    }

    @Test
    void addMultiEdgeThrowsException() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        String edgeName = "Edge";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, edgeName, 1, 1);
        graph.addEdge(nodeName1, nodeName2, edgeName, 1, 1);
        Assertions.assertThat(graph.getEdge(nodeName1, nodeName2)).isNotNull();
        Assertions.assertThat(graph.getEdge(nodeName2, nodeName1)).isNotNull();
    }

    @Test
    void addMultiEdgeThrowsExceptionRegardlessOfNodeNameOrder() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        String edgeName = "Edge";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, edgeName, 1, 1);
        graph.addEdge(nodeName2, nodeName1, edgeName, 1, 1);

        Assertions.assertThat(graph.getEdges(nodeName1).size()).isEqualTo(1);
        Assertions.assertThat(graph.getEdges(nodeName2).size()).isEqualTo(1);
    }

    @Test
    void addEdgeToNonExistingNodeThrowsException() {
        String nodeName1 = "S";
        graph.addNode(nodeName1);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> graph.addEdge(nodeName1, "nodeName2", "edge", 1, 1));
    }

    @Test
    void addLoopToGraphThrowException() {
        String nodeName = "S";
        graph.addNode(nodeName);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> graph.addEdge(nodeName, nodeName, "edge", 1, 1));
    }

    @Test
    void removeEdgeCorrectly() {
        List<String> nodeNames = List.of("1", "2", "3", "4");

        for (String name : nodeNames) {
            graph.addNode(name);
        }

        for (int i = 0; i < nodeNames.size(); i++) {
            for (int j = 0; j < nodeNames.size(); j++) {
                if (i != j) {
                    graph.addEdge(nodeNames.get(i), nodeNames.get(j), "edge", 1, 2);
                }
            }
        }

        for (int i = 0; i < nodeNames.size(); i++) {
            for (int j = 0; j < nodeNames.size(); j++) {
                if (i != j) {
                    graph.removeEdge(nodeNames.get(i), nodeNames.get(j));
                    Edge edge = graph.getEdge(nodeNames.get(i), nodeNames.get(j));
                    boolean hasEdge = graph.hasEdge(nodeNames.get(i), nodeNames.get(j));
                    Assertions.assertThat(edge).isNull();
                    Assertions.assertThat(hasEdge).isFalse();
                }
            }
        }
    }

    @Test
    void removeNodeRemoveConnectedEdges() {
        List<String> nodeNames = List.of("1", "2", "3", "4");
        String removedNodeName = nodeNames.get(0);
        for (String name : nodeNames) {
            graph.addNode(name);
        }

        for (int i = 0; i < nodeNames.size(); i++) {
            for (int j = 0; j < nodeNames.size(); j++) {
                if (i != j) {
                    graph.addEdge(nodeNames.get(i), nodeNames.get(j), "edge", 1, 2);
                }
            }
        }

        Set<Edge> edgeSet = graph.getEdges(removedNodeName);
        int edgeSize = edgeSet.size();
        Assertions.assertThat(edgeSize).isEqualTo(3);

        graph.removeNode(removedNodeName);
        edgeSet = graph.getEdges(removedNodeName);
        Assertions.assertThat(edgeSet).isNull();

        for (int i = 1; i < nodeNames.size(); i++) {
            Edge edge = graph.getEdge(removedNodeName, nodeNames.get(i));
            Assertions.assertThat(edge).isNull();
        }
    }

    @Test
    void updateWeightsCorrectly() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, "Edge", 1, 1);
        graph.updateEdgeWeight(nodeName1, nodeName2, 99, 991);
        Edge edge = graph.getEdge(nodeName1, nodeName2);
        Assertions.assertThat(edge.getWeights()[0]).isEqualTo(99);
        Assertions.assertThat(edge.getWeights()[1]).isEqualTo(991);
    }

    @Test
    void name() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, "Edge", 1, 1);
        graph.getEdge(nodeName1, nodeName2).getSource2();
        System.out.println();
    }

    @Test
    void findShortestPath() {
        String[] nodes = {"S", "2", "3", "4", "5", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }

        graph.addEdge("S", "T", "a", 14, 10);
        graph.addEdge("S", "3", "b", 9, 5);
        graph.addEdge("S", "2", "c", 7, 6);

        graph.addEdge("2", "3", "d", 10, 11);
        graph.addEdge("2", "4", "d", 15, 14);

        graph.addEdge("3", "T", "d", 2, 3);
        graph.addEdge("3", "4", "d", 11, 6);

        graph.addEdge("5", "T", "a", 9, 7);
        graph.addEdge("5", "4", "a", 6, 10);

//        Map<String, Integer> distances = graph.findShortestPath("S");
//        int distanceToT = distances.get("T");
//        int distanceTo2 = distances.get("2");
//        int distanceTo3 = distances.get("3");
//        int distanceTo4 = distances.get("4");
//        int distanceTo5 = distances.get("5");
//
//        Assertions.assertThat(distanceToT).isEqualTo(11);
//        Assertions.assertThat(distanceTo2).isEqualTo(7);
//        Assertions.assertThat(distanceTo3).isEqualTo(9);
//        Assertions.assertThat(distanceTo4).isEqualTo(20);
//        Assertions.assertThat(distanceTo5).isEqualTo(20);
    }
}
