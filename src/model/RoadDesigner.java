
package model;

import exceptions.SegmentCollectionEmptyException;

/**
 *
 * @author Dan
 */
public class RoadDesigner {

    private static int lengthOfSegment = 1; //result of calculation 

    public RoadDesigner() {
    }

    public static Segment[] getStraight(int lengthOfSection, Lane lane) throws IllegalArgumentException {

        if (lengthOfSection < 1) {
            throw new IllegalArgumentException("You cannot enter a number of 0 or less");
        }
        Segment[] segments;
        
        segments = buildSection(lengthOfSection, 0, lane);
        return segments;
    }

    public static Segment[] buildLeftTurn(int lengthOfSection, Lane lane, boolean isClockwise) throws IllegalArgumentException{
        if (lengthOfSection < 1) {
            throw new IllegalArgumentException("You cannot enter a number of 0 or less");
        }
        Segment[] segments;
        if (isClockwise == true) {
            segments = buildSection(lengthOfSection, -5, lane);
            return segments;
        } else{
           segments = buildSection(lengthOfSection, 5, lane);
           return segments;
        }
    }
    
    private static Segment[] buildSection(int numOfSections, int angle, Lane l){
         Segment[] segments;
        if (numOfSections == 1) {
                segments = new Segment[1];
                segments[0] = new Segment(l, lengthOfSegment, angle);

            } else {
                segments = new Segment[numOfSections];
                for (int i = 0; i < numOfSections; i++) {

                    segments[i] = new Segment(l, lengthOfSegment, angle);
                    if (i == 0) {
                        segments[i].setNextSegment(segments[i + 1]);
                        continue;
                    }
                    if (i == numOfSections - 1) {
                        segments[i].setPreviousSegment(segments[i - 1]);
                        break;
                    }

                    segments[i].setNextSegment(segments[i + 1]);
                    segments[i].setPreviousSegment(segments[i - 1]);
                }
            }
            return segments;
    }
    
    
    public static Segment[] buildRightTurn(int lengthOfSection, Lane lane, boolean isClockwise)throws IllegalArgumentException{
        if (lengthOfSection < 1) {
            throw new IllegalArgumentException("You cannot enter a number of 0 or less");
        }
        Segment[] segments;
        if (isClockwise == true) {
            segments = buildSection(lengthOfSection, 5, lane);
            return segments;
        } else{
           segments = buildSection(lengthOfSection, -5, lane);
           return segments;
        }
    }
    
    

    public void setUpConnectionsAdjacent(Segment[] firstSet, Segment[] secondSet) throws SegmentCollectionEmptyException {

        if (firstSet.length == 0 || secondSet.length == 0) {
            throw new SegmentCollectionEmptyException("Collection is empty");
        }

        for (int i = 0; i < firstSet.length; i++) {
            setUpConnectionSingle(firstSet[i], secondSet[i], ConnectionType.NEXT_TO);
        }
    }

    private void setUpConnectionSingle(Segment seg1, Segment seg2, ConnectionType connType) { //will be private once testing has completed
        seg1.addConnectedSegment(seg2);
        seg2.addConnectedSegment(seg1);
        seg1.setConnectionType(connType);
        seg2.setConnectionType(connType);
    }
}
