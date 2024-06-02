package ru.kotb.lno.gui.node;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The panel for drawing the graph
 */
@Getter
public class DrawPane extends Pane {

    private final Set<InfoNode> infoNodeSet = new HashSet<>();

    private final Set<InfoEdge> infoEdgeSet = new HashSet<>();

    private InfoNode selectedNode;

    public DrawPane() {
        setBackground(new Background(new BackgroundFill(
                Color.WHITESMOKE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));

        this.getChildren().add(new Canvas(900, 600));
    }

    //TODO: Optional
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
            infoNode.changeColor(Color.WHITE);
            this.getChildren().add(infoNode);
        }

        for (InfoEdge infoEdge : infoEdgeSet) {
            this.getChildren().add(infoEdge);
            infoEdge.toBack();
            infoEdge.changeColor(Color.LIGHTGREY);
            this.getChildren().add(infoEdge.text);
        }
    }

    public void addNode(String text, double x, double y) {
        InfoNode node = new InfoNode(text);
        node.setPosition(x, y);

        node.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            resetSelectedNode();

            selectedNode = node;
            selectedNode.changeColor(Color.RED);
        });

        infoNodeSet.add(node);
        update();
    }

    private void resetSelectedNode() {
        if (selectedNode != null) {
            selectedNode.changeColor(Color.YELLOW);
            selectedNode = null;
        }
    }

    public InfoEdge addEdge(InfoNode source, InfoNode target, double w1, double w2) {
        String weightText = String.format("(%.1f, %.1f)", w1, w2);
        InfoEdge edge = new InfoEdge(source, target, weightText);

        source.getEdgeList().add(edge);
        target.getEdgeList().add(edge);

        infoEdgeSet.add(edge);
        update();

        return edge;
    }

    public void removeNode(InfoNode node) {
        infoNodeSet.remove(node);
        infoEdgeSet.removeIf(infoEdge -> infoEdge.source == node || infoEdge.target == node);
        update();
    }

    public InfoEdge editEdgeWeight(InfoEdge infoEdge, double w1, double w2) {
        removeEdge(infoEdge);
        return addEdge(infoEdge.source, infoEdge.target, w1, w2);
    }

    public void removeEdge(InfoEdge infoEdge) {
        infoEdgeSet.remove(infoEdge);
        update();
    }

    @Getter
    public static class InfoNode extends StackPane {

        private static final double PADDING = 8;

        private final String name;

        private double x;

        private double y;

        private final List<InfoEdge> edgeList = new ArrayList<>();

        public InfoNode(String txt) {
            Text text = new Text(txt);
            text.setFont(Font.font("Arial", FontWeight.LIGHT, FontPosture.REGULAR, 14));
            double size = 20 + (2 * PADDING);
            setStyle("-fx-shape:\"M 0 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0\";" +
                    "-fx-border-color:black;" +
                    "-fx-border-width:2px;" +
                    "-fx-background-color:white;");

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

        public void changeColor(Color color) {
            this.setBackground(new Background(
                    new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }


    @Getter
    public static class InfoEdge extends Line {

        private final InfoNode source;

        private final InfoNode target;

        private Text text;

        public InfoEdge(InfoNode source, InfoNode target, String text) {
            this.startXProperty().bind(source.centerXProperty());
            this.startYProperty().bind(source.centerYProperty());
            this.endXProperty().bind(target.centerXProperty());
            this.endYProperty().bind(target.centerYProperty());
            this.setStrokeWidth(3);
            this.setStroke(Color.LIGHTGRAY);

            this.source = source;
            this.target = target;
            initText(text);
        }

        private void initText(String string) {
            double maxX = Math.max(this.getStartX(), this.getEndX());
            double minX = Math.min(this.getStartX(), this.getEndX());

            double maxY = Math.max(this.getStartY(), this.getEndY());
            double minY = Math.min(this.getStartY(), this.getEndY());

            double x = minX + (maxX - minX) / 2 - (double) (string.length() * 6) / 2;
            double y = minY + (maxY - minY) / 2 - 5;

            text = new Text(string);
            text.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            text.setLayoutX(x);
            text.setLayoutY(y);
        }

        public void changeColor(Color color) {
            this.setStroke(color);
        }

        @Override
        public String toString() {
            return "Edge{" + source + " - " + target + '}';
        }
    }
}
