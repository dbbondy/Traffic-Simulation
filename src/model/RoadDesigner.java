package model;

import exceptions.SegmentCollectionEmptyException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Dan
 */
public class RoadDesigner {

    public RoadDesigner() {
    }

    public static Segment[] buildStraight(int lengthOfSection, Lane lane) throws IllegalArgumentException {

        if (lengthOfSection < 1) {
            throw new IllegalArgumentException("You cannot enter a number of 0 or less");
        }
        Segment[] segments;

        segments = buildSection(lengthOfSection, 0, lane);
        return segments;
    }

    public static Segment[] buildTurn(int angle, Lane lane) throws IllegalArgumentException {
        boolean isClockwise = angle > 0;
        int lengthOfSection = Math.abs(angle) / 5;
        Segment[] segments;
        if (isClockwise == true) {
            segments = buildSection(lengthOfSection, 5, lane);
            return segments;
        } else {
            segments = buildSection(lengthOfSection, -5, lane);
            return segments;
        }
    }
    
    public static Segment[] buildLargeTurn(int angle, Lane lane, int spacing) throws IllegalArgumentException {
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

    private static Segment[] buildSection(int numOfSections, int angle, Lane l) {
        Segment[] segments = new Segment[numOfSections];
        for (int i = 0; i < numOfSections; i++) {
            Segment seg = new Segment(l, Segment.LENGTH, angle);
            if (i > 0) seg.setPreviousSegment(segments[i-1]);
            segments[i] = seg;
        }
        return segments;
    } 
    
    public static void buildLane(int length, Lane lane){
        Segment[] s = RoadDesigner.buildStraight(length, lane);
        lane.add(s);
    }
    
    public static void buildParallelLanes(int length, Lane lane1, Lane lane2){
        Segment[] s1 = RoadDesigner.buildStraight(length, lane1);
        Segment[] s2 = RoadDesigner.buildStraight(length, lane2);
        for (int i = 0; i < s1.length; i++) {
            s1[i].addConnectedSegment(s2[i], ConnectionType.NEXT_TO);
        }
        lane1.add(s1);
        lane2.add(s2);
    }
}
