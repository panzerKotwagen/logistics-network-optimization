package ru.kotb.lno.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kotb.lno.graph.algorithms.OptimalPathSolver;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.List;


public class OptimalPathTest {

    private final Graph graph = new JGraphT();

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


        double[] globalCompromiseArray = new double[]{0, 5, 8, 11, 15};
        double[] expectedWinArray = new double[]{26, 22, 19, 15, 11};

        OptimalPathSolver solver = new OptimalPathSolver(graph, "S", "T", 0, 0);

        for (int i = 0; i < globalCompromiseArray.length; i++) {
            solver.setGlobalCompromiseValue(globalCompromiseArray[i]);

            OptimalPathSolver.Result res = solver.solve();
            List<String> optPath = solver.restoreOptimalPath();

            System.out.println(res);
            System.out.println(optPath);

            Assertions.assertThat(res.getWin()).isEqualTo(expectedWinArray[i]);
        }
    }

    @Test
    void findOptimalPathFromAnyNode() {
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

        graph.addEdge("22", "31", "", 7, 8);
        graph.addEdge("22", "32", "", 6, 7);

        graph.addEdge("23", "32", "", 7, 1);

        graph.addEdge("31", "T", "", 10, 5);
        graph.addEdge("32", "T", "", 5, 10);


        double globalCompromise = 5;

        OptimalPathSolver solver = new OptimalPathSolver(graph, "S", "T", 0, 0);

        solver.setGlobalCompromiseValue(globalCompromise);

        solver.setStartState("12");

        OptimalPathSolver.Result res = solver.solve();
        List<String> optPath = solver.restoreOptimalPath();

        System.out.println(res);
        System.out.println(optPath);

        Assertions.assertThat(res.getWin()).isEqualTo(16);
    }

    @Test
    void findOptimalPathFromAnyNode1() {
        String[] nodes = {"S", "11", "12", "21", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }

        graph.addEdge("S", "11", "", 2, 1);
        graph.addEdge("S", "12", "", 1, 2);

        graph.addEdge("11", "T", "", 5, 4);

        graph.addEdge("12", "21", "", 8, 2);

        graph.addEdge("21", "T", "", 1, 1000);

        double globalCompromise = 100;

        OptimalPathSolver solver = new OptimalPathSolver(graph, "S", "T", 0, 0);

        OptimalPathSolver.Result res = solver.solve();
        List<String> optPath = solver.restoreOptimalPath();

        System.out.println(res);
        System.out.println(optPath);

        Assertions.assertThat(res.getWin()).isEqualTo(5);


        solver.setStartState("21");
        solver.setGlobalCompromiseValue(globalCompromise);

        res = solver.solve();
        optPath = solver.restoreOptimalPath();

        System.out.println(res);
        System.out.println(optPath);

        Assertions.assertThat(res.getWin()).isEqualTo(9);
    }

    @Test
    void throwExceptionWhenThereIsNoPathFromStartToLast() {
        String[] nodes = {"S", "11", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }
        graph.addEdge("S", "11", "", 1, 1);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> new OptimalPathSolver(graph, "S", "T", 0, 0));
    }
}
