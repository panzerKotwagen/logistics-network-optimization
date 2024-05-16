package ru.kotb.lno.gui;

import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.impl.JGraphT;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static javax.swing.JOptionPane.showInputDialog;


/**
 * The class describes action performing the following graph
 * operations: add vertex, add edge
 */
public class Action extends AbstractAction {

    public static final Action addVertexAction = new Action(
            "Add vertex",
            KeyEvent.VK_A,
            KeyEvent.VK_A,
            "Add (Ctrl+A)");

    public static final Action addEdgeAction = new Action(
            "Add edge",
            KeyEvent.VK_E,
            KeyEvent.VK_E,
            "Add (Ctrl+E)");

    /**
     * The graph to edit
     */
    private static final Graph graph = new JGraphT();

    /**
     * The main window of the application
     */
    private static MainWindow mainWindow;

    /**
     * Constructor for {@code Action}
     *
     * @param name        name of the action
     * @param mnemonicKey the key for calling the action
     * @param accel       accelerator key
     * @param tTip        description
     */
    public Action(String name, int mnemonicKey,
                  int accel, String tTip) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accel,
                InputEvent.CTRL_DOWN_MASK));
        putValue(MNEMONIC_KEY, mnemonicKey);
        putValue(SHORT_DESCRIPTION, tTip);
    }

    /**
     * Initializes main window
     *
     * @param mainWindow the main window of the application
     */
    public void setMainWindow(MainWindow mainWindow) {
        Action.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comStr = e.getActionCommand();
        switch (comStr) {
            case "Add vertex":
                addVertex();
                break;
            case "Add edge":
                addEdge();
                break;
            default:
        }
    }

    /**
     * Invoke dialog window to add a vertex
     */
    public void addVertex() {
        String vertexName = showInputDialog(mainWindow, "Vertex name");
        graph.addNode(vertexName);
        System.out.println(graph.getNode(vertexName));
    }

    /**
     * Invoke dialog window to add an edge
     */
    public void addEdge() {
        AddEdgeDialog addEdgeDialog = new AddEdgeDialog(mainWindow);
        EdgeDTO edge = addEdgeDialog.getInputtedData();

        graph.addEdge(
                edge.getSource(),
                edge.getTarget(),
                "",
                edge.getWeight1(),
                edge.getWeight2()
        );
        System.out.println(graph.getEdge(edge.getSource(), edge.getTarget()));
    }
}
