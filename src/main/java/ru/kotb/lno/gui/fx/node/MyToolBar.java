package ru.kotb.lno.gui.fx.node;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.gui.fx.action.GraphEditActions;
import ru.kotb.lno.gui.fx.dialog.OptimizeDialog;


/**
 * The toolbar of the main window
 */
public class MyToolBar extends ToolBar {

    public static Button addNodeBtn;

    public static DrawPane drawPane;

    public MyToolBar() {
        addNodeBtn = new Button("Add node");
        Button addEdgeBtn = new Button("Add edge");
        Button shortestW1Btn = new Button("Add edge");
        Button shortestW2Btn = new Button("Add edge");
        Button deleteNodeBtn = new Button("Remove node");
        Button deleteEdgeBtn = new Button("Remove edge");
        Button optimizeBtn = new Button("Optimize");

        optimizeBtn.setOnAction((e) -> new OptimizeDialog().init());

        ObservableList<String> nodes = FXCollections.observableArrayList("Cost", "Time");
        ComboBox<String> criteriaComboBOx = new ComboBox<>(nodes);

        addEdgeBtn.setOnAction(actionEvent -> {
            EdgeDTO edgeDTO = GraphEditActions.addEdge();
            assert edgeDTO != null;
            DrawPane.InfoNode source = drawPane.getInfoNode(edgeDTO.getSource());
            DrawPane.InfoNode target = drawPane.getInfoNode(edgeDTO.getTarget());
            drawPane.buildAndAddLine(source, target, edgeDTO.getWeight1(), edgeDTO.getWeight2());
        });

        addNodeBtn.setOnAction(actionEvent -> {
            addNodeBtn.setDisable(true);
            GraphEditActions.addNodeBtnIsPressed = true;
        });

        this.getItems().addAll(addNodeBtn, addEdgeBtn, deleteNodeBtn, deleteEdgeBtn, criteriaComboBOx, optimizeBtn);
    }
}
