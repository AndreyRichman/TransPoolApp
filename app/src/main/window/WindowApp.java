package main.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transpool.logic.handler.LogicHandler;
import transpool.ui.window.controller.WindowController;

import java.net.URL;


public class WindowApp extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

//        Parent load = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        URL resource = getClass().getResource("mainWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        WindowController controller = loader.getController();

        controller.setLogic(new LogicHandler());


        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
