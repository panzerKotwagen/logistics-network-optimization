package ru.kotb.lno.gui;

import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.impl.JGraphT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static javax.swing.JOptionPane.showInputDialog;


/**
 * The class describes Action performing the following graph
 * operations: add vertex, add edge
 */
public class Action extends AbstractAction {

    private static MainWindow mainWindow;

    private static final Graph graph = new JGraphT();

    public void setMainWindow(MainWindow mainWindow) {
        Action.mainWindow = mainWindow;
    }

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

    public Action(String name, int mnemonicKey,
                  int accel, String tTip) {
        super(name);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(accel,
                InputEvent.CTRL_DOWN_MASK));
        putValue(MNEMONIC_KEY, mnemonicKey);
        putValue(SHORT_DESCRIPTION, tTip);
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
        System.out.println(vertexName);
        graph.addNode(vertexName);
        System.out.println(graph.getNode(vertexName));
    }

    public void addEdge() {
        addEdgeDialog();
    }

    /**
     * Invoke dialog window to add an edge
     */
    public void addEdgeDialog() {
        JDialog addEdgeDialog = new JDialog(mainWindow, "Add edge", true);
        addEdgeDialog.setSize(200, 150);
        addEdgeDialog.setResizable(false);
        addEdgeDialog.getContentPane().setLayout(new FlowLayout());
        addEdgeDialog.setLocationRelativeTo(null);

        JLabel sourceLabel = new JLabel("Source");
        JTextField sourceTextField = new JTextField(10);
        JLabel targetLabel = new JLabel("Target");
        JTextField targetTextField = new JTextField(10);
        JLabel weights = new JLabel("Weights");
        JTextField weightsTextField1 = new JTextField(5);
        JTextField weightsTextField2 = new JTextField(5);

        addEdgeDialog.add(sourceLabel);
        addEdgeDialog.add(sourceTextField);
        addEdgeDialog.add(targetLabel);
        addEdgeDialog.add(targetTextField);
        addEdgeDialog.add(weights);
        addEdgeDialog.add(weightsTextField1);
        addEdgeDialog.add(weightsTextField2);

        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> {
            String source = sourceTextField.getText();
            String target = targetTextField.getText();
            int weight1 = Integer.parseInt(weightsTextField1.getText());
            int weight2 = Integer.parseInt(weightsTextField2.getText());
            graph.addEdge(source, target, "", weight1, weight2);
            addEdgeDialog.dispose();
            System.out.println(graph.getEdge(source, target));
        });
        cancelBtn.addActionListener((e) -> addEdgeDialog.dispose());

        addEdgeDialog.add(okBtn);
        addEdgeDialog.add(cancelBtn);

        addEdgeDialog.setVisible(true);

    }
}
