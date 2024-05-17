package ru.kotb.lno.gui.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The class that main window of the application
 */
public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("logistics-network-optimization");
        stage.setWidth(900);
        stage.setHeight(600);
        stage.centerOnScreen();

        VBox vb = new VBox();

        vb.getChildren().add(new MyMenuBar());
        vb.getChildren().add(new MyToolBar());

        Scene scene = new Scene(vb);
        stage.setScene(scene);

        stage.show();
    }
}
