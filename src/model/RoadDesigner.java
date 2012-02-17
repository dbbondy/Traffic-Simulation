/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import exceptions.SegmentCollectionEmptyException;

/**
 *
 * @author Dan
 */
public class RoadDesigner {
    
    private static double angleOfRotation = 5;

    public RoadDesigner() {
    }

   public static Segment[] getStraight(int lengthOfSection, Lane lane)throws IllegalArgumentException{
      
       if(lengthOfSection < 1){
           throw new IllegalArgumentException("You cannot enter a number of 0 or less");
       } 
       Segment[] segments;
       int l =  (int)(angleOfRotation / (2 * Math.PI)) * (Segment.WIDTH / 2);
       if(lengthOfSection == 1){
           segments = new Segment[1];
           segments[0] = new Segment(lane, l, 0); 
           
       }else{
            segments = new Segment[lengthOfSection];
           for (int i = 0; i < lengthOfSection; i++){
               segments[i] = new Segment(lane, l, 0);
               if(i == 0){
                   segments[i].setNextSegment(segments[i + 1]);
                   continue;
               }
               if(i == lengthOfSection - 1){
                   segments[i].setPreviousSegment(segments[i - 1]);
                   break;
               }
               
               segments[i].setNextSegment(segments[i + 1]);
               segments[i].setPreviousSegment(segments[i - 1]);
           }
       }
       return segments;
       
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
