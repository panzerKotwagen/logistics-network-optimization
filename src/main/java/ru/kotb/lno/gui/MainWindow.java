package ru.kotb.lno.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static ru.kotb.lno.gui.Action.addEdgeAction;
import static ru.kotb.lno.gui.Action.addVertexAction;


/**
 * The class that main window of the application
 */
public class MainWindow extends JFrame {

    private DrawPanel drawPanel = new DrawPanel();

    public MainWindow() {
        super("logistics-network-optimization");

        this.setMinimumSize(new Dimension(900, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawPanel = new DrawPanel();
        this.add(drawPanel, BorderLayout.CENTER);

        Toolbar toolBar = new Toolbar();
        this.add(toolBar, BorderLayout.NORTH);

        Menu menuBar = new Menu();
        this.setJMenuBar(menuBar);

        addVertexAction.setMainWindow(this);
        addEdgeAction.setMainWindow(this);

        setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
