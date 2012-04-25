
package model.junctions;

import model.*;
import view.SimulationPanel;

/**
 * A class to represent a Two-Lane Junction
 * @author Daniel Bond
 */
public class TwoLaneJunction extends Junction {
    
    public static final String NAME = "Two-Lane Junction"; // the name of the junction. Used for reflection purposes

    private Lane bottomUpwardsLane; // lanes of the junction
    private Lane bottomUpwardsLane2;
    private Lane topDownwardsLane;
    private Lane topDownwardsLane2;
    private Lane leftRightwardsLane;
    private Lane leftRightwardsLane2;
    private Lane rightLeftwardsLane;
    private Lane rightLeftwardsLane2;
    
    public TwoLaneJunction() {
        
        // create lanes
        bottomUpwardsLane = new Lane(400, SimulationPanel.HEIGHT, 180, TurnDirection.RIGHT_AND_STRAIGHT);
        bottomUpwardsLane2 = new Lane(400 - Segment.WIDTH, SimulationPanel.HEIGHT, 180, TurnDirection.LEFT);

        topDownwardsLane = new Lane((400 - (Segment.WIDTH * 2)), 0, 0, TurnDirection.LEFT);
        topDownwardsLane2 = new Lane((400 - Segment.WIDTH * 3), 0, 0, TurnDirection.RIGHT_AND_STRAIGHT);

        leftRightwardsLane = new Lane(0, 350, 270, TurnDirection.LEFT);
        leftRightwardsLane2 = new Lane(0, 350 - Segment.WIDTH, 270, TurnDirection.RIGHT_AND_STRAIGHT);

        rightLeftwardsLane = new Lane(800, (350 - (Segment.WIDTH * 2)), 90, TurnDirection.RIGHT_AND_STRAIGHT);
        rightLeftwardsLane2 = new Lane(800, (350 - (Segment.WIDTH * 3)), 90, TurnDirection.LEFT);

        // build lanes and connections between those lanes
        buildLanes();
        setUpIntersectionConnections();

        // register the lanes with the junction
        registerLane(bottomUpwardsLane);
        registerLane(bottomUpwardsLane2);
        registerLane(topDownwardsLane);
        registerLane(topDownwardsLane2);
        registerLane(leftRightwardsLane);
        registerLane(leftRightwardsLane2);
        registerLane(rightLeftwardsLane);
        registerLane(rightLeftwardsLane2);
    }

    /**
     * Build the lanes of the junction
     */
    private void buildLanes() {
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT, bottomUpwardsLane, bottomUpwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT, topDownwardsLane, topDownwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.WIDTH, leftRightwardsLane, leftRightwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.WIDTH, rightLeftwardsLane, rightLeftwardsLane2);
    }

    /**
     * Set up the connections between the intersecting segments of the junction
     */
    private void setUpIntersectionConnections() {

        Segment s1;
        Segment s2;

        int width50 = Segment.WIDTH / 2;

        s1 = topDownwardsLane2.getLaneSegments().get(rightLeftwardsLane2.getYStart() - width50);
        s2 = rightLeftwardsLane2.getLaneSegments().get(SimulationPanel.WIDTH - (topDownwardsLane2.getXStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = rightLeftwardsLane2.getLaneSegments().get(SimulationPanel.WIDTH - (bottomUpwardsLane.getXStart() + width50));
        s2 = bottomUpwardsLane.getLaneSegments().get(SimulationPanel.HEIGHT - (rightLeftwardsLane2.getYStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.NEXT_TO_RIGHT);

        s1 = leftRightwardsLane.getLaneSegments().get(topDownwardsLane2.getXStart() - width50);
        s2 = topDownwardsLane2.getLaneSegments().get(leftRightwardsLane.getYStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.NEXT_TO_RIGHT);

        s1 = bottomUpwardsLane.getLaneSegments().get(SimulationPanel.HEIGHT - (leftRightwardsLane.getYStart() + width50));
        s2 = leftRightwardsLane.getLaneSegments().get(bottomUpwardsLane.getXStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.NEXT_TO_LEFT);

        s1 = rightLeftwardsLane.getLaneSegments().get(SimulationPanel.WIDTH - (topDownwardsLane.getXStart() + width50));
        s2 = topDownwardsLane.getLaneSegments().get(rightLeftwardsLane.getYStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.NEXT_TO_LEFT);

        s1 = bottomUpwardsLane2.getLaneSegments().get(SimulationPanel.HEIGHT - (rightLeftwardsLane.getYStart() + width50));
        s2 = rightLeftwardsLane.getLaneSegments().get(SimulationPanel.WIDTH - (bottomUpwardsLane2.getXStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.NEXT_TO_LEFT);

        s1 = leftRightwardsLane2.getLaneSegments().get(bottomUpwardsLane2.getXStart() - width50);
        s2 = bottomUpwardsLane2.getLaneSegments().get(SimulationPanel.HEIGHT - (leftRightwardsLane2.getYStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.NEXT_TO_LEFT);

        s1 = topDownwardsLane.getLaneSegments().get(leftRightwardsLane2.getYStart() - width50);
        s2 = leftRightwardsLane2.getLaneSegments().get(topDownwardsLane.getXStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.NEXT_TO_LEFT);
    }
    
    /**
     * Returns the named representation of this junction
     * @return the named representation of this junction
     */
    @Override
    public String toString() {
        return NAME;
    }
}
