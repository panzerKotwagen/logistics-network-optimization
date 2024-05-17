package ru.kotb.lno.gui.fx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("logistics-network-optimization");
        stage.setWidth(900);
        stage.setHeight(600);
        stage.centerOnScreen();

        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }
}
