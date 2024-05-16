package ru.kotb.lno.gui;

import javax.swing.*;
import java.awt.*;

import static ru.kotb.lno.gui.Action.addEdgeAction;
import static ru.kotb.lno.gui.Action.addVertexAction;


/**
 * The class that main window of the application
 */
public class MainWindow extends JFrame {

    private JPanel graphPanel;

    public MainWindow() {
        super("logistics-network-optimization");

        this.setMinimumSize(new Dimension(650, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        graphPanel = new JPanel();
        graphPanel.add(new JLabel("Graph panel"));
        this.add(graphPanel, BorderLayout.CENTER);

        Toolbar toolBar = new Toolbar();
        this.add(toolBar, BorderLayout.NORTH);

        Menu menuBar = new Menu();
        this.setJMenuBar(menuBar);

        addVertexAction.setMainWindow(this);
        addEdgeAction.setMainWindow(this);

        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
