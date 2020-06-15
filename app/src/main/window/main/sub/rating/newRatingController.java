package main.window.main.sub.rating;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.window.main.sub.tremp.TrempSubWindowController;
import org.controlsfx.control.Rating;
import transpool.logic.handler.LogicHandler;

public class newRatingController {

    private TrempSubWindowController mainController;
    private LogicHandler logicHandler;
    private Stage stage;

    @FXML
    private Rating ratingValue;

    @FXML
    private TextField commentValue;

    public Rating getRatingValue() {
        return ratingValue;
    }

    @FXML
    void onClickSubmit(ActionEvent event) {
        this.stage.close();
    }


    public TextField getCommentValue() {
        return commentValue;
    }

    public void setLogicHandler(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(TrempSubWindowController mainController) { this.mainController = mainController;  }
}

