/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import java.util.ArrayList;
import java.util.Random;
import model.*;
import view.SimulationPanel;

/**
 *
 * @author Dan
 */
public class TwoLaneJunction extends Junction {
    
    public static final String name = "Two-Lane Junction";

    private Lane bottomUpwardsLane;
    private Lane bottomUpwardsLane2;
    private Lane topDownwardsLane;
    private Lane topDownwardsLane2;
    private Lane leftRightwardsLane;
    private Lane leftRightwardsLane2;
    private Lane rightLeftwardsLane;
    private Lane rightLeftwardsLane2;
    

    public TwoLaneJunction() {

        bottomUpwardsLane = new Lane(400, SimulationPanel.HEIGHT, 180);
        bottomUpwardsLane2 = new Lane(400 - Segment.WIDTH, SimulationPanel.HEIGHT, 180);

        topDownwardsLane = new Lane((400 - (Segment.WIDTH * 2)), 0, 0);
        topDownwardsLane2 = new Lane((400 - Segment.WIDTH * 3), 0, 0);

        leftRightwardsLane = new Lane(0, 350, 270);
        leftRightwardsLane2 = new Lane(0, 350 - Segment.WIDTH, 270);

        rightLeftwardsLane = new Lane(800, (350 - (Segment.WIDTH * 2)), 90);
        rightLeftwardsLane2 = new Lane(800, (350 - (Segment.WIDTH * 3)), 90);

        buildRoads();
        setUpIntersectionConnections();

        registerLane(bottomUpwardsLane);
        registerLane(bottomUpwardsLane2);
        registerLane(topDownwardsLane);
        registerLane(topDownwardsLane2);
        registerLane(leftRightwardsLane);
        registerLane(leftRightwardsLane2);
        registerLane(rightLeftwardsLane);
        registerLane(rightLeftwardsLane2);
 
    }

    private void buildRoads() {
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT, bottomUpwardsLane, bottomUpwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT, topDownwardsLane, topDownwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.WIDTH, leftRightwardsLane, leftRightwardsLane2);
        RoadDesigner.buildParallelLanes(SimulationPanel.WIDTH, rightLeftwardsLane, rightLeftwardsLane2);
    }

    private void setUpIntersectionConnections() {

        Segment s1;
        Segment s2;

        int width50 = Segment.WIDTH / 2;

        s1 = topDownwardsLane2.getLaneSegments().get(rightLeftwardsLane2.getYStart() - width50);
        s2 = rightLeftwardsLane2.getLaneSegments().get(SimulationPanel.WIDTH - (topDownwardsLane2.getXStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = rightLeftwardsLane2.getLaneSegments().get(SimulationPanel.WIDTH - (bottomUpwardsLane.getXStart() + width50));
        s2 = bottomUpwardsLane.getLaneSegments().get(SimulationPanel.HEIGHT - (rightLeftwardsLane2.getYStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = leftRightwardsLane.getLaneSegments().get(topDownwardsLane2.getXStart() - width50);
        s2 = topDownwardsLane2.getLaneSegments().get(leftRightwardsLane.getYStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = bottomUpwardsLane.getLaneSegments().get(SimulationPanel.HEIGHT - (leftRightwardsLane.getYStart() + width50));
        s2 = leftRightwardsLane.getLaneSegments().get(bottomUpwardsLane.getXStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = rightLeftwardsLane.getLaneSegments().get(SimulationPanel.WIDTH - (topDownwardsLane.getXStart() + width50));
        s2 = topDownwardsLane.getLaneSegments().get(rightLeftwardsLane.getYStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = bottomUpwardsLane2.getLaneSegments().get(SimulationPanel.HEIGHT - (rightLeftwardsLane.getYStart() + width50));
        s2 = rightLeftwardsLane.getLaneSegments().get(SimulationPanel.WIDTH - (bottomUpwardsLane2.getXStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = leftRightwardsLane2.getLaneSegments().get(bottomUpwardsLane2.getXStart() - width50);
        s2 = bottomUpwardsLane2.getLaneSegments().get(SimulationPanel.HEIGHT - (leftRightwardsLane2.getYStart() - width50));
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);

        s1 = topDownwardsLane.getLaneSegments().get(leftRightwardsLane2.getYStart() - width50);
        s2 = leftRightwardsLane2.getLaneSegments().get(topDownwardsLane.getXStart() + width50);
        s1.addConnectedSegment(s2, ConnectionType.OVERLAP);


    }
    
    @Override
    public String toString() {
        return name;
    }
}
