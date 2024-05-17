package ru.kotb.lno.gui.fx;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


public class MyMenuBar extends MenuBar {

    public MyMenuBar() {
        Menu menu = new Menu("Graph");

        MenuItem m1 = new MenuItem("Add node");
        MenuItem m2 = new MenuItem("Add edge");

        menu.getItems().add(m1);
        menu.getItems().add(m2);

        this.getMenus().add(menu);
    }
}
