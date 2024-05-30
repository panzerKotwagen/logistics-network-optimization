package ru.kotb.lno.gui.fx.action;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.impl.JGraphT;
import ru.kotb.lno.gui.fx.dialog.AddEdgeDialog;
import ru.kotb.lno.gui.fx.node.DrawPane;
import ru.kotb.lno.gui.fx.node.MyToolBar;

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
    private final Graph graph;

    private final DrawPane drawPane;

    public GraphEditActions(DrawPane drawPane) {
        this.graph = new JGraphT();
        this.drawPane = drawPane;

        mouseEventHandler();
    }

    public Set<String> getNodes() {
        return graph.nodeNamesSet();
    }

    /**
     * Invoke dialog window to write node name
     *
     * @return {@code Optional<String>} of name
     */
    private Optional<String> invokeNodeDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add node");
        dialog.setHeaderText("New node");
        dialog.setContentText("Name");

        return dialog.showAndWait();
    }

    private void mouseEventHandler() {
        drawPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this::addNodeByMouseClicking);
    }

    private void addNodeByMouseClicking(MouseEvent mouseEvent) {
        if (!MyToolBar.addNodeBtn.isDisable()) {
            return;
        }

        Optional<String> optionalName = invokeNodeDialog();
        if (optionalName.isEmpty()) {
            return;
        }

        String name = optionalName.get();

        graph.addNode(name);
        System.out.println(graph.getNode(name));

        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        drawPane.addNode(name, x, y);

        MyToolBar.addNodeBtn.setDisable(false);
    }

    /**
     * Invoke dialog window to write info about edge
     *
     * @return {@code Optional<EdgeDTO>} of edge
     */
    private Optional<EdgeDTO> invokeEdgeDialog() {
        return new AddEdgeDialog(this).invoke();
    }

    /**
     * Add edge to the graph
     */
    public void addEdge() {
        Optional<EdgeDTO> edgeOptional = invokeEdgeDialog();
        if (edgeOptional.isEmpty()) {
            return;
        }

        EdgeDTO edge = edgeOptional.get();
        graph.addEdge(
                edge.getSource(),
                edge.getTarget(),
                "",
                edge.getWeight1(),
                edge.getWeight2()
        );


        DrawPane.InfoNode source = drawPane.getInfoNode(edge.getSource());
        DrawPane.InfoNode target = drawPane.getInfoNode(edge.getTarget());
        drawPane.addEdge(source, target, edge.getWeight1(), edge.getWeight2());

        System.out.println(graph.getEdge(edge.getSource(), edge.getTarget()));
    }
}
