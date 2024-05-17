package ru.kotb.lno.gui.fx;

import javafx.scene.control.TextInputDialog;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.impl.JGraphT;

import java.util.Optional;


/**
 * The class describes action performing the following graph
 * operations: add vertex, add edge
 */
public class GraphEditActions {

    /**
     * The graph to edit
     */
    private static final Graph graph = new JGraphT();

    /**
     * Add node to the graph
     */
    public static void addNode() {
        Optional<String> nodeName = addNodeDialog();
        if (nodeName.isEmpty()) {
            return;
        }
        graph.addNode(nodeName.get());
        System.out.println(graph.getNode(nodeName.get()));
    }

    /**
     * Invoke dialog window to write node name
     *
     * @return {@code Optional<String>} of name
     */
    private static Optional<String> addNodeDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add node");
        dialog.setHeaderText("New node");
        dialog.setContentText("Name");

        return dialog.showAndWait();
    }
}
