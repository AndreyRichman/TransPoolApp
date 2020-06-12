package main.window.map.layout;

import com.fxgraph.graph.Graph;
import com.fxgraph.layout.Layout;
import main.window.map.component.coordinate.CoordinatesManager;
import main.window.map.component.station.StationManager;

public class MapGridLayout implements Layout {

    private int scale;
    private CoordinatesManager coordinatesManager;
    private StationManager stationManager;

    public MapGridLayout(CoordinatesManager coordinatesManager, StationManager stationManager, int scale) {
        this.coordinatesManager = coordinatesManager;
        this.stationManager = stationManager;
        this.scale = scale;
    }

    @Override
    public void execute(Graph graph) {

        coordinatesManager.getAllCoordinates().forEach(coordinateNode ->
                graph
                    .getGraphic(coordinateNode)
                    .relocate(coordinateNode.getX() * this.scale, coordinateNode.getY() * this.scale)
        );
        
        final int STATION_FIX_X = -15;
        final int STATION_FIX_Y = -15;
        stationManager.getAllCoordinates().forEach(stationNode -> {
            int x = stationNode.getX();
            int y = stationNode.getY();
            graph.getGraphic(stationNode).relocate(x * this.scale + STATION_FIX_X, y * this.scale + STATION_FIX_Y);
        });
    }

}
