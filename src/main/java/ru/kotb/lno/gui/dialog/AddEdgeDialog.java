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
import ru.kotb.lno.dto.EdgeDTO;
import ru.kotb.lno.gui.action.GraphEditActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * The dialog window for entering edge information
 */
public class AddEdgeDialog {

    private final GraphEditActions actions;

    private EdgeDTO inputtedEdge;

    public AddEdgeDialog(GraphEditActions actions) {
        this.actions = actions;
    }

    private static FlowPane getFlowPane(Label nameLabel, GridPane gridPane, Button okBtn, Button cancelBtn) {
        FlowPane flowPane = new FlowPane();

        flowPane.getChildren().add(nameLabel);
        flowPane.getChildren().add(gridPane);
        flowPane.getChildren().add(okBtn);
        flowPane.getChildren().add(cancelBtn);
        flowPane.setHgap(20);
        flowPane.setAlignment(Pos.CENTER);
        return flowPane;
    }

    private static GridPane getGridPane(Label sourceLabel, Label targetLabel, ComboBox<String> sourceComboBox, ComboBox<String> targetComboBox, Label w1Label, Label w2Label, TextField w1TextField, TextField w2TextField) {
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(sourceLabel, 0, 1);
        gridPane.add(targetLabel, 0, 2);
        gridPane.add(sourceComboBox, 1, 1);
        gridPane.add(targetComboBox, 1, 2);
        gridPane.add(w1Label, 0, 3);
        gridPane.add(w2Label, 0, 4);
        gridPane.add(w1TextField, 1, 3);
        gridPane.add(w2TextField, 1, 4);
        return gridPane;
    }

    private static Stage stageSettings() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("New edge");
        return stage;
    }

    public Optional<EdgeDTO> invoke() {
        init();
        return Optional.ofNullable(inputtedEdge);
    }

    private void init() {
        List<String> nodeList = new ArrayList<>(actions.getNodes());
        nodeList = nodeList.stream()
                .sorted()
                .toList();

        ObservableList<String> nodes = FXCollections.observableArrayList(nodeList);

        Stage stage = stageSettings();

        //TODO: fix ability to specify one node as the source and target
        ComboBox<String> sourceComboBox = new ComboBox<>(nodes);
        ComboBox<String> targetComboBox = new ComboBox<>(nodes);

        Label nameLabel = new Label("Provide edge info");
        Label sourceLabel = new Label("Source:");
        Label targetLabel = new Label("Target:");
        Label w1Label = new Label("Weight 1:");
        Label w2Label = new Label("Weight 2:");
        TextField w1TextField = new TextField();
        TextField w2TextField = new TextField();

        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(60);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(60);

        setBtnActions(okBtn, w1TextField, w2TextField, sourceComboBox, targetComboBox, stage, cancelBtn);

        GridPane gridPane = getGridPane(sourceLabel, targetLabel, sourceComboBox, targetComboBox, w1Label, w2Label, w1TextField, w2TextField);

        FlowPane flowPane = getFlowPane(nameLabel, gridPane, okBtn, cancelBtn);

        Scene scene = new Scene(flowPane, 240, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void setBtnActions(Button okBtn, TextField w1TextField, TextField w2TextField, ComboBox<String> sourceComboBox, ComboBox<String> targetComboBox, Stage stage, Button cancelBtn) {
        okBtn.setOnAction(e -> {
            double w1 = Double.parseDouble(w1TextField.getText());
            double w2 = Double.parseDouble(w2TextField.getText());
            String source = sourceComboBox.getValue();
            String target = targetComboBox.getValue();
            inputtedEdge = new EdgeDTO(source, target, w1, w2);
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });
    }
}
