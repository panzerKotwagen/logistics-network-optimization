package ru.kotb.lno.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kotb.lno.graph.algorithms.DijkstraShortestPath;
import ru.kotb.lno.graph.algorithms.OptimalPathSolver;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.Collections;
import java.util.List;


public class OptimalPathTest {

    private final Graph graph = new JGraphT();

    private final OptimalPathSolver solver = new OptimalPathSolver();

    private final DijkstraShortestPath optimalPath = new DijkstraShortestPath();

    @Test
    void findOptimalPath() {
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

        solver.init(graph, "S", "T", 4, 0);
        OptimalPathSolver.Result res = solver.solve();
        System.out.println(res);
        List<String> optPath = solver.restoreOptimalPath();
        System.out.println(optPath);
        List<String> expectedOptimalPath = List.of("S", "12", "22", "32", "T");
//        Assertions.assertThat(optPath.equals(expectedOptimalPath)).isTrue();

        solver.init(graph, "S", "T", 9, 0);
        OptimalPathSolver.Result res1 = solver.solve();
        System.out.println(res1);
        List<String> optPath1 = solver.restoreOptimalPath();
        System.out.println(optPath1);
        List<String> expectedOptimalPath1 = List.of("S", "11", "22", "31", "T");
//        Assertions.assertThat(optPath1.equals(expectedOptimalPath1)).isTrue();
    }
}
