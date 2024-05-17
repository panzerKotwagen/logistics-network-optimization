package ru.kotb.lno.gui.swing;

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

    int currentX, currentY, oldX, oldY;

    private final int tool = 1;

    public DrawPanel() {
        super();
        this.add(new JLabel("Graph panel"));
        this.setBackground(new java.awt.Color(255, 255, 255));

//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
//                oldX = e.getX();
//                oldY = e.getY();
//                System.out.println(oldX + " " + oldY);
//            }
//        });
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
            g2d.drawString(point.name, point.x + point.radius - point.name.length() * 4, point.y + point.radius + 4);
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
