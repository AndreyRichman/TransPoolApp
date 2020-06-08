package tasks.loadFile;

import exception.FaildLoadingXMLFileException;
import exception.InvalidMapBoundariesException;
import javafx.concurrent.Task;
import transpool.logic.handler.LogicHandler;
import transpool.logic.handler.XMLHandler;

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

        updateProgress(0,5);

        logicHandler.fetchFile(fileName);
        updateProgress(1,5);
        Thread.sleep(SLEEP_TIME);

        logicHandler.loadWorld();
        updateProgress(2,5);
        Thread.sleep(SLEEP_TIME);

        logicHandler.loadStations();
        updateProgress(3,5);
        Thread.sleep(SLEEP_TIME);

        logicHandler.loadRoads();
        updateProgress(4,5);
        Thread.sleep(SLEEP_TIME);

        logicHandler.loadRides();
        updateProgress(5,5);
        Thread.sleep(SLEEP_TIME);

        return Boolean.TRUE;

    }

    public loadXmlFileTask(String fileName, LogicHandler logicHandler ) {
        this.fileName = fileName;
        this.logicHandler = logicHandler;
    }
}

