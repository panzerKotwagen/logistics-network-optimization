package ru.kotb.lno.gui.fx;

import javafx.scene.control.TextInputDialog;
import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.Optional;
import java.util.Set;


/**
 * The class describes action performing the following graph
 * operations: add vertex, add edge
 */
public class GraphEditActions {

    /**
     * The graph to edit
     */
    private static final Graph graph = new JGraphT();

    public static boolean addNodeBtnIsPressed = false;

    public static Set<String> getNodes() {
        return graph.nodeNamesSet();
    }

    /**
     * Add node to the graph
     */
    public static String addNode() {
        Optional<String> nodeName = invokeNodeDialog();
        if (nodeName.isEmpty()) {
            return null;
        }
        graph.addNode(nodeName.get());
        System.out.println(graph.getNode(nodeName.get()));
        return nodeName.get();
    }

    /**
     * Add edge to the graph
     */
    //TODO: replace return type  with Optional
    public static EdgeDTO addEdge() {
        Optional<EdgeDTO> res = invokeEdgeDialog();
        if (res.isEmpty()) {
            return null;
        }

        EdgeDTO edge = res.get();
        graph.addEdge(
                edge.getSource(),
                edge.getTarget(),
                "",
                edge.getWeight1(),
                edge.getWeight2()
        );
        System.out.println(graph.getEdge(edge.getSource(), edge.getTarget()));
        return edge;
    }

    /**
     * Invoke dialog window to write node name
     *
     * @return {@code Optional<String>} of name
     */
    private static Optional<String> invokeNodeDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add node");
        dialog.setHeaderText("New node");
        dialog.setContentText("Name");

        return dialog.showAndWait();
    }

    /**
     * Invoke dialog window to write info about edge
     *
     * @return {@code Optional<EdgeDTO>} of edge
     */
    private static Optional<EdgeDTO> invokeEdgeDialog() {
        return new AddEdgeDialog().invoke();
    }
}
