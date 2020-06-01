package main;

import main.console.ConsoleApp;
import main.window.WindowApp;

public class Main {
    public static void main(String[] args) {
        Runnable app = new ConsoleApp(); //WindowApp();
        app.run();
    }
}
