
package model.junctions;

import java.util.ArrayList;
import model.Lane;

/**
 *
 * @author Dan
 */
public abstract class Junction {
    
    private ArrayList<Lane> lanes;
    
    public abstract void distributeNewCars(int cars, int trucks);
    
    public void registerLane(Lane lane){
        lanes.add(lane);
    }
    
    public abstract void manageJunction();
        
    public ArrayList<Lane> getLanes(){
        return lanes;
    }
    
}
