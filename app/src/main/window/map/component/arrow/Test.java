package main.window.map.component.arrow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ArrowShape arrow = new ArrowShape(100, 100, 20, 20);
        Pane root = new Pane();
        root.getChildren().add(arrow);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
