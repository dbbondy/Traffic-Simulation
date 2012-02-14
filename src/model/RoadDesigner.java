/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exceptions.NoLaneExistsException;
import exceptions.SegmentCollectionEmptyException;

/**
 *
 * @author Dan
 */
public class RoadDesigner {

    public RoadDesigner() {
    }

    public Segment[] build10Segments(Lane lane) throws NoLaneExistsException {
        
        if (lane == null) {
            throw new NoLaneExistsException("You cannot assign segments to a null valued lane");
        }

       
        Segment[] set = new Segment[10];
        for (int i = 0; i < set.length; i++) {
            set[i] = new Segment(lane, 15, 0);
        }

        for (int i = 0; i < set.length; i++) {
            
            if (i == 0) { //if first element special case
                set[i].setNextSegment(set[i + 1]);
                continue;
            }
            if (i == set.length - 1) { //if end of list special case
                set[i].setPreviousSegment(set[i - 1]);
                break;
            }
            //else normal computation
            set[i].setNextSegment(set[i + 1]);
            set[i].setPreviousSegment(set[i - 1]);
            
        }

        return set;
    }

    /*public Segment[] buildLeftSection(Lane lane, Lane intersectingLane){
    
    //get 5 segments leading into the junction from any direction
    // 2 segments inside the junction belong to the lane that 5 segments were built for.
    //those 2 segments have connected neighbours to 2 other segments in that quadrant
    
    
    }*/
    
    
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
