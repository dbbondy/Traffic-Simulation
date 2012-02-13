
package model.junctions;

/**
 *
 * @author Dan
 */
public abstract class Junction {
    
    public abstract void distributeNewCars(int cars, int trucks);
    
    public abstract void update();
    
    public abstract void manageJunction();
        
    
}
