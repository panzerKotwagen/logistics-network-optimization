package ru.kotb.lno.gui.fx.dialog;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class OptimizeDialog {

    public void init() {
        Stage stage = new Stage();

        FlowPane flowPane = new FlowPane();


        TextField w1TextField = new TextField();
        TextField w2TextField = new TextField();
        TextField w3TextField = new TextField();
        TextField w4TextField = new TextField();

        GridPane layout = new GridPane();

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(5);
        layout.setHgap(5);

//        layout.add(nameLabel, 1, 0, 2, 1);
        for (int i = 0; i < 4; i++) {
            layout.add(new Label("\u0394" + i), i, 0);
        }
        layout.add(w1TextField, 0, 1);
        layout.add(w2TextField, 1, 1);
        layout.add(w3TextField, 2, 1);
        layout.add(w4TextField, 3, 1);

//        flowPane.getChildren().add(w1TextField);
//        flowPane.getChildren().add(w2TextField);
//        flowPane.getChildren().add(w3TextField);
//        flowPane.getChildren().add(w4TextField);

        Button okBtn = new Button("OK");
        okBtn.setPrefWidth(60);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(60);

//        flowPane.getChildren().add(layout);
        flowPane.getChildren().add(okBtn);
        flowPane.getChildren().add(cancelBtn);

        VBox hbox = new VBox(layout, flowPane);

        flowPane.setHgap(20);
        flowPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(hbox, 240, 150);
        stage.setTitle("Compromise scheme");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
