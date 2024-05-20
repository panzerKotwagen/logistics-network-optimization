package ru.kotb.lno.gui.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kotb.lno.gui.fx.node.DrawPane;
import ru.kotb.lno.gui.fx.node.MyMenuBar;
import ru.kotb.lno.gui.fx.node.MyToolBar;


/**
 * The class that main window of the application
 */
public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private void drawLines(GraphicsContext gc) {

        gc.beginPath();
        gc.moveTo(30.5, 30.5);
        gc.lineTo(150.5, 30.5);
        gc.lineTo(150.5, 150.5);
        gc.lineTo(30.5, 30.5);
        gc.stroke();
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
        DrawPane drawPane = new DrawPane();
        MyToolBar.drawPane = drawPane;
        vb.getChildren().add(drawPane);

        Scene scene = new Scene(vb);
        stage.setScene(scene);

        stage.show();
    }
}
