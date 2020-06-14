package main.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transpool.logic.handler.LogicHandler;
import main.window.main.MainWindowController;

import java.net.URL;


public class WindowApp extends Application implements Runnable{


    @Override
    public void start(Stage primaryStage) throws Exception {

//        Parent load = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));

        // load main fxml
        URL resource = getClass().getResource("main/mainWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();

        // wire up controller
        MainWindowController controller = loader.getController();
        controller.setLogic(new LogicHandler());
        controller.setPrimaryStage(primaryStage);

        // set stage
        Scene scene = new Scene(root, 1050, 780);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void run(){
        javafx.application.Application.launch(WindowApp.class);
    }
}
