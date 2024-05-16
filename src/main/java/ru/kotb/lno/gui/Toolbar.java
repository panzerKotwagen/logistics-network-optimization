package ru.kotb.lno.gui;

import javax.swing.*;


public class Toolbar extends JToolBar {
    /**
     * Makes the toolbar. Each button is associated with appropriate
     * Action.
     */
    public Toolbar() {
        super("Tools");
        this.setFloatable(false);

        JButton btnAddVertex = new JButton("Add vertex");
        JButton btnAddEdge = new JButton("Add edge");

        this.add(btnAddVertex);
        this.add(btnAddEdge);
    }
}