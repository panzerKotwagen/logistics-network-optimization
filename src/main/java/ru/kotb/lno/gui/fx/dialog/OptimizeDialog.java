package ru.kotb.lno.gui.fx.dialog;

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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.kotb.lno.gui.fx.action.GraphEditActions;


public class OptimizeDialog {

    private final GraphEditActions actions;

    public OptimizeDialog(GraphEditActions actions) {
        this.actions = actions;
    }

    public void init() {
        ObservableList<String> nodes = FXCollections.observableArrayList(actions.getNodes());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("New edge");

        //TODO: fix ability to specify one node as the source and target
        ComboBox<String> startComboBox = new ComboBox<>(nodes);
        ComboBox<String> endComboBox = new ComboBox<>(nodes);

        RadioButton costBtn = new RadioButton("Cost");
        RadioButton timeBtn = new RadioButton("Time");

        ToggleGroup toggleGroup = new ToggleGroup();
        costBtn.setToggleGroup(toggleGroup);
        timeBtn.setToggleGroup(toggleGroup);

        Label nameLabel = new Label("Optimal path");
        Label sourceLabel = new Label("Start:");
        Label targetLabel = new Label("End:");
        Label mainCriteriaLabel = new Label("Main criteria");
        Label concessionLabel = new Label("Δ");

        TextField concessionTextField = new TextField();

        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(60);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(60);

        okBtn.setOnAction(e -> {
//            int w1 = Integer.parseInt(w1TextField.getText());
//            int w2 = Integer.parseInt(w2TextField.getText());
//            String source = startComboBox.getValue();
//            String target = mainCriteriaComboBox.getValue();
//            inputtedEdge = new EdgeDTO(source, target, w1, w2);
//            stage.close();
        });
        cancelBtn.setOnAction(e -> {
            stage.close();
        });

        GridPane layout = new GridPane();
        FlowPane flowPane = new FlowPane();

        // определения столбцов
//        ColumnConstraints column1 = new ColumnConstraints();
//        column1.setPercentWidth(30);
//        layout.getColumnConstraints().add(column1);
//
//        ColumnConstraints column2 = new ColumnConstraints();
//        column2.setPercentWidth(40);
//        layout.getColumnConstraints().add(column2);

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

        layout.add(sourceLabel, 0, 0);
        layout.add(targetLabel, 0, 1);
        layout.add(mainCriteriaLabel, 0, 2);
        layout.add(concessionLabel, 0, 4);
        GridPane.setHalignment(concessionLabel, HPos.RIGHT);

        layout.add(startComboBox, 1, 0);
        layout.add(endComboBox, 1, 1);
        layout.add(costBtn, 1, 2);
        layout.add(timeBtn, 1, 3);
        layout.add(concessionTextField, 1, 4);

        flowPane.getChildren().add(nameLabel);
        flowPane.getChildren().add(layout);
        flowPane.getChildren().add(okBtn);
        flowPane.getChildren().add(cancelBtn);
        flowPane.setHgap(20);
        flowPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(flowPane, 240, 200);
        stage.setTitle("Optimize");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
