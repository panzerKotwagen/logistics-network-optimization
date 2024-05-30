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
@Getter
public class DrawPane extends Pane {

    Set<InfoNode> infoNodeSet = new HashSet<>();

    Set<InfoEdge> infoEdgeSet = new HashSet<>();

    public DrawPane() {
        setBackground(new Background(new BackgroundFill(
                Color.WHITESMOKE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));

        this.getChildren().add(new Canvas(900, 600));
    }

    public InfoNode getInfoNode(String text) {
        for (InfoNode node : infoNodeSet) {
            if (node.name.equals(text)) {
                return node;
            }
        }
        return null;
    }

    public void update() {
        this.getChildren().clear();
        this.getChildren().add(new Canvas(900, 600));

        for (InfoNode infoNode : infoNodeSet) {
            this.getChildren().add(infoNode);
        }

        for (InfoEdge infoEdge : infoEdgeSet) {
            this.getChildren().add(infoEdge);
            infoEdge.toBack();
            this.getChildren().add(infoEdge.text);
        }
    }

    /**
     * Build and place new node
     *
     * @param text node text
     * @param x    x position of the node
     * @param y    y position of the node
     */
    public void addNode(String text, double x, double y) {
        InfoNode node = new InfoNode(text);
        node.setPosition(x, y);
        infoNodeSet.add(node);
    }

    /**
     * Build and place line between two nodes
     *
     * @param source source node
     * @param target target node
     */
    public void addEdge(InfoNode source, InfoNode target, double w1, double w2) {
        String weightText = String.format("(%.1f, %.1f)", w1, w2);
        InfoEdge line = new InfoEdge(source, target, weightText);
        infoEdgeSet.add(line);
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
    }


    public static class InfoEdge extends Line {

        private InfoNode source;

        private InfoNode target;

        private Text text;

        public InfoEdge(InfoNode source, InfoNode target, String text) {
            this.startXProperty().bind(source.centerXProperty());
            this.startYProperty().bind(source.centerYProperty());
            this.endXProperty().bind(target.centerXProperty());
            this.endYProperty().bind(target.centerYProperty());

            this.source = source;
            this.target = target;
            initText(text);
        }

        private void initText(String string) {
            double maxX = Math.max(this.getStartX(), this.getEndX());
            double minX = Math.min(this.getStartX(), this.getEndX());

            double maxY = Math.max(this.getStartY(), this.getEndY());
            double minY = Math.min(this.getStartY(), this.getEndY());

            double x = minX + (maxX - minX) / 2;
            double y = minY + (maxY - minY) / 2 - 10;

            text = new Text(string);
            text.setLayoutX(x);
            text.setLayoutY(y);
        }
    }
}
