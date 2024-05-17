package ru.kotb.lno.gui.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import ru.kotb.lno.dto.EdgeDTO;

import java.util.Optional;


/**
 * The toolbar of the main window
 */
public class MyToolBar extends ToolBar {

    public static Button addNodeBtn;

    public MyToolBar() {
        addNodeBtn = new Button("Add node");
        Button addEdgeBtn = new Button("Add edge");

        addEdgeBtn.setOnAction(actionEvent -> {
            Optional<EdgeDTO> res = new AddEdgeDialog().invoke();
            if (res.isPresent()) {
                EdgeDTO edge = res.get();
                GraphEditActions.addEdge(edge);
            }
        });
        addNodeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addNodeBtn.setDisable(true);
                GraphEditActions.addNodeBtnIsPressed = true;
            }
        });

        this.getItems().addAll(addNodeBtn, addEdgeBtn);
    }
}
