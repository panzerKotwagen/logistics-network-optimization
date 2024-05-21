package ru.kotb.lno.graph.algorithms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.components.Edge;

import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Finds the shortest paths from the vertex {@code } to all the others in
     * the graph by weight with the specified number
     *
     * @param graph        the graph in which the algorithm performs the work
     * @param startNode    the vertex from which the distance is being searched
     * @param weightNumber the number of the edge weight criterion
     * @return a data structure with the shortest distance to all
     * vertices with optimal paths in the form of a list of vertices
     */
    public Result findShortestPath(Graph graph, String startNode, int weightNumber) {
        Map<String, Double> distances = new LinkedHashMap<>();
        Map<String, String> previousNodeMap = new HashMap<>();

        for (String vertex : graph.nodeNamesSet()) {
            distances.put(vertex, Double.MAX_VALUE);
        }
        distances.put(startNode, 0d);

        Queue<NodeAndDistance> queue = new LinkedList<>();
        queue.add(new NodeAndDistance(startNode, 0d));

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
                    previousNodeMap.put(neighbourNode, current.node);
                    queue.add(new NodeAndDistance(neighbourNode, distance));
                }
            }
        }

        return new Result(distances, previousNodeMap);
    }

    /**
     * Restores the optimal path to the vertex {@code node}
     * and returns it as a list of vertices
     *
     * @param previousNodeMap map of vertices and the shortest vertices to them
     * @param node            the vertex to which we restore the shortest path
     * @return list of vertices
     */
    public List<String> restoreOptimalPath(Map<String, String> previousNodeMap, String node) {
        List<String> path = new ArrayList<>();
        path.add(node);

        String current = node;
        while (previousNodeMap.containsKey(current)) {
            current = previousNodeMap.get(current);
            path.add(current);
        }

        Collections.reverse(path);
        return path;
    }


    @AllArgsConstructor
    private static class NodeAndDistance {

        public String node;

        public Double distance;
    }


    @Getter
    public static class Result {

        private final Map<String, Double> distances;

        private final Map<String, String> previousNodeList;

        private Result(Map<String, Double> distances, Map<String, String> previousNodeList) {
            this.distances = distances;
            this.previousNodeList = previousNodeList;
        }
    }
}
