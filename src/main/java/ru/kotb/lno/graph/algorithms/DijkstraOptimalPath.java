package ru.kotb.lno.graph.algorithms;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.optimization.GlobalCriteria;
import ru.kotb.lno.optimization.Solver;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


/**
 * The class describing Dijkstra's algorithm for finding the shortest path
 */
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DijkstraOptimalPath {

    private Solver solver;

    //TODO: Describe
    public Map<String, GlobalCriteria> findOptimalPath(Graph graph, String firstNodeName) {
        Map<String, GlobalCriteria> criteriaMap = new LinkedHashMap<>();
        Map<String, Boolean> marked = new HashMap<>();
        for (String vertex : graph.nodeNamesSet()) {
            criteriaMap.put(vertex, GlobalCriteria.MAX_CRITERIA);
        }

        Queue<NodeAndCriteria> queue = new LinkedList<>();
        queue.add(new NodeAndCriteria(firstNodeName, GlobalCriteria.ZERO_CRITERIA));

        while (!queue.isEmpty()) {
            NodeAndCriteria current = queue.poll();
            if (marked.containsKey(current.node)) {
                continue;
            }
            marked.put(current.node, true);

            int res = solver.compareTwoCriteria(current.criteria, criteriaMap.get(current.node));
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
