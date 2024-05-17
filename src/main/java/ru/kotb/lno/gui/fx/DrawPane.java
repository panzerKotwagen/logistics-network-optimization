package ru.kotb.lno.gui.fx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
        canvas = new Canvas(300, 300);
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
        double width = point.radius * 2;
        graphicsContext.strokeOval(x, y, width, width);
    }

    private void initGraphicsContext() {
        graphicsContext.setStroke(Color.FORESTGREEN.brighter());
        graphicsContext.setLineWidth(5);
    }

    public static class Point {

        private final int radius = 25;

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
