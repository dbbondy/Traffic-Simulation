package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Dan
 */
public class Segment {
    
    private int lengthLeft;
    private int lengthRight;
    private static final int WIDTH = 25;
    private Segment next;
    private Segment previous;
    private List<Segment> connectedSegments;
    private Lane lane;
    private ConnectionType connType;
    private static int segmentCounter = 0;
    private int id;
    
    public Segment(Lane lane, int lengthLeft, int lengthRight){
        this.lengthLeft = lengthLeft * (Lane.SEGMENT_DENSITY);
        this.lengthRight = lengthRight * (Lane.SEGMENT_DENSITY);
        this.lane = lane;
        this.id = segmentCounter++;
        connectedSegments = new ArrayList<Segment>();
    }
    
    public int id() {
        return this.id;
    }
    
    public void setConnectionType(ConnectionType type){
        connType = type;
    }
    
    public void addConnectedSegment(Segment segment){
        connectedSegments.add(segment);
    }
    
    public void addConnectedSegments(Segment[] segments){
        connectedSegments.addAll(Arrays.asList(segments));
    }
    
    public List<Segment> getConnectedSegments(){
        return connectedSegments;
    }
    
    public void setNextSegment(Segment segment){
        next = segment;
    }
    
    public void setPreviousSegment(Segment segment){
        previous = segment;
    }
    
    public Segment getNextSegment(){
        return next;
    }
    
    public Segment getPreviousSegment(){
        return previous;
    }
    
}
