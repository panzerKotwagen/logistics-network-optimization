package ru.kotb.lno.gui.fx;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import static ru.kotb.lno.gui.fx.GraphEditActions.addNode;


/**
 * The application menu
 */
public class MyMenuBar extends MenuBar {

    public MyMenuBar() {
        Menu menu = new Menu("Graph");

        MenuItem m1 = new MenuItem("Add node");
        MenuItem m2 = new MenuItem("Add edge");

        menu.getItems().add(m1);
        menu.getItems().add(m2);

        m1.setOnAction(actionEvent -> addNode());

        this.getMenus().add(menu);
    }
}
