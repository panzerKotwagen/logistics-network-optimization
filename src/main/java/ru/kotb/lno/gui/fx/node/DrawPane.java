package ru.kotb.lno.gui.fx.node;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;


/**
 * The panel for drawing the graph
 */
public class DrawPane extends Pane {

    @Getter
    Set<InfoNode> infoNodeSet = new HashSet<>();

    public DrawPane() {
        setBackground(new Background(new BackgroundFill(Color.WHITESMOKE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));

        Canvas canvas = new Canvas(900, 600);
//        this.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
//            if (!GraphEditActions.addNodeBtnIsPressed) {
//                return;
//            }
//
//            int x = (int) mouseEvent.getX();
//            int y = (int) mouseEvent.getY();
//
//            String nodeName = GraphEditActions.addNode();
//            infoNodeSet.add(drawNode(nodeName, x, y));
//
//            MyToolBar.addNodeBtn.setDisable(false);
//            GraphEditActions.addNodeBtnIsPressed = false;
//        });

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
    public InfoNode drawNode(String text, double x, double y) {
        InfoNode node = new InfoNode(text);
//        node.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                node.getChildren().remove(0);
//            }
//        });
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
    public void drawLine(InfoNode sourceNode, InfoNode targetNode, double w1, double w2) {
        javafx.scene.shape.Line line = new javafx.scene.shape.Line();
        line.startXProperty().bind(sourceNode.centerXProperty());
        line.startYProperty().bind(sourceNode.centerYProperty());
        line.endXProperty().bind(targetNode.centerXProperty());
        line.endYProperty().bind(targetNode.centerYProperty());
        this.getChildren().add(line);

        drawEdgeWeights(w1, w2, line);

        line.toBack();
    }

    private void drawEdgeWeights(double w1, double w2, Line line) {
        double maxX = Math.max(line.getStartX(), line.getEndX());
        double minX = Math.min(line.getStartX(), line.getEndX());

        double maxY = Math.max(line.getStartY(), line.getEndY());
        double minY = Math.min(line.getStartY(), line.getEndY());

        double x = minX + (maxX - minX) / 2;
        double y = minY + (maxY - minY) / 2 - 10;

        String weightText = String.format("(%.1f, %.1f)", w1, w2);
        Text text = new Text(weightText);
        text.setLayoutX(x);
        text.setLayoutY(y);
        this.getChildren().add(text);
    }

    public static class InfoNode extends StackPane {

        private static final double PADDING = 8;

        private final String name;

        private double x;

        private double y;

        public InfoNode(String txt) {
            Text text = new Text(txt);
            text.setFont(Font.font("Arial", FontWeight.LIGHT, FontPosture.REGULAR, 14));
            double size = 20 + (2 * PADDING);
            setStyle("-fx-shape:\"M 0 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0\";" +
                    "-fx-border-color:black;" +
                    "-fx-border-width:2px;" +
                    "-fx-background-color:yellow;");

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
