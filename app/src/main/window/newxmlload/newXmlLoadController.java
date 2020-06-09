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
import transpool.ui.request.type.LoadXMLRequest;

import java.io.File;

public class newXmlLoadController {

    private LogicHandler logicHandler;
    private Stage stage;
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
        stage.close();
    }

    @FXML
    void onClickClearButton(ActionEvent event) {
        //clear buttons & labels
        selectedFileProperty.set("");
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        progressIndicator.progressProperty().unbind();
        progressIndicator.setProgress(0);

        //disable Load & clear
        isFileSelected.set(false);

        //clear old metadata
        logicHandler.cleanOldResults();
    }

    @FXML
    void onClickLoadFileButton(ActionEvent event) {
//        not used due to progress bar load. might be used in future, right now its working with bypass.
//        LoadXMLRequest req = new LoadXMLRequest();
//        req.setFileDirectory(selectedFileProperty.get());
        logicHandler.collectMetadata(selectedFileProperty, this);
    }

    @FXML
    void onClickOpenFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.XML"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) { return; }

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);

    }

    public void setMainController(MainWindowController mainWindowController) { this.mainWindowController = mainWindowController;  }

    public void setStage(Stage stage) {
        this.stage = stage;
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
