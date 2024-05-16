package ru.kotb.lno.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kotb.lno.graph.algorithms.DijkstraOptimalPath;
import ru.kotb.lno.graph.impl.JGraphT;
import ru.kotb.lno.optimization.GlobalCriteria;
import ru.kotb.lno.optimization.Solver;
import ru.kotb.lno.optimization.schemes.CompromiseScheme;
import ru.kotb.lno.optimization.schemes.impl.Convolution;

import java.util.Map;


public class OptimalPathTest {

    private final Graph graph = new JGraphT();

    private final DijkstraOptimalPath optimalPath = new DijkstraOptimalPath();

    @Test
    void findOptimalPath() {
        String[] nodes = {"S", "1", "2", "3", "T"};
        for (String node : nodes) {
            graph.addNode(node);
        }

        graph.addEdge("S", "1", "", 1, 1);
        graph.addEdge("S", "2", "", 1, 1);
        graph.addEdge("S", "3", "", 1, 1);

        graph.addEdge("1", "T", "", 10, 1);

        graph.addEdge("2", "T", "", 5, 5);

        graph.addEdge("3", "T", "", 1, 10);

        double[] weights1 = new double[]{1, 0.1};
        double[] weights2 = new double[]{0.1, 1};
        double[] weights3 = new double[]{0.5, 0.5};

        CompromiseScheme scheme1 = new Convolution(weights1);
        CompromiseScheme scheme2 = new Convolution(weights2);
        CompromiseScheme scheme3 = new Convolution(weights3);

        optimalPath.setSolver(new Solver(scheme1));
        Map<String, GlobalCriteria> distances = optimalPath.findOptimalPath(graph, "S");
        GlobalCriteria distanceToT = distances.get("T");
        GlobalCriteria expected = new GlobalCriteria(2, 11);
        Assertions.assertThat(distanceToT).isEqualTo(expected);

        optimalPath.setSolver(new Solver(scheme2));
        distances = optimalPath.findOptimalPath(graph, "S");
        distanceToT = distances.get("T");
        expected = new GlobalCriteria(11, 2);
        Assertions.assertThat(distanceToT).isEqualTo(expected);

        optimalPath.setSolver(new Solver(scheme3));
        distances = optimalPath.findOptimalPath(graph, "S");
        distanceToT = distances.get("T");
        expected = new GlobalCriteria(6, 6);
        Assertions.assertThat(distanceToT).isEqualTo(expected);
    }
}
