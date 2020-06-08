package main.window.newxmlload;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.window.main.MainWindowController;
import transpool.logic.handler.LogicHandler;

import java.io.File;

public class newXmlLoadController {

    private LogicHandler logicHandler;
    private Stage primaryStage;
    private MainWindowController mainWindowController;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;

    @FXML
    private Button openFileButton;

    @FXML
    private Button LoadFileButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label FilePathLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    void onClickCancelButton(ActionEvent event) {

    }

    @FXML
    void onClickClearButton(ActionEvent event) {
        selectedFileProperty.set("");
        isFileSelected.set(false);
        progressBar.setProgress(0);
        progressIndicator.setProgress(0);
    }

    @FXML
    void onClickLoadFileButton(ActionEvent event) {
        logicHandler.collectMetadata(selectedFileProperty, this);
    }

    @FXML
    void onClickOpenFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.XML"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile == null) { return; }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);

    }

    public void setMainController(MainWindowController mainWindowController) { this.mainWindowController = mainWindowController;  }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setLogicHandler(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    public newXmlLoadController(){
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        FilePathLabel.textProperty().bind(selectedFileProperty);
        LoadFileButton.disableProperty().bind(isFileSelected.not());
        clearButton.disableProperty().bind(isFileSelected.not());
    }


    public void bindTaskToUIComponents(Task<Boolean> task) {
        // task progress bar
        progressBar.progressProperty().bind(task.progressProperty());

        //task progress Indicator
        progressIndicator.progressProperty().bind(task.progressProperty());
    }
}
