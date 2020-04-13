package interfaces;

import requests.interfaces.UserRequest;

public interface UIHandler {

    void showOptions();
    void showOutput(Object outputMsg);
    String getInput();
    UserRequest getRequestFromUser();
}
