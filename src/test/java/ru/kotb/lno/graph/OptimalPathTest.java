package ru.kotb.lno.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kotb.lno.graph.algorithms.DijkstraShortestPath;
import ru.kotb.lno.graph.algorithms.OptimalPathSolver;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.List;


public class OptimalPathTest {

    private final Graph graph = new JGraphT();

    private final OptimalPathSolver solver = new OptimalPathSolver();

    private final DijkstraShortestPath optimalPath = new DijkstraShortestPath();

    @Test
    void findOptimalPath() {
        String[] nodes = {"S", "11", "12", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }

        graph.addEdge("S", "11", "", 5, 7);
        graph.addEdge("S", "12", "", 6, 3);

        graph.addEdge("11", "T", "", 5, 10);
        graph.addEdge("12", "T", "", 9, 13);

        DijkstraShortestPath.Result w1Result = optimalPath.findShortestPath(graph, "S", 0);
        solver.init(graph, optimalPath.restoreOptimalPath(w1Result.getPreviousNodeList(), "T"));

        List<String> optPath = solver.solve();

        List<String> expectedOptimalPath = List.of("S", "11", "T");

        Assertions.assertThat(optPath.equals(expectedOptimalPath)).isTrue();
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
        solver.init(graph, optimalPath.restoreOptimalPath(w1Result.getPreviousNodeList(), "T"));
        List<String> optPath = solver.solve();

        List<String> expectedOptimalPath = List.of("S", "12", "22", "32", "T");

        Assertions.assertThat(optPath.equals(expectedOptimalPath)).isTrue();
    }
}
