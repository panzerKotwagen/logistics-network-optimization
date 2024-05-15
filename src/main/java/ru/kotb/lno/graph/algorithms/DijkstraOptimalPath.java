package ru.kotb.lno.graph.algorithms;

import lombok.AllArgsConstructor;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.optimization.GlobalCriteria;
import ru.kotb.lno.optimization.Solver;
import ru.kotb.lno.optimization.schemes.CompromiseScheme;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


/**
 * The class describing Dijkstra's algorithm for finding the shortest path
 */
public class DijkstraOptimalPath {

    private final Solver solver;

    public DijkstraOptimalPath(CompromiseScheme scheme) {
        this.solver = new Solver(scheme);
    }

    //TODO: Describe
    public Map<String, GlobalCriteria> findOptimalPath(Graph graph, String firstNodeName) {
        Map<String, GlobalCriteria> criteriaMap = new LinkedHashMap<>();
        for (String vertex : graph.nodeNamesSet()) {
            criteriaMap.put(vertex, GlobalCriteria.MAX_CRITERIA);
        }

        Queue<NodeAndCriteria> queue = new LinkedList<>();
        queue.add(new NodeAndCriteria(firstNodeName, GlobalCriteria.ZERO_CRITERIA));

        while (!queue.isEmpty()) {
            NodeAndCriteria current = queue.poll();

            //Minus because we are solving the problem of minimization
            int res = -solver.compareTwoCriteria(current.criteria, criteriaMap.get(current.node));
            //Processing only the vertex with the smallest distance
            if (res < 0) {
                continue;
            }

            for (Edge adjacentEdge : graph.getAdjacentEdges(current.node)) {
                String neighbourNode = graph.getOppositeNode(adjacentEdge, current.node);
                GlobalCriteria comparedCriteria = current.criteria.add(
                        new GlobalCriteria(adjacentEdge.getWeights())
                );

                List<GlobalCriteria> criteriaList = List.of(
                        criteriaMap.get(neighbourNode),
                        comparedCriteria
                );

                GlobalCriteria bestCriteria = solver.solve(criteriaList);

                //We consider this new path only if it is better than
                // any path we have found so far
                if (bestCriteria.equals(comparedCriteria)) {
                    criteriaMap.put(neighbourNode, bestCriteria);
                    queue.add(new NodeAndCriteria(neighbourNode, bestCriteria));
                }
            }
        }
        return criteriaMap;
    }

    @AllArgsConstructor
    private static class NodeAndCriteria {

        public String node;

        public GlobalCriteria criteria;
    }
}