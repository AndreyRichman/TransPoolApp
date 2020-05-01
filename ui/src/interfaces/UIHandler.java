package interfaces;

import requests.classes.NewTrempRequest;
import requests.interfaces.UserRequest;

import java.util.List;

public interface UIHandler {

    void showOptions();
    void showOutput(String outputMsg);
    String getInput();
    UserRequest getRequestFromUser();
    public int showOptionsAndGetUserSelection(String titleForOptions, List<String> options);
    boolean getYesNoAnswerForQuestion(String question);
    int getNumberForString(String question);
    void showErrorMsg(String errorMsg);
    public NewTrempRequest getRequestForNewTremp(NewTrempRequest req);
}
