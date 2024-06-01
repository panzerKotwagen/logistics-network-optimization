package ru.kotb.lno.gui.fx.node;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import ru.kotb.lno.gui.fx.action.GraphEditActions;


/**
 * The toolbar of the main window
 */
public class MyToolBar extends ToolBar {

    public static Button addNodeBtn;

    public static Button deleteNodeBtn;

    private final GraphEditActions actions;

    public MyToolBar(GraphEditActions actions) {
        this.actions = actions;
        addNodeBtn = new Button("Add node");
        Button addEdgeBtn = new Button("Add edge");
        deleteNodeBtn = new Button("Remove node");
        Button deleteEdgeBtn = new Button("Edit edge");
        Button optimizeBtn = new Button("Optimize");
        Button update = new Button("Update");
        Button clear = new Button("Clear");

        optimizeBtn.setOnAction((e) -> actions.optimize());

        addEdgeBtn.setOnAction(actionEvent -> {
            actions.addEdge();
        });

        addNodeBtn.setOnAction(actionEvent -> {
            addNodeBtn.setDisable(true);
        });

        deleteNodeBtn.setOnAction(actionEvent -> {
            actions.removeNode();
        });

        clear.setOnAction(actionEvent -> {
            actions.clear();
        });

        update.setOnAction(actionEvent -> {
            actions.update();
        });

        this.getItems().addAll(addNodeBtn, addEdgeBtn, deleteNodeBtn, deleteEdgeBtn, optimizeBtn, update, clear);
    }
}
