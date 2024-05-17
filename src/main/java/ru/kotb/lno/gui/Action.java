package ru.kotb.lno.gui;

import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.impl.JGraphT;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

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

    private static DrawPanel drawPanel;

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
        Action.drawPanel = mainWindow.getDrawPanel();

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (addVertexAction.enabled) {
                    return;
                }

                super.mousePressed(e);
                int x = e.getX() - 25;
                int y = e.getY() - 25;
                System.out.println(x + " " + y);

                String vertexName = addVertex();
                if (vertexName == null) {
                    return;
                }

                System.out.println(graph.nodeNamesSet());
                drawPanel.addPoint(new DrawPanel.Point(x, y, vertexName));
                drawPanel.repaint();
                addVertexAction.setEnabled(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comStr = e.getActionCommand();
        switch (comStr) {
            case "Add vertex":
                addVertexAction.setEnabled(false);
//                addVertex();
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
    public String addVertex() {
        Optional<String> res = Optional.ofNullable(
                showInputDialog(mainWindow, "Vertex name"));

        if (res.isPresent()) {
            String vertexName = res.get();
            graph.addNode(vertexName);
            System.out.println(graph.getNode(vertexName));
            return vertexName;
        }
        return null;
    }

    public void addVertexByClickingMouseOnDrawPanel() {
//        drawPanel.
    }

    /**
     * Invoke dialog window to add an edge
     */
    public void addEdge() {
        AddEdgeDialog addEdgeDialog = new AddEdgeDialog(mainWindow);
        Optional<EdgeDTO> res = Optional.ofNullable(addEdgeDialog.getInputtedData());

        if (res.isPresent()) {
            EdgeDTO edge = res.get();
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
}
