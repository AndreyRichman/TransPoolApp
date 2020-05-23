package transpool.ui.window.controller;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.text.Text;
        import transpool.logic.handler.LogicHandler;

public class WindowController {

    LogicHandler logicHandler;

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
    private AnchorPane ridesScrollPane;

    @FXML
    private AnchorPane trempsScrollPane;

    @FXML
    private Text rideUserTextField;

    @FXML
    private Text trempUserTextField;

    @FXML
    private Button showRideDetailsBtn;

    @FXML
    private Button showTrempDetailsBtn;

    @FXML
    void onLoadXmlBtnClick(ActionEvent event) {

    }

    @FXML
    void onMatchBtnClick(ActionEvent event) {

    }

    @FXML
    void onNewRideBtnClick(ActionEvent event) {

    }

    @FXML
    void onNewTrempBtnClick(ActionEvent event) {

    }

    @FXML
    void onQuitBtnClick(ActionEvent event) {

    }

    @FXML
    void onRidesScrollSelected(MouseEvent event) {

    }

    @FXML
    void onShowRideDetailsBtnClick(ActionEvent event) {

    }

    @FXML
    void onShowTrempDetailsBtnClick(ActionEvent event) {

    }

    @FXML
    void onTrempScrollClicked(MouseEvent event) {

    }

    public void setLogic(LogicHandler logicHandler){
        this.logicHandler = logicHandler;
    }

}

