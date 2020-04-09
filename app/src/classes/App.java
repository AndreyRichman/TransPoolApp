package classes;

import interfaces.UIHandler;

public class App {
    UIHandler uiHandler;
    public App() {
        uiHandler = new UI();
        uiHandler.ShowOutput("App created successfully!");
    }
    public void run(){
        uiHandler.ShowOutput("App is Running!");
    }
}
