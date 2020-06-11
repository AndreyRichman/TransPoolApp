package main.window.main;

import exception.FaildLoadingXMLFileException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.window.main.sub.ride.RideSubWindowController;
import main.window.main.sub.ride.TrempSubWindowController;
import main.window.newtremp.CreateTrempController;
import main.window.newxmlload.newXmlLoadController;
import transpool.logic.handler.LogicHandler;
import java.io.IOException;
import java.net.URL;


public class MainWindowController {

    private LogicHandler logicHandler;
    private Stage primaryStage;

    @FXML private Pane rideComponent;
    @FXML private Pane trempComponent;
    @FXML private RideSubWindowController rideComponentController;
    @FXML private TrempSubWindowController trempComponentController;

    @FXML
    private Button loadXmlBtn;

    @FXML
    private Button newBtn;

    @FXML
    private Button newBtn1;

    @FXML
    private Button matchBtn;

    @FXML
    private Button matchBtn1;

    @FXML
    void onLoadXmlBtnClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        URL resource = getClass().getResource("../newxmlload/newXmlLoadWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        newXmlLoadController controller = loader.getController();
        controller.setMainController(this);
        controller.setStage(stage);
        controller.setLogicHandler(logicHandler);
        Scene scene = new Scene(root, 400, 390);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onMatchBtnClick(ActionEvent event) {

    }

    @FXML
    void onNewRideBtnClick(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        URL resource = getClass().getResource("../newride/newRideWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        CreateTrempController controller = loader.getController();
        controller.setMainController(this);
        controller.setStage(stage);
        controller.setLogicHandler(logicHandler);
        Scene scene = new Scene(root, 400, 390);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onQuitBtnClick(ActionEvent event) {
        primaryStage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setLogic(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    @FXML
    public void initialize(){
        if (rideComponentController != null && this.trempComponentController != null){
            this.rideComponentController.setMainController(this);
            this.trempComponentController.setMainController(this);
        }
    }

    @FXML
    void onNewTrempBtnClick(ActionEvent event) throws IOException, FaildLoadingXMLFileException {
        Stage stage = new Stage();
        stage.setResizable(false);
        URL resource = getClass().getResource("../newtremp/newTrempWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        CreateTrempController controller = loader.getController();
        controller.setMainController(this);
        controller.setStage(stage);
        controller.setLogicHandler(logicHandler);
        Scene scene = new Scene(root, 400, 390);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }


}
