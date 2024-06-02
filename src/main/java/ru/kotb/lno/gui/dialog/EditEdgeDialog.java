package ru.kotb.lno.gui.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.kotb.lno.graph.components.Edge;
import ru.kotb.lno.gui.action.GraphEditActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EditEdgeDialog {

    private final GraphEditActions actions;

    ObservableList<Edge> nodes;

    private Data data;

    public EditEdgeDialog(GraphEditActions actions) {
        this.actions = actions;
    }

    private static FlowPane constructFlowPane(GridPane gridLayout, Button okBtn, Button removeBtn, Button cancelBtn) {
        FlowPane flowLayout = new FlowPane();
        flowLayout.setAlignment(Pos.CENTER);
        flowLayout.setHgap(5);

        flowLayout.getChildren().add(gridLayout);
        flowLayout.getChildren().add(okBtn);
        flowLayout.getChildren().add(removeBtn);
        flowLayout.getChildren().add(cancelBtn);
        return flowLayout;
    }

    private static GridPane constructGridPane(Label edgeLabel, Label w1Label, Label w2Label, ComboBox<Edge> edgeComboBox, TextField w1TextField, TextField w2TextField) {
        GridPane gridLayout = new GridPane();

        gridLayout.setPadding(new Insets(10, 10, 10, 10));
        gridLayout.setVgap(5);
        gridLayout.setHgap(5);

        gridLayout.add(edgeLabel, 0, 0);
        gridLayout.add(w1Label, 0, 1);
        gridLayout.add(w2Label, 0, 2);

        gridLayout.add(edgeComboBox, 1, 0);
        gridLayout.add(w1TextField, 1, 1);
        gridLayout.add(w2TextField, 1, 2);
        return gridLayout;
    }

    private static Stage stageSettings() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("New edge");
        return stage;
    }

    public Optional<Data> invoke() {
        init();
        return Optional.ofNullable(data);
    }

    private void init() {
        Stage stage = stageSettings();

        List<Edge> infoEdgeList = new ArrayList<>(actions.getEdgeToInfoEdgeMap().keySet());
        nodes = FXCollections.observableArrayList(infoEdgeList);
        ComboBox<Edge> edgeComboBox = new ComboBox<>(nodes);

        Label edgeLabel = new Label("Edge:");
        Label w1Label = new Label("Weight 1:");
        Label w2Label = new Label("Weight 2:");
        TextField w1TextField = new TextField();
        TextField w2TextField = new TextField();

        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(60);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(60);
        Button removeBtn = new Button("Remove");
        removeBtn.setPrefWidth(60);

        setBtnActions(okBtn, w1TextField, w2TextField, edgeComboBox, stage, cancelBtn, removeBtn);

        GridPane gridLayout = constructGridPane(edgeLabel, w1Label, w2Label, edgeComboBox, w1TextField, w2TextField);

        FlowPane flowLayout = constructFlowPane(gridLayout, okBtn, removeBtn, cancelBtn);

        Scene scene = new Scene(flowLayout, 240, 150);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void setBtnActions(Button okBtn, TextField w1TextField, TextField w2TextField, ComboBox<Edge> edgeComboBox, Stage stage, Button cancelBtn, Button removeBtn) {
        okBtn.setOnAction(e -> {
            double w1 = Double.parseDouble(w1TextField.getText());
            double w2 = Double.parseDouble(w2TextField.getText());

            data = new Data(edgeComboBox.getValue(), w1, w2);

            stage.close();
        });

        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        removeBtn.setOnAction(e -> {
            Edge selectedEdge = edgeComboBox.getValue();
            actions.removeEdge(selectedEdge);
            nodes.remove(selectedEdge);
            edgeComboBox.setValue(null);
        });
    }


    @Getter
    @RequiredArgsConstructor
    public static class Data {

        private final Edge edge;

        private final double w1;

        private final double w2;
    }
}
