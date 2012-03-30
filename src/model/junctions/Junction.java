
package model.junctions;

import java.io.Serializable;
import java.util.ArrayList;
import model.Lane;

/**
 *
 * @author Dan
 */
public abstract class Junction{
    
    private ArrayList<Lane> lanes = new ArrayList<>();
    
    public abstract void distributeNewCars(int cars, int trucks);
    public abstract void manageJunction();
        
    public ArrayList<Lane> getLanes(){
        return lanes;
    }
     public void registerLane(Lane lane){
        lanes.add(lane);
    }
    
}
