package ru.kotb.lno.optimization;

import org.junit.jupiter.api.Test;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.algorithms.DijkstraShortestPath;
import ru.kotb.lno.graph.algorithms.DynamicProgrammingSolver;
import ru.kotb.lno.graph.impl.JGraphT;


public class Dynamic {

    private final Graph graph = new JGraphT();

    private final DynamicProgrammingSolver solver = new DynamicProgrammingSolver();

    private final DijkstraShortestPath optimalPath = new DijkstraShortestPath();

    @Test
    void name() {
        String[] nodes = {"S", "11", "12", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }

        graph.addEdge("S", "11", "", 6, 3);
        graph.addEdge("S", "12", "", 5, 7);

        graph.addEdge("11", "T", "", 9, 13);
        graph.addEdge("12", "T", "", 5, 10);

        DijkstraShortestPath.Result w1Result = optimalPath.findShortestPath(graph, "S", 0);
        solver.init(graph, optimalPath.restoreOptimalPath(w1Result.getPreviousNodeList(), "T"));

        DynamicProgrammingSolver.WinningAndControl res = solver.solve(new DynamicProgrammingSolver.State("S"));
    }
}
