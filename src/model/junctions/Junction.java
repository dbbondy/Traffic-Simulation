
package model.junctions;

import java.util.ArrayList;
import model.Lane;

/**
 *
 * @author Dan
 */
public abstract class Junction {
    
    protected ArrayList<Lane> lanes;
    
    public abstract void distributeNewCars(int cars, int trucks);
    
    public void registerLane(Lane lane){
        lanes.add(lane);
    }
    
    public abstract void manageJunction();
        
    
}
