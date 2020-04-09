package classes;

import interfaces.UIHandler;

public class UI implements UIHandler {
    @Override
    public void showOptions() {

    }

    @Override
    public void ShowOutput(String outputMsg) {
        System.out.println(outputMsg);
    }

    @Override
    public String getInput() {
        return null;
    }
}
