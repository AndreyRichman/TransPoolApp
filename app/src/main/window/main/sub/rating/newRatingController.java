package main.window.main.sub.rating;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.window.main.sub.tremp.TrempSubWindowController;
import org.controlsfx.control.Rating;
import transpool.logic.handler.LogicHandler;
import transpool.logic.traffic.item.TrempRequest;
import transpool.logic.user.Driver;
import transpool.logic.user.User;

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
        TrempRequest trempRequest = this.mainController.getSelectedTrempRequest();
        int score = (int) ratingValue.getRating();
        User user = trempRequest.getUser();
        Driver.Rank rank = new Driver.Rank(user, score);

        if(!this.commentValue.getText().equals(""))
            rank.setComment(this.commentValue.getText());

        trempRequest.setRank(rank);
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

