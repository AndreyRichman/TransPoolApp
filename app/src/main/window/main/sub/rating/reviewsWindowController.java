package main.window.main.sub.rating;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import transpool.logic.user.Driver;

import java.util.List;

public class reviewsWindowController {

    @FXML
    private TextArea ReviewTextAreaValue;

    public void show(){
        System.out.println("Hii");
    }

    public void setReviewTextAreaValue(List<Driver.Rank> str) {
        for(Driver.Rank rank : str)
            ReviewTextAreaValue.appendText(String.format("stars: %d \ncomment:", rank.getStars()) + rank.getComment());
    }
}
