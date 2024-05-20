package ru.kotb.lno.gui.fx;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;


/**
 * The toolbar of the main window
 */
public class MyToolBar extends ToolBar {

    public static Button addNodeBtn;

    public MyToolBar() {
        addNodeBtn = new Button("Add node");
        Button addEdgeBtn = new Button("Add edge");

        addEdgeBtn.setOnAction(actionEvent -> {
            GraphEditActions.addEdge();
        });

        addNodeBtn.setOnAction(actionEvent -> {
            addNodeBtn.setDisable(true);
            GraphEditActions.addNodeBtnIsPressed = true;
        });

        this.getItems().addAll(addNodeBtn, addEdgeBtn);
    }
}
