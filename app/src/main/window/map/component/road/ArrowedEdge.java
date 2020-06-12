package main.window.map.component.road;

import com.fxgraph.edges.Edge;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/*
represents an edge that can bare an arrow on it
 */
public class ArrowedEdge extends Edge {

    private final double ARROW_HEAD_SIZE = 15;
    private EdgeGraphic edgeGraphic;
    private String edgeStyleSheet;
    private Polygon arrowHead;

    public ArrowedEdge(ICell source, ICell target) {
        super(source, target);
        edgeGraphic = null;
        edgeStyleSheet =
                getClass()
                    .getResource("RoadStyle.css")
                    .toExternalForm();
    }

    @Override
    public EdgeGraphic getGraphic(Graph graph) {
        if (edgeGraphic == null) {
            edgeGraphic = super.getGraphic(graph);
            addArrowShape(edgeGraphic.getLine());
            edgeGraphic.getStylesheets().add(edgeStyleSheet);
        }
        return edgeGraphic;
    }

    private void addArrowShape(Line line) {

        // each time the line end point changes (happens when the graph gets to be built and laid out for the first time)
        // update the arrow head accordingly (technically supports also the effect of moving the nodes in the area, and
        // recalculate the arrow shape to move accordingly)
        line.endXProperty().addListener(q -> {
            recalculateArrowHead(line);
        });

        line.endYProperty().addListener(q -> {
            recalculateArrowHead(line);
        });

        line.startXProperty().addListener(q -> {
            recalculateArrowHead(line);
        });

        line.startYProperty().addListener(q -> {
            recalculateArrowHead(line);
        });



    }

    // arrow head calculations are based on this sample of code:
    // https://gist.github.com/kn0412/2086581e98a32c8dfa1f69772f14bca4
    // currently adding the arrow at the middle of the edge. Can be easily changed to add more arrows (pointing the same
    // direction) on the edge, or only one at its end etc.
    private void recalculateArrowHead(Line line) {
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();

        // calculates the position of the arrow head on the line. Here for example in the middle. Can be easily changed if needed
        double arrowHeadX = startX + ((endX - startX) / 2);
        double arrowHeadY = startY + ((endY - startY) / 2);

        //ArrowHead
        double angle = Math.atan2((arrowHeadY - startY), (arrowHeadX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + arrowHeadX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + arrowHeadY;

        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + arrowHeadX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + arrowHeadY;

        // remove last arrow head and add this new one
        Group group = edgeGraphic.getGroup();
        group.getChildren().remove(arrowHead);

        // create and add the new arrow head
        arrowHead = new Polygon(arrowHeadX, arrowHeadY, x1, y1, x2, y2);
        arrowHead.getStyleClass().add("arrow-head");
        group.getChildren().add(arrowHead);
    }

    public Line getLine() {
        return edgeGraphic.getLine();
    }

    public Text getText() {
        return edgeGraphic.getText();
    }
}
