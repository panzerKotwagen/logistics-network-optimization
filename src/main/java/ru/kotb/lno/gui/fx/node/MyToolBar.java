package ru.kotb.lno.gui.fx.node;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.gui.fx.action.GraphEditActions;


/**
 * The toolbar of the main window
 */
public class MyToolBar extends ToolBar {

    public static Button addNodeBtn;

    public static DrawPane drawPane;

    public MyToolBar() {
        addNodeBtn = new Button("Add node");
        Button addEdgeBtn = new Button("Add edge");

        addEdgeBtn.setOnAction(actionEvent -> {
            EdgeDTO edgeDTO = GraphEditActions.addEdge();
            assert edgeDTO != null;
            DrawPane.InfoNode source = drawPane.getInfoNode(edgeDTO.getSource());
            DrawPane.InfoNode target = drawPane.getInfoNode(edgeDTO.getTarget());
            drawPane.buildAndAddLine(source, target);
        });

        addNodeBtn.setOnAction(actionEvent -> {
            addNodeBtn.setDisable(true);
            GraphEditActions.addNodeBtnIsPressed = true;
        });

        this.getItems().addAll(addNodeBtn, addEdgeBtn);
    }
}
