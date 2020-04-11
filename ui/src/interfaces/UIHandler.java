package interfaces;

import requests.interfaces.UserRequest;

public interface UIHandler {

    void showOptions();
    void showOutput(String outputMsg);
    String getInput();
    UserRequest getRequestFromUser();
}
