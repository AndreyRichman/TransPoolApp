package main.window.map.component.coordinate;

import main.window.map.component.util.NodesManager;

import java.util.function.BiFunction;

/*
Makes sure that each coordinate node will e created exactly once
 */
public class CoordinatesManager extends NodesManager<CoordinateNode> {

    public CoordinatesManager(BiFunction<Integer, Integer, CoordinateNode> factory) {
        super(factory);
    }
}
