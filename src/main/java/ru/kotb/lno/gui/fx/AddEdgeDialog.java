package ru.kotb.lno.gui.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kotb.lno.dto.EdgeDTO;

import java.util.Optional;


/**
 * The dialog window for entering edge information
 */
public class AddEdgeDialog {

    //     = FXCollections.observableArrayList();
    private EdgeDTO inputtedEdge;

    public Optional<EdgeDTO> invoke() {
        init();
        return Optional.ofNullable(inputtedEdge);
    }

    private void init() {
        ObservableList<String> nodes = FXCollections.observableArrayList(GraphEditActions.getNodes());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);

        ComboBox<String> sourceComboBox = new ComboBox<>(nodes);
        ComboBox<String> targetComboBox = new ComboBox<>(nodes);

        Label nameLabel = new Label("New edge");
        Label sourceLabel = new Label("Source:");
        Label targetLabel = new Label("Target:");
        Label w1Label = new Label("Weight 1:");
        Label w2Label = new Label("Weight 2:");
        TextField w1TextField = new TextField();
        TextField w2TextField = new TextField();

        Button okBtn = new Button("OK");
        Button cancelBtn = new Button("Cancel");

        okBtn.setOnAction(e -> {
            int w1 = Integer.parseInt(w1TextField.getText());
            int w2 = Integer.parseInt(w2TextField.getText());
            String source = sourceComboBox.getValue();
            String target = targetComboBox.getValue();
            inputtedEdge = new EdgeDTO(source, target, w1, w2);
            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        GridPane layout = new GridPane();

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        layout.add(nameLabel, 1, 0, 2, 1);
        layout.add(sourceLabel, 0, 1);
        layout.add(targetLabel, 0, 2);
        layout.add(sourceComboBox, 1, 1);
        layout.add(targetComboBox, 1, 2);
        layout.add(w1Label, 0, 3);
        layout.add(w2Label, 0, 4);
        layout.add(w1TextField, 1, 3);
        layout.add(w2TextField, 1, 4);
        layout.add(okBtn, 0, 5);
        layout.add(cancelBtn, 1, 5);

        Scene scene = new Scene(layout, 240, 200);
        stage.setTitle("Dialog");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
