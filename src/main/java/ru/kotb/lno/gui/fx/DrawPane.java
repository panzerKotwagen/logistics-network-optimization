package ru.kotb.lno.gui.fx;

import javafx.beans.binding.DoubleBinding;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


/**
 * The panel for drawing the graph
 */
public class DrawPane extends Pane {

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
            buildAndAddNode(this, null, nodeName, x, y);

            MyToolBar.addNodeBtn.setDisable(false);
            GraphEditActions.addNodeBtnIsPressed = false;
        });

        this.getChildren().add(canvas);
    }

    public static InfoNode buildAndAddNode(Pane root, InfoNode parentNode, String text, double x, double y) {
        InfoNode node = new InfoNode(text);
        node.setPosition(x, y);
        root.getChildren().add(node);
        if (parentNode != null) {
            javafx.scene.shape.Line line = new javafx.scene.shape.Line();
            line.startXProperty().bind(parentNode.centerXProperty());
            line.startYProperty().bind(parentNode.centerYProperty());
            line.endXProperty().bind(node.centerXProperty());
            line.endYProperty().bind(node.centerYProperty());
            root.getChildren().add(line);
            line.toBack();
        }
        return node;
    }

    public static class InfoNode extends StackPane {

        private static final double PADDING = 8;

        private double x;

        private double y;

        public InfoNode(String txt) {
            Text text = new Text(txt);
            double size = getWidth(text) + (2 * PADDING);
            setStyle("-fx-shape:\"M 0 0 m -5, 0 a 5,5 0 1,0 10,0 a 5,5 0 1,0 -10,0\";-fx-border-color:black;-fx-border-width:1px;-fx-background-color:yellow;");
            setMinSize(size, size);
            getChildren().add(text);

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
