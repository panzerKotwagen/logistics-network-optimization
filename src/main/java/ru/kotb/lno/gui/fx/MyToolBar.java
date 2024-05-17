package ru.kotb.lno.gui.fx;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

import static ru.kotb.lno.gui.fx.GraphEditActions.addNode;


/**
 * The toolbar of the main window
 */
public class MyToolBar extends ToolBar {

    public MyToolBar() {
        Button addNodeBtn = new Button("Add node");
        Button addEdgeBtn = new Button("Add edge");

        addNodeBtn.setOnAction(actionEvent -> addNode());

        this.getItems().addAll(addNodeBtn, addEdgeBtn);
    }
}
