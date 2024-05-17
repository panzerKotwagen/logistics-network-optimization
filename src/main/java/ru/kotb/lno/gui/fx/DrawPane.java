package ru.kotb.lno.gui.fx;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


/**
 * The panel for drawing the graph
 */
public class DrawPane extends Pane {

    private final List<Point> pointList = new ArrayList<>();

    private final List<Line> lineList = new ArrayList<>();

    private final GraphicsContext graphicsContext;

    private final Canvas canvas;

    public DrawPane() {
        setBackground(new Background(new BackgroundFill(Color.WHITESMOKE.brighter(), CornerRadii.EMPTY, Insets.EMPTY)));

        canvas = new Canvas(900, 600);
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int) mouseEvent.getX();
                int y = (int) mouseEvent.getY();
                System.out.println(x + " : " + y);
                String nodeName = GraphEditActions.addNode();
                drawPoint(new Point(x, y, nodeName));
            }
        });

        graphicsContext = canvas.getGraphicsContext2D();
        initGraphicsContext();

        this.getChildren().add(canvas);
    }

    public void update() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Point point : pointList) {
            drawPoint(point);
        }
    }

    /**
     * Draw a single point
     *
     * @param point point
     */
    public void drawPoint(Point point) {
        double x = point.x;
        double y = point.y;
        int radius = point.radius;
        double width = radius * 2;
        Text text = new Text(x - point.name.length() * 6, y + 5, point.name);
        text.setFont(new Font(20));
        this.getChildren().add(text);
        graphicsContext.strokeOval(x - radius, y - radius, width, width);
    }

    private void initGraphicsContext() {
        graphicsContext.setStroke(Color.FORESTGREEN.brighter());
        graphicsContext.setLineWidth(5);
    }

    public static class Point {

        private final int radius = 30;

        private final int x;

        private final int y;

        String name;

        public Point(int x, int y, String name) {
            this.x = x;
            this.y = y;
            this.name = name;
        }
    }


    @Getter
    public static class Line {

        private Point source;

        private Point target;
    }
}
