package model.junctions;

import model.*;
import view.SimulationPanel;

/**
 * Class to model a junction with a blocked lane
 *
 * @author Daniel Bond
 */
public class BlockedLaneJunction extends Junction {

    public static final String NAME = "Blocked-Lane Junction";
    private Lane unblockedLane;
    private Lane blockedLane;

    public BlockedLaneJunction() {

        unblockedLane = new Lane(400 - Segment.WIDTH, SimulationPanel.HEIGHT, 180, TurnDirection.STRAIGHT); 
        blockedLane = new Lane(400, SimulationPanel.HEIGHT, 180, TurnDirection.BLOCKED);

        buildRoads();
        registerLane(unblockedLane);
        registerLane(blockedLane);
    }

    private void buildRoads() {
        RoadDesigner.buildParallelLanes(SimulationPanel.HEIGHT / 2, unblockedLane, blockedLane);
        Segment[] s = RoadDesigner.buildStraight(SimulationPanel.HEIGHT / 2, unblockedLane);
        unblockedLane.add(s);
    }

    @Override
    public void updateDeletions() {
        Vehicle[] vehArray = unblockedLane.getVehicles().toArray(new Vehicle[0]);
        for (Vehicle v : vehArray) {
            if (v.getHeadSegment().equals(unblockedLane.getLastSegment())) {
                unblockedLane.getVehicles().remove(v);
                numberOfVehicles--;
                if (v instanceof Car) {
                    SimulationStats.incrementCars();
                    SimulationStats.addEvent(SimulationStats.createVehicleLeavingEvent(v));
                } else if (v instanceof Truck) {
                    SimulationStats.incrementTrucks();
                    SimulationStats.addEvent(SimulationStats.createVehicleLeavingEvent(v));
                }
            }
        }
    }
    
    @Override
    public String toString(){
        return NAME;
    }
}
