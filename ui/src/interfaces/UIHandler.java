package interfaces;

import requests.classes.NewTrempRequest;
import requests.interfaces.UserRequest;

import java.time.LocalTime;
import java.util.List;

public interface UIHandler {

    void showOptions();
    void showOutput(String outputMsg);
    String getInput();
    UserRequest getRequestFromUser();
    public int showOptionsAndGetUserSelection(String titleForOptions, List<String> options);
    boolean getYesNoAnswerForQuestion(String question);
    int getNumberForString(String question);
    String getStringForQuestion(String question);
    void showErrorMsg(String errorMsg);
    LocalTime getTimeFromUser(String question);
    public void showTitle(String title);
}
