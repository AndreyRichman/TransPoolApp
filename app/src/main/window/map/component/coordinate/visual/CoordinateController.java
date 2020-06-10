package main.window.map.component.coordinate.visual;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.lang.reflect.Field;

public class CoordinateController {

    @FXML
    private VBox container;
    
    public void setLocation(int x, int y) {
        String value = x + " | " + y;
        Tooltip tooltip = new Tooltip(value);
        hackTooltipStartTiming(tooltip);
        Tooltip.install(container, tooltip);
    }

    private void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(20)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
