package ru.kotb.lno.gui;

import ru.kotb.lno.graph.Graph;
import ru.kotb.lno.graph.impl.JGraphT;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * The class describes Action performing the following graph
 * operations: add vertex, add edge
 */
public class Action extends AbstractAction {

    Graph graph = new JGraphT();

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
                break;
            case "Add edge":
                break;
            default:
        }
    }
}
