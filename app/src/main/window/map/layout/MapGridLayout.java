package main.window.map.layout;

import com.fxgraph.graph.Graph;
import com.fxgraph.layout.Layout;
import main.window.map.component.coordinate.CoordinatesManager;
import main.window.map.component.station.StationManager;

public class MapGridLayout implements Layout {

    private final int SCALE = 70;
    private CoordinatesManager coordinatesManager;
    private StationManager stationManager;

    public MapGridLayout(CoordinatesManager coordinatesManager, StationManager stationManager) {
        this.coordinatesManager = coordinatesManager;
        this.stationManager = stationManager;
    }

    @Override
    public void execute(Graph graph) {

        coordinatesManager.getAllCoordinates().forEach(coordinateNode ->
                graph
                    .getGraphic(coordinateNode)
                    .relocate(coordinateNode.getX() * SCALE, coordinateNode.getY() * SCALE)
        );
        
        final int STATION_FIX_X = -15;
        final int STATION_FIX_Y = -15;
        stationManager.getAllCoordinates().forEach(stationNode -> {
            int x = stationNode.getX();
            int y = stationNode.getY();
            graph.getGraphic(stationNode).relocate(x * SCALE + STATION_FIX_X, y * SCALE + STATION_FIX_Y);
        });
    }

}
