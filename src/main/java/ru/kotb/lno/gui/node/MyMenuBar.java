package ru.kotb.lno.gui.node;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import ru.kotb.lno.gui.action.GraphEditActions;


/**
 * The application menu
 */
public class MyMenuBar extends MenuBar {

    private final GraphEditActions graphEditActions;

    public MyMenuBar(GraphEditActions graphEditActions) {
        this.graphEditActions = graphEditActions;
        Menu menu = new Menu("Graph");

        MenuItem m1 = new MenuItem("Add node");
        MenuItem m2 = new MenuItem("Add edge");

        menu.getItems().add(m1);
        menu.getItems().add(m2);

        this.getMenus().add(menu);
    }
}
