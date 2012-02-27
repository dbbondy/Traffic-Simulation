/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.junctions;

import java.util.ArrayList;
import model.Segment;
import model.Car;
import model.Lane;
import model.RoadDesigner;
import model.Vehicle;

/**
 *
 * @author Dan
 */
public class TwoLaneJunction extends Junction {
   
    private Lane bottomUpwardsLane;  

    public TwoLaneJunction() {        
        bottomUpwardsLane = new Lane(400, 0, 0);        
        bottomUpwardsLane.add(RoadDesigner.getStraight(100, bottomUpwardsLane));
        
        registerLane(bottomUpwardsLane);
        
        ArrayList<Segment> segments = bottomUpwardsLane.getLaneSegments();
        
        for (int i = 0; i < 10; i++) {
            int get = (segments.size() / 10) * i;
            Segment s = segments.get(get);
            Vehicle v1 = new Car(bottomUpwardsLane, s); // TODO: constructor should do the line below
            bottomUpwardsLane.addVehicle(v1);
        }   
    }
    
    @Override
    public void distributeNewCars(int cars, int trucks) {        
    }

    @Override
    public void manageJunction(){
        bottomUpwardsLane.updateVehicles();
    }
    
}
