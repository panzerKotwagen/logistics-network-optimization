package ru.kotb.lno.gui.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
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

import java.util.Optional;


/**
 * The dialog window for entering edge information
 */
public class AddEdgeDialog {

    private final GraphEditActions actions;

    public AddEdgeDialog(GraphEditActions actions) {
        this.actions = actions;
    }

    private EdgeDTO inputtedEdge;

    public Optional<EdgeDTO> invoke() {
        init();
        return Optional.ofNullable(inputtedEdge);
    }

    private void init() {
        ObservableList<String> nodes = FXCollections.observableArrayList(actions.getNodes());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("New edge");

        //TODO: fix ability to specify one node as the source and target
        ComboBox<String> sourceComboBox = new ComboBox<>(nodes);
        ComboBox<String> targetComboBox = new ComboBox<>(nodes);

        Group group = new Group();

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

        GridPane layout = new GridPane();
        FlowPane flowPane = new FlowPane();

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

//        layout.add(nameLabel, 1, 0, 2, 1);
        layout.add(sourceLabel, 0, 1);
        layout.add(targetLabel, 0, 2);
        layout.add(sourceComboBox, 1, 1);
        layout.add(targetComboBox, 1, 2);
        layout.add(w1Label, 0, 3);
        layout.add(w2Label, 0, 4);
        layout.add(w1TextField, 1, 3);
        layout.add(w2TextField, 1, 4);

        flowPane.getChildren().add(nameLabel);
        flowPane.getChildren().add(layout);
        flowPane.getChildren().add(okBtn);
        flowPane.getChildren().add(cancelBtn);
        flowPane.setHgap(20);
        flowPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(flowPane, 240, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
