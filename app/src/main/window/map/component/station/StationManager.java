package main.window.map.component.station;

import main.window.map.component.util.NodesManager;

import java.util.function.BiFunction;

/*
Makes sure that each Station node will e created exactly once
 */
public class StationManager extends NodesManager<StationNode> {

    public StationManager(BiFunction<Integer, Integer, StationNode> factory) {
        super(factory);
    }
}
