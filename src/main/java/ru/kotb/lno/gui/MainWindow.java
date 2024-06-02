package ru.kotb.lno.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kotb.lno.gui.action.GraphEditActions;
import ru.kotb.lno.gui.node.DrawPane;
import ru.kotb.lno.gui.node.MyToolBar;


/**
 * The class that main window of the application
 */
public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        settings(stage);

        VBox vb = new VBox();

        DrawPane drawPane = new DrawPane();
        GraphEditActions actions = new GraphEditActions(drawPane);

        MyToolBar toolBar = new MyToolBar(actions);

        vb.getChildren().add(toolBar);

        vb.getChildren().add(drawPane);

        Scene scene = new Scene(vb);
        stage.setScene(scene);

        stage.show();
    }

    private static void settings(Stage stage) {
        stage.setTitle("logistics-network-optimization");
        stage.setWidth(900);
        stage.setHeight(600);
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}
