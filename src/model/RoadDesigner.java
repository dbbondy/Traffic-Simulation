package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to encapsulate all road building functions. 
 * This class provides various functions that can be combined together to produce complex roads
 * @author Daniel Bond
 */
public abstract class RoadDesigner {

    public RoadDesigner() {
    }

    /**
     * Function to build a set of segments that build up to become a "straight" section of lane
     * @param lengthOfSection the length of the section we want to build
     * @param lane the lane we want to build this section for
     * @return the collection of segments we created
     * @throws IllegalArgumentException if we pass an invalid number for the parameter <code> lengthOfSection </code> then this function will throw an Exception
     */
    public static Segment[] buildStraight(int lengthOfSection, Lane lane) throws IllegalArgumentException {

        if (lengthOfSection < 1) {
            throw new IllegalArgumentException("You cannot enter a number of 0 or less");
        }
        Segment[] segments;

        segments = buildSection(lengthOfSection, 0, lane);
        return segments;
    }

    /**
     * Function to build a set of "turning" segments for a lane
     * @param angle the angle of the turn
     * @param lane the lane we will associate these segments with
     * @return the collection of segments we created
     */
    public static Segment[] buildTurn(int angle, Lane lane) {
        boolean isClockwise = angle > 0;
        int lengthOfSection = Math.abs(angle) / 5;
        Segment[] segments;
        if (isClockwise) { // if we are turning in a clockwise direction
            segments = buildSection(lengthOfSection, 5, lane);
            return segments;
        } else { // else we are turning in an anti-clockwise direction
            segments = buildSection(lengthOfSection, -5, lane);
            return segments;
        }
    }
    
    /**
     * Function to create a larger turn which is spread over a larger distance than <code> buildTurn </code>
     * @param angle the angle of the turn
     * @param lane the lane we will associate these segments with
     * @param spacing the spacing out of turning segments so the turn is elongated.
     * @return the collection of segments we created
     */
    public static Segment[] buildLargeTurn(int angle, Lane lane, int spacing){
        boolean isClockwise = angle > 0;
        int lengthOfSection = Math.abs(angle) / 5;
        ArrayList<Segment> segments = new ArrayList<>();
        for (int i = 0; i < lengthOfSection; i++) {
            Segment[] corner = buildSection(1, isClockwise ? 5 : -5, lane);
            if (i > 0) segments.get(segments.size()-1).setNextSegment(corner[0]);
            segments.addAll(Arrays.asList(corner));
            if (i + 1 == lengthOfSection) break;
            Segment[] straight = buildSection(spacing, 0, lane);
            segments.get(segments.size()-1).setNextSegment(straight[0]);
            segments.addAll(Arrays.asList(straight));
        }
        return segments.toArray(new Segment[] {});
    }

    /**
     * Helper method to construct a section of segments for a lane
     * @param numOfSegments the number of segments in the section we want to construct
     * @param angle the angle of the segments we want to construct
     * @param l the lane we want to associate these segments with
     * @return the collection of segments we created
     */
    private static Segment[] buildSection(int numOfSegments, int angle, Lane l) {
        Segment[] segments = new Segment[numOfSegments];
        for (int i = 0; i < numOfSegments; i++) {
            Segment seg = new Segment(l, Segment.LENGTH, angle);
            if (i > 0) seg.setPreviousSegment(segments[i-1]);
            segments[i] = seg;
        }
        return segments;
    } 
    
    /**
     * Helper method to construct a lane of segments. 
     * This method is only used for straight lanes.
     * @param length the length of the lane
     * @param lane the lane to add the sections of road to
     */
    public static void buildLane(int length, Lane lane){
        Segment[] s = RoadDesigner.buildStraight(length, lane);
        lane.add(s);
    }
    
    /**
     * Function to construct two identical parallel lanes, that have connections adjacently to one another.
     * This method will only allow for equal sized parallel lanes. 
     * @param length the length of the lanes
     * @param lane1 the left lane of two
     * @param lane2 the right lane of two
     */
    public static void buildParallelLanes(int length, Lane lane1, Lane lane2){
        Segment[] s1 = RoadDesigner.buildStraight(length, lane1);
        Segment[] s2 = RoadDesigner.buildStraight(length, lane2);
        for (int i = 0; i < s1.length; i++) {
            s1[i].addConnectedSegment(s2[i], ConnectionType.NEXT_TO_RIGHT); 
        }
        for(int i = 0; i < s2.length; i++){
            s2[i].addConnectedSegment(s1[i], ConnectionType.NEXT_TO_LEFT);
        }
        lane1.add(s1);
        lane2.add(s2);
    }
}
