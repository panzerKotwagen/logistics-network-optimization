package ru.kotb.lno.gui.fx.node;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ru.kotb.lno.gui.fx.action.GraphEditActions;

import java.util.HashSet;
import java.util.Set;


/**
 * The panel for drawing the graph
 */
public class DrawPane extends Pane {

    Set<InfoNode> infoNodeSet = new HashSet<>();

    public DrawPane() {
        setBackground(new Background(new BackgroundFill(Color.WHITESMOKE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));

        Canvas canvas = new Canvas(900, 600);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            if (!GraphEditActions.addNodeBtnIsPressed) {
                return;
            }

            int x = (int) mouseEvent.getX();
            int y = (int) mouseEvent.getY();
            String nodeName = GraphEditActions.addNode();
            infoNodeSet.add(buildAndAddNode(nodeName, x, y));

            MyToolBar.addNodeBtn.setDisable(false);
            GraphEditActions.addNodeBtnIsPressed = false;
        });

        this.getChildren().add(canvas);
    }

    public InfoNode getInfoNode(String text) {
        for (InfoNode node : infoNodeSet) {
            if (node.name.equals(text)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Build and place new node
     *
     * @param text node text
     * @param x    x position of the node
     * @param y    y position of the node
     * @return built node
     */
    public InfoNode buildAndAddNode(String text, double x, double y) {
        InfoNode node = new InfoNode(text);
        node.setPosition(x, y);
        this.getChildren().add(node);
        return node;
    }

    /**
     * Build and place line between two nodes
     *
     * @param sourceNode source node
     * @param targetNode target node
     */
    public void buildAndAddLine(InfoNode sourceNode, InfoNode targetNode) {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line();
        line.startXProperty().bind(sourceNode.centerXProperty());
        line.startYProperty().bind(sourceNode.centerYProperty());
        line.endXProperty().bind(targetNode.centerXProperty());
        line.endYProperty().bind(targetNode.centerYProperty());
        this.getChildren().add(line);
        line.toBack();
    }

    public static class InfoNode extends StackPane {

        private static final double PADDING = 8;

        private double x;

        private final String name;

        private double y;

        public InfoNode(String txt) {
            Text text = new Text(txt);
            double size = getWidth(text) + (2 * PADDING);
            setStyle("-fx-shape:\"M 0 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0\";-fx-border-color:black;-fx-border-width:1px;-fx-background-color:yellow;");
            setMinSize(size, size);
            getChildren().add(text);
            name = txt;

            widthProperty().addListener((obs, old, val) -> setLayoutX(x - val.doubleValue() / 2));
            heightProperty().addListener((obs, old, val) -> setLayoutY(y - val.doubleValue() / 2));
        }

        public DoubleBinding centerXProperty() {
            return layoutXProperty().add(widthProperty().divide(2));
        }

        public DoubleBinding centerYProperty() {
            return layoutYProperty().add(heightProperty().divide(2));
        }

        public void setPosition(double x, double y) {
            this.x = x;
            this.y = y;
            setLayoutX(x - getWidth() / 2);
            setLayoutY(y - getHeight() / 2);
        }

        private double getWidth(Text text) {
            new Scene(new Group(text));
            text.applyCss();
            return text.getLayoutBounds().getWidth();
        }
    }
}
