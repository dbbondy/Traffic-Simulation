
package model.junctions;

import java.awt.Color;
import java.util.ArrayList;
import model.Lane;

/**
 *
 * @author Dan
 */
public abstract class Junction{
    
    protected static final Color CAR_COLOR = new Color(0, 102, 153);
    protected static final Color TRUCK_COLOR = new Color(153, 0, 0);
    
    
    private ArrayList<Lane> lanes = new ArrayList<>();
    
    public abstract void distributeNewCars(int cars, int trucks);
    public abstract void manageJunction();
    protected abstract Lane chooseEmptyLane();
        
    public ArrayList<Lane> getLanes(){
        return lanes;
    }
     public void registerLane(Lane lane){
        lanes.add(lane);
    }
    
}
