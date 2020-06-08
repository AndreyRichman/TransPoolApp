package tasks.loadFile;

import javafx.concurrent.Task;
import transpool.logic.handler.LogicHandler;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class loadXmlFileTask extends Task<Boolean> {

    private String fileName;
    private LogicHandler logicHandler;
    private final int SLEEP_TIME = 5;


    @Override
    protected Boolean call() throws Exception {

        updateProgress(0,1);

        logicHandler.loadXMLFile(fileName);

        updateProgress(0,1);

        return Boolean.TRUE;

    }

    public loadXmlFileTask(String fileName, LogicHandler logicHandler ) {
        this.fileName = fileName;
        this.logicHandler = logicHandler;
    }





}
