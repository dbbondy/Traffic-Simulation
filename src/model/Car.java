package model;

// my awesome change
import controller.Simulation;
import java.awt.Color;
import java.util.Map;

// this is another change
/**
 *
 * @author Daniel Bond
 */
public class Car extends Vehicle {

    private static final int WIDTH = 12;
    private static final int LENGTH = 20;

    public Car() {
    }

    public Car(Lane lane) {
        super(lane, lane.getFirstSegment(), null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Color c) {
        super(lane, lane.getFirstSegment(), null, null, c);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Segment seg, Color c) {
        super(lane, seg, null, null, c);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Segment seg) {
        super(lane, seg, null, null, Color.RED);
        setDimensions(WIDTH, LENGTH);
    }

    public Car(Lane lane, Segment seg, Vehicle inFront, Vehicle behind, Color c) {
        super(lane, seg, inFront, behind, c);
        setDimensions(WIDTH, LENGTH);
    }

    @Override
    public void act() {
        advanceVehicle(currentSpeed);

        /*Vehicle ahead = currentLane.getVehicleAhead(getHeadSegment());
        
        int aheadIndex = currentLane.getLaneSegments().indexOf(ahead);
        int currentIndex = currentLane.getLaneSegments().indexOf(this);
        int distance = aheadIndex - currentIndex;
        if(distance < 0){ // there is no vehicle ahead of us, so we can safely accelerate
            accelerate();
            return;
        }else if (distance < 10){ //if vehicle ahead is close enough, we need to evaluate our own speed.
            int aheadSpeed = ahead.getSpeed();
            int speedDifferential = aheadSpeed - currentSpeed;
            if(speedDifferential < 0){ // if we are going faster than the vehicle ahead, we need to at least match the speed of that vehicle. 
                for(int i = 0; i < Math.abs(speedDifferential); i++){
                    decelerate();
                }
                if(checkLaneAvailability()){
                    
                }
            }else if(speedDifferential <= 5 && (Integer)Simulation.getOption(Simulation.AGGRESSION) > 50){
                accelerate();
            }else { //else the speed differential is significantly large whereby a change of lanes serves no purpose
                accelerate();
                return;
            }
        }
        
        

        if (currentSpeed < ((Integer) Simulation.getOption(Simulation.MAXIMUM_SPEED))) {
            accelerate();
            
        }else{
            decelerate();
        }
        */
    }

    public void accelerate() {
        this.currentSpeed++;
    }
    
    public void decelerate(){
        this.currentSpeed--;
    }
    
    public void checkLaneAvailability(){
        Segment s = this.getHeadSegment();
        Map<Segment, ConnectionType> connectedSegments = s.getConnectedSegments();
       // connectedSegments.
    }
}
