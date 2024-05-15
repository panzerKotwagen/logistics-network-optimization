package ru.kotb.lno.graph;

import org.junit.jupiter.api.Test;
import ru.kotb.lno.graph.algorithms.DijkstraOptimalPath;
import ru.kotb.lno.graph.impl.JGraphT;
import ru.kotb.lno.optimization.GlobalCriteria;
import ru.kotb.lno.optimization.schemes.impl.Convolution;

import java.util.Map;


public class ShortestPathTest {

    private final Graph graph = new JGraphT();

    private final DijkstraOptimalPath optimalPath = new DijkstraOptimalPath(new Convolution(1, 0.5));

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

        Map<String, GlobalCriteria> distances = optimalPath.findOptimalPath(graph, "S");
        GlobalCriteria distanceToT = distances.get("T");
        GlobalCriteria distanceTo2 = distances.get("2");
        GlobalCriteria distanceTo3 = distances.get("3");
        GlobalCriteria distanceTo4 = distances.get("4");
        GlobalCriteria distanceTo5 = distances.get("5");
//
//        Assertions.assertThat(distanceToT).isEqualTo(11);
//        Assertions.assertThat(distanceTo2).isEqualTo(7);
//        Assertions.assertThat(distanceTo3).isEqualTo(9);
//        Assertions.assertThat(distanceTo4).isEqualTo(20);
//        Assertions.assertThat(distanceTo5).isEqualTo(20);
    }
}
