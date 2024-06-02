package ru.kotb.lno.gui.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kotb.lno.dto.SettingsDTO;
import ru.kotb.lno.gui.action.GraphEditActions;

import java.util.Optional;


public class OptimizeDialog {

    private final GraphEditActions actions;

    private SettingsDTO res;

    public OptimizeDialog(GraphEditActions actions) {
        this.actions = actions;
    }

    public Optional<SettingsDTO> invoke() {
        init();
        return Optional.ofNullable(res);
    }

    private void init() {
        ObservableList<String> nodes = FXCollections.observableArrayList(actions.getNodes());

        Stage stage = stageSettings();

        //TODO: fix ability to specify one node as the source and target
        ComboBox<String> startComboBox = new ComboBox<>(nodes);
        ComboBox<String> endComboBox = new ComboBox<>(nodes);

        RadioButton costBtn = new RadioButton("Cost");
        RadioButton timeBtn = new RadioButton("Time");

        ToggleGroup toggleGroup = new ToggleGroup();
        costBtn.setToggleGroup(toggleGroup);
        timeBtn.setToggleGroup(toggleGroup);

        Label sourceLabel = new Label("Source:");
        Label targetLabel = new Label("Target:");
        Label concessionLabel = new Label("Δ");

        TextField concessionTextField = new TextField();
        concessionTextField.setMaxWidth(50);

        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(60);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(60);

        setBtnActions(okBtn, concessionTextField, startComboBox, endComboBox, toggleGroup, costBtn, stage, cancelBtn);

        GridPane gridPane = getGridPane(sourceLabel, targetLabel, costBtn, concessionLabel, startComboBox, endComboBox, timeBtn, concessionTextField, okBtn, cancelBtn);

        Scene scene = new Scene(gridPane, 200, 150);
        stage.setScene(scene);

        stage.showAndWait();
    }

    private static GridPane getGridPane(
            Label sourceLabel, Label targetLabel, RadioButton costBtn,
            Label concessionLabel, ComboBox<String> startComboBox,
            ComboBox<String> endComboBox, RadioButton timeBtn,
            TextField concessionTextField,
            Button okBtn, Button cancelBtn) {

        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        gridPane.add(sourceLabel, 0, 0);
        gridPane.add(targetLabel, 0, 1);
        gridPane.add(costBtn, 0, 2);
        gridPane.add(concessionLabel, 0, 3);
        gridPane.add(okBtn, 0, 4);

        gridPane.setAlignment(Pos.CENTER);
        GridPane.setHalignment(concessionLabel, HPos.CENTER);

        gridPane.add(startComboBox, 1, 0);
        gridPane.add(endComboBox, 1, 1);
        gridPane.add(timeBtn, 1, 2);
        gridPane.add(concessionTextField, 1, 3);
        gridPane.add(cancelBtn, 1, 4);

        return gridPane;
    }

    private void setBtnActions(Button okBtn, TextField concessionTextField, ComboBox<String> startComboBox, ComboBox<String> endComboBox, ToggleGroup toggleGroup, RadioButton costBtn, Stage stage, Button cancelBtn) {
        okBtn.setOnAction(e -> {
            double concessionValue = Double.parseDouble(concessionTextField.getText());
            String source = startComboBox.getValue();
            String target = endComboBox.getValue();
            int mainCriteriaNum;
            RadioButton selectedToggle = (RadioButton) toggleGroup.getSelectedToggle();
            if (selectedToggle.equals(costBtn)) {
                mainCriteriaNum = 0;
            } else {
                mainCriteriaNum = 1;
            }

            res = new SettingsDTO(source, target, mainCriteriaNum, concessionValue);

            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });
    }

    private static Stage stageSettings() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Optimize");
        return stage;
    }
}
