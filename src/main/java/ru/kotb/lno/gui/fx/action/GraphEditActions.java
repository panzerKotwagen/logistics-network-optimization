package ru.kotb.lno.gui.fx.action;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.dto.SettingsDTO;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.algorithms.OptimalPathSolver;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.graph.impl.JGraphT;
import ru.kotb.lno.gui.fx.dialog.AddEdgeDialog;
import ru.kotb.lno.gui.fx.dialog.OptimizeDialog;
import ru.kotb.lno.gui.fx.node.DrawPane;
import ru.kotb.lno.gui.fx.node.MyToolBar;

import java.util.List;
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

    public void removeNode() {
        if (drawPane.getInfoNodeSet() != null) {
            String removed = drawPane.getSelectedNode().getName();
            graph.removeNode(removed);

            drawPane.removeNode(drawPane.getSelectedNode());
        }
    }

    public void update() {
        drawPane.update();
    }

    public void clear() {
        drawPane.getInfoNodeSet().clear();
        drawPane.getInfoEdgeSet().clear();
        drawPane.update();
    }

    public void optimize() {
        Optional<SettingsDTO> settingsOptional = new OptimizeDialog(this).invoke();
        if (settingsOptional.isEmpty()) {
            return;
        }

        SettingsDTO settings = settingsOptional.get();
        OptimalPathSolver solver = new OptimalPathSolver(
                graph, settings.getStart(), settings.getEnd(), settings.getMainCriteriaNum(), settings.getConcessionValue());

        solver.solve();
        List<String> optPath = solver.restoreOptimalPath();

        DrawPane.InfoNode startNode = null;
        for (DrawPane.InfoNode infoNode : drawPane.getInfoNodeSet()) {
            if (optPath.get(0).equals(infoNode.getName())) {
                startNode = infoNode;
                startNode.changeColor(Color.GREEN);
                break;
            }
        }

        if (startNode == null) {
            return;
        }

        double[] weightSum = new double[2];

        for (int i = 1; i < optPath.size(); i++) {
            String source = optPath.get(i - 1);
            String target = optPath.get(i);

            Edge edge = graph.getEdge(source, target);

            for (int j = 0; j < 2; j++) {
                weightSum[i] += edge.getWeights()[i];
            }

            for (DrawPane.InfoEdge infoEdge : startNode.getEdgeList()) {
                String s = infoEdge.getSource().getName();
                String t = infoEdge.getTarget().getName();

                if (s.equals(source) && t.equals(target)
                        || s.equals(target) && t.equals(source)) {
                    infoEdge.changeColor(Color.GREEN);

                    if (startNode == infoEdge.getSource()) {
                        startNode = infoEdge.getTarget();
                    } else {
                        startNode = infoEdge.getSource();
                    }

                    startNode.changeColor(Color.GREEN);
                    break;
                }
            }
        }

        String weights = String.format("(%.1f, %.1f)", weightSum[0], weightSum[1]);
        Text text = new Text(weights);
        text.setFont(Font.font("Arial", FontWeight.LIGHT, FontPosture.REGULAR, 15));
        text.setX(800);
        text.setY(20);
        drawPane.getChildren().add(text);
    }
}
