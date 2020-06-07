package main.window.newxmlload;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.window.main.MainWindowController;

import java.io.File;

public class newXmlLoadController {

    private MainWindowController mainWindowController;
    private Stage primaryStage;

    @FXML
    private Button openFileButton;

    @FXML
    private Button LoadFileButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label FilePathLabel;

    @FXML
    private Label progressPercentLabel;

    @FXML
    void onClickCancelButton(ActionEvent event) {

    }

    @FXML
    void onClickLoadFileButton(ActionEvent event) {

    }

    @FXML
    void onClickOpenFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.XML"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        FilePathLabel.setText(absolutePath);

    }

    public void setMainController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}
