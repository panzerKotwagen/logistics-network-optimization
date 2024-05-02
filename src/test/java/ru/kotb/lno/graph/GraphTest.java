package ru.kotb.lno.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.graph.components.Node;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.List;
import java.util.Set;


@TestComponent
public class GraphTest {

    private Graph graph = new JGraphT();

//    @Autowired
//    public GraphTest(Graph graph) {
//        this.graph = graph;
//    }

//    @BeforeEach
//    void foo() {
//        graph = new GraphImpl();
//    }

    @Test
    void getNonExistingNodeReturnNull() {
        Assertions.assertThat(graph.getEdge("S", "S")).isNull();
    }

    @Test
    void getExistingNodeReturnNode() {
        String nodeName = "S";
        graph.addNode(nodeName);
        Node node =  graph.getNode(nodeName);
        Assertions.assertThat(graph.getNode(nodeName)).isNotNull();
    }

    @Test
    void getExistingEdgeReturnEdge() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, "Edge", 1, 1);
        Assertions.assertThat(graph.getEdge(nodeName1, nodeName2)).isNotNull();
    }

    @Test
    void getExistingEdgeReturnEdgeRegardlessOfNodeNameOrder() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, "Edge", 1, 1);
        Assertions.assertThat(graph.getEdge(nodeName1, nodeName2)).isNotNull();
        Assertions.assertThat(graph.getEdge(nodeName2, nodeName1)).isNotNull();
    }

    @Test
    void addMultiEdgeThrowsException() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        String edgeName = "Edge";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, edgeName, 1, 1);
        graph.addEdge(nodeName1, nodeName2, edgeName, 1, 1);
        Assertions.assertThat(graph.getEdge(nodeName1, nodeName2)).isNotNull();
        Assertions.assertThat(graph.getEdge(nodeName2, nodeName1)).isNotNull();
    }

    @Test
    void addMultiEdgeThrowsExceptionRegardlessOfNodeNameOrder() {
        String nodeName1 = "S";
        String nodeName2 = "T";
        String edgeName = "Edge";
        graph.addNode(nodeName1);
        graph.addNode(nodeName2);

        graph.addEdge(nodeName1, nodeName2, edgeName, 1, 1);
        graph.addEdge(nodeName2, nodeName1, edgeName, 1, 1);

        Assertions.assertThat(graph.getEdges(nodeName1).size()).isEqualTo(1);
        Assertions.assertThat(graph.getEdges(nodeName2).size()).isEqualTo(1);
    }

    @Test
    void addEdgeToNonExistingNodeThrowsException() {
        String nodeName1 = "S";
        graph.addNode(nodeName1);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> graph.addEdge(nodeName1, "nodeName2", "edge", 1, 1));
    }

    @Test
    void addLoopToGraphThrowException() {
        String nodeName = "S";
        graph.addNode(nodeName);

        org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> graph.addEdge(nodeName, nodeName, "edge", 1, 1));
    }

    @Test
    void removeEdgeCorrectly() {
        List<String> nodeNames = List.of("1", "2", "3", "4");

        for (String name : nodeNames) {
            graph.addNode(name);
        }

        for (int i = 0; i < nodeNames.size(); i++) {
            for (int j = 1; j < nodeNames.size(); j++) {
                if (i != j) {
                    graph.addEdge(nodeNames.get(i), nodeNames.get(j), "edge", 1, 2);
                }
            }
        }

        for (int i = 0; i < nodeNames.size(); i++) {
            for (int j = 1; j < nodeNames.size(); j++) {
                if (i != j) {
                    graph.removeEdge(nodeNames.get(i), nodeNames.get(j));
                    Edge edge = graph.getEdge(nodeNames.get(i), nodeNames.get(j));
                    boolean hasEdge = graph.hasEdge(nodeNames.get(i), nodeNames.get(j));
                    Assertions.assertThat(edge).isNull();
                    Assertions.assertThat(hasEdge).isFalse();
                }
            }
        }
    }

    @Test
    void removeNodeRemoveConnectedEdges() {
        List<String> nodeNames = List.of("1", "2", "3", "4");
        String removedNodeName = nodeNames.get(0);
        for (String name : nodeNames) {
            graph.addNode(name);
        }

        for (int i = 0; i < nodeNames.size(); i++) {
            for (int j = 0; j < nodeNames.size(); j++) {
                if (i != j) {
                    graph.addEdge(nodeNames.get(i), nodeNames.get(j), "edge", 1, 2);
                }
            }
        }

        Set<Edge> edgeSet = graph.getEdges(removedNodeName);
        int edgeSize = edgeSet.size();
        Assertions.assertThat(edgeSize).isEqualTo(3);

        graph.removeNode(removedNodeName);
        edgeSet = graph.getEdges(removedNodeName);
        Assertions.assertThat(edgeSet).isNull();

        for (int i = 1; i < nodeNames.size(); i++) {
            Edge edge = graph.getEdge(removedNodeName, nodeNames.get(i));
            Assertions.assertThat(edge).isNull();
        }
    }
}
