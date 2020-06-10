package main;

import main.console.ConsoleApp;
import main.window.WindowApp;

public class Main {
    public static void main(String[] args) {
        Runnable app = new WindowApp(); //ConsoleApp();
        app.run();
    }
}
