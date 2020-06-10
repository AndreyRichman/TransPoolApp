package main.window.map.component.station;

import com.fxgraph.cells.AbstractCell;
import com.fxgraph.graph.Graph;
import main.window.map.component.station.visual.StationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import main.window.map.component.details.StationDetailsDTO;

import java.net.URL;
import java.util.function.Supplier;

/*
Represents a node (called 'cell' in the fxGraph library) that is the station
Station has a coordinate (x,y), a name and a supplier of additional data to be shown when user clicks on it
Station is created from an independent fxml file along with its controller and style sheet
 */
public class StationNode extends AbstractCell {

    private int x;
    private int y;
    private String name;
    private Supplier<StationDetailsDTO> detailsSupplier;
    public StationNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetailsSupplier(Supplier<StationDetailsDTO> detailsSupplier) {
        this.detailsSupplier = detailsSupplier;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    /*
    Creates the graphical representation of the station.
     */
    public Region getGraphic(Graph graph) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("visual/StationView.fxml");
            fxmlLoader.setLocation(url);
            HBox root = fxmlLoader.load(url.openStream());

            // updates information on the actual node's controller
            StationController controller = fxmlLoader.getController();
            controller.setX(x);
            controller.setY(y);
            controller.setName(name);
            controller.setDetailsDTOSupplier(detailsSupplier);

            return root;
        } catch (Exception e) {
            return new Label("Error when tried to create main.window.map.graphic node !");
        }
    }

}
