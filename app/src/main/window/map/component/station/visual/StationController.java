package main.window.map.component.station.visual;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.window.map.component.details.StationDetailsDTO;
import main.window.map.component.details.visual.StationDetailsController;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

public class StationController {

    private Supplier<StationDetailsDTO> detailsDTOSupplier;
    private String name;
    private int x;
    private int y;

    @FXML private Circle stationCircle;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String stationName) {
        this.name = stationName;
        Tooltip tooltip = new Tooltip(stationName);
        Tooltip.install(stationCircle, tooltip);
    }

    public void setDetailsDTOSupplier(Supplier<StationDetailsDTO> detailsDTOSupplier) {
        this.detailsDTOSupplier = detailsDTOSupplier;
    }

    @FXML
    /*
    Upon clicking on the station area, will get the station details (this is where you would generate updated, fresh details every time)
    and will create a new Stage where the popup will be presented with the updated details.
    The popup is MODAL and have to be closed to continue work with the map
     */
    private void showStationDetails(MouseEvent event) {
        try {
            StationDetailsDTO stationDetailsDTO = detailsDTOSupplier.get();
            stationDetailsDTO.setName(name);
            stationDetailsDTO.setX(x);
            stationDetailsDTO.setY(y);

            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("../../details/visual/StationDetailsView.fxml");
            fxmlLoader.setLocation(url);
            AnchorPane root = fxmlLoader.load(url.openStream());

            StationDetailsController controller = fxmlLoader.getController();
            //controller.setData(stationDetailsDTO);

            Stage details = new Stage();
            details.initModality(Modality.APPLICATION_MODAL);
            final Scene scene = new Scene(root, 739, 206);
            details.setResizable(false);
            details.setScene(scene);
            details.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
