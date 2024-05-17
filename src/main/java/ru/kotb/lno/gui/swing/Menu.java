package ru.kotb.lno.gui.swing;


import javax.swing.*;

import static ru.kotb.lno.gui.swing.Action.addEdgeAction;
import static ru.kotb.lno.gui.swing.Action.addVertexAction;


/**
 * The application menu
 */
public class Menu extends JMenuBar {

    public Menu() {
        makeFileMenu();
        makeHelpMenu();
    }

    /**
     * Makes submenu File of the menu bar. Each menu item is
     * associated with appropriate FileAction
     */
    private void makeFileMenu() {
        JMenu menuFile = new JMenu("Graph");

        JMenuItem mItemAddVertex = new JMenuItem(addVertexAction);
        JMenuItem mItemAddEdge = new JMenuItem(addEdgeAction);

        menuFile.add(mItemAddVertex);
        menuFile.addSeparator();
        menuFile.add(mItemAddEdge);

        this.add(menuFile);
    }

    /**
     * Makes submenu Help of the menu bar
     */
    private void makeHelpMenu() {
        JMenu menuHelp = new JMenu("Help");
        JMenuItem mItemAbout = new JMenuItem("About");
        menuHelp.add(mItemAbout);

        this.add(menuHelp);
    }
}
