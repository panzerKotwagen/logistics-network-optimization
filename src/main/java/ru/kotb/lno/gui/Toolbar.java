package ru.kotb.lno.gui;

import javax.swing.*;

import static ru.kotb.lno.gui.Action.addEdgeAction;
import static ru.kotb.lno.gui.Action.addVertexAction;


/**
 * The toolbar of the main window
 */
public class Toolbar extends JToolBar {

    /**
     * Constructs the toolbar. Each button is associated with appropriate Action
     */
    public Toolbar() {
        super("Tools");
        this.setFloatable(false);

        JButton btnAddVertex = new JButton(addVertexAction);
        JButton btnAddEdge = new JButton(addEdgeAction);

        this.add(btnAddVertex);
        this.add(btnAddEdge);
    }
}