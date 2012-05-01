/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import model.Lane;
import model.RoadDesigner;
import model.Segment;
import model.TurnDirection;
import view.SimulationPanel;

/**
 *
 * @author Dan
 */
public class TurnJunc extends Junction{
    
    public static final String NAME = "Turning-Lane";
    private Lane lane;
    
    public TurnJunc(){
        lane = new Lane(400, SimulationPanel.HEIGHT, 180, TurnDirection.ALL);
        
        buildRoads();
        registerLane(lane);
    }
    
    private void buildRoads(){
        Segment[]s = RoadDesigner.buildStraight(400, lane);
        lane.add(s);
        
        s = RoadDesigner.buildLargeTurn(90, lane, 3);
        lane.add(s);
        
        s = RoadDesigner.buildStraight(200, lane);
        lane.add(s);
        
    }
    
    @Override
    public String toString(){
        return NAME;
    }
}
