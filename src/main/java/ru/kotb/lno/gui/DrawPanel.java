package ru.kotb.lno.gui;

import lombok.Getter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;


public class DrawPanel extends JPanel {

    private final List<Point> pointList = new ArrayList<>();

    private final List<Line> lineList = new ArrayList<>();

    public DrawPanel() {
        super();
        this.add(new JLabel("Graph panel"));
    }

    public static class Point {

        String name;

        private final int radius = 25;

        private final int x;

        private final int y;

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

    public void addPoint(Point point) {
        pointList.add(point);
    }

    public void addLine(Line line) {
        lineList.add(line);
    }

    private void doDrawing(Graphics g) {
        var g2d = (Graphics2D) g;

        g2d.setColor(Color.black);

        Dimension size = getSize();
        Insets insets = getInsets();

        for (Point point : pointList) {
            int w = size.width - insets.left - insets.right;
            int h = size.height - insets.top - insets.bottom;

            int x = Math.abs(point.x) % w;
            int y = Math.abs(point.y) % h;
            g2d.drawOval(x, y, point.radius * 2, point.radius * 2);
        }

        for (Line line : lineList) {
            int x1 = line.getSource().x;
            int y1 = line.getSource().y;
            int x2 = line.getTarget().x;
            int y2 = line.getTarget().y;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}
