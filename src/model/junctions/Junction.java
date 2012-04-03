
package model.junctions;

import java.util.ArrayList;
import model.Lane;
import model.Vehicle;

/**
 *
 * @author Dan
 */
public abstract class Junction{
    
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
