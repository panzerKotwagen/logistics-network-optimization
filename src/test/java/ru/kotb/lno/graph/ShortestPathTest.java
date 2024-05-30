package ru.kotb.lno.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kotb.lno.graph.algorithms.DijkstraShortestPath;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.List;
import java.util.Map;


public class ShortestPathTest {

    private final Graph graph = new JGraphT();

    private final DijkstraShortestPath optimalPath = new DijkstraShortestPath();

    @Test
    void findOptimalPath() {
        String[] nodes = {"S", "2", "3", "4", "5", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }

        graph.addEdge("S", "T", "", 14, 1);
        graph.addEdge("S", "2", "", 7, 1);
        graph.addEdge("S", "3", "", 9, 1);

        graph.addEdge("2", "3", "", 10, 1);
        graph.addEdge("2", "4", "", 15, 1);

        graph.addEdge("3", "T", "", 2, 1);
        graph.addEdge("3", "4", "", 11, 1);

        graph.addEdge("5", "T", "", 9, 1);
        graph.addEdge("5", "4", "", 6, 1);

        Map<String, Double> distances = optimalPath.findShortestPath(graph, "S", 0).getDistances();
        double distanceTo2 = distances.get("2");
        double distanceTo3 = distances.get("3");
        double distanceTo4 = distances.get("4");
        double distanceTo5 = distances.get("5");
        double distanceToT = distances.get("T");

        Assertions.assertThat(distanceTo2).isEqualTo(7);
        Assertions.assertThat(distanceTo3).isEqualTo(9);
        Assertions.assertThat(distanceTo4).isEqualTo(20);
        Assertions.assertThat(distanceTo5).isEqualTo(20);
        Assertions.assertThat(distanceToT).isEqualTo(11);
    }

    @Test
    void findOptimalPath1() {
        String[] nodes = {"S", "11", "12", "21", "22", "23", "31", "32", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }

        graph.addEdge("S", "11", "", 1, 1);
        graph.addEdge("S", "12", "", 1, 1);

        graph.addEdge("11", "21", "", 5, 4);
        graph.addEdge("11", "22", "", 3, 8);
        graph.addEdge("11", "23", "", 6, 3);

        graph.addEdge("12", "21", "", 8, 2);
        graph.addEdge("12", "22", "", 6, 4);
        graph.addEdge("12", "23", "", 5, 5);

        graph.addEdge("21", "31", "", 7, 3);

        graph.addEdge("22", "31", "", 5, 5);
        graph.addEdge("22", "32", "", 2, 7);

        graph.addEdge("23", "32", "", 9, 6);

        graph.addEdge("31", "T", "", 10, 5);
        graph.addEdge("32", "T", "", 5, 10);

        DijkstraShortestPath.Result w1Result = optimalPath.findShortestPath(graph, "S", 0);
        DijkstraShortestPath.Result w2Result = optimalPath.findShortestPath(graph, "S", 1);

        Map<String, Double> w1Distances = w1Result.getDistances();
        Map<String, Double> w2Distances = w2Result.getDistances();

        double w1DistanceToT = w1Distances.get("T");
        double w2DistanceToT = w2Distances.get("T");

        Assertions.assertThat(w1DistanceToT).isEqualTo(11);
        Assertions.assertThat(w2DistanceToT).isEqualTo(11);

        List<String> w1ActualPath = optimalPath.restoreOptimalPath(w1Result.getPreviousNodeList(), "T");
        List<String> w2ActualPath = optimalPath.restoreOptimalPath(w2Result.getPreviousNodeList(), "T");

        List<String> w1ExpectedShortestPath = List.of("S", "11", "22", "32", "T");
        List<String> w2ExpectedShortestPath = List.of("S", "12", "21", "31", "T");

        Assertions.assertThat(w1ActualPath.equals(w1ExpectedShortestPath)).isTrue();
        Assertions.assertThat(w2ActualPath.equals(w2ExpectedShortestPath)).isTrue();
    }
}
