package ru.kotb.lno.graph.algorithms;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/**
 * The class describing Dijkstra's algorithm for finding the shortest path
 */
@Setter
@NoArgsConstructor
public class DijkstraShortestPath {

    private static String getOppositeNode(Edge adjacentEdge, NodeAndDistance current) {
        String source = adjacentEdge.getSourceNode();
        String target = adjacentEdge.getTargetNode();
        if (source.equals(current.node)) {
            return target;
        } else {
            return source;
        }
    }

    public Map<String, Double> findShortestPath(Graph graph, String firstNodeName, int weightNumber) {
        Map<String, Double> distances = new LinkedHashMap<>();

        for (String vertex : graph.nodeNamesSet()) {
            distances.put(vertex, Double.MAX_VALUE);
        }
        distances.put(firstNodeName, 0d);

        Queue<NodeAndDistance> queue = new LinkedList<>();
        queue.add(new NodeAndDistance(firstNodeName, 0d));

        while (!queue.isEmpty()) {
            NodeAndDistance current = queue.poll();

            //Processing only the vertex with the smallest distance
            if (current.distance > distances.get(current.node)) {
                continue;
            }

            for (Edge adjacentEdge : graph.getAdjacentEdges(current.node)) {
                String neighbourNode = getOppositeNode(adjacentEdge, current);

                double distance = current.distance + adjacentEdge.getWeights()[weightNumber];

                //We consider this new path only if it is better than
                // any path we have found so far
                if (distance < distances.get(neighbourNode)) {
                    distances.put(neighbourNode, distance);
                    queue.add(new NodeAndDistance(neighbourNode, distance));
                }
            }
        }

        return distances;
    }


    @AllArgsConstructor
    private static class NodeAndDistance {

        public String node;

        public Double distance;
    }
}
