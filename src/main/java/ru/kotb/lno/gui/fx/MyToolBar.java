package ru.kotb.lno.gui.fx;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;


public class MyToolBar extends ToolBar {

    public MyToolBar() {
        Button addNodeBtn = new Button("Add node");
        Button addEdgeBtn = new Button("Add edge");
        this.getItems().addAll(addNodeBtn, addEdgeBtn);
    }
}
