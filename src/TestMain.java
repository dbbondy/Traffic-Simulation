
import java.util.Iterator;
import java.util.Random;
import model.*;



/**
 *
 * @author Dan
 */
public class TestMain {

    public static void main(String[] args) {
                
        // TurnDirection is a bad idea!
        Lane lane = new Lane(0, 0, 0, TurnDirection.ALL);
        RoadDesigner.buildLane(200, lane);
        
        Random rand = new Random(System.nanoTime());   
        
        Vehicle v;
        
        v = new Car();
        v.setHeadSegment(lane.getLaneSegments().get(0));
        try { VehicleBinarySearch.addVehicle(lane.getVehicles(), v); }
        catch (Exception e) {}
        
        for (int i = 0; i < 100; i++) {
            v = new Car();
            int segmentID = rand.nextInt(200); 
            v.setHeadSegment(lane.getLaneSegments().get(segmentID));
            try { VehicleBinarySearch.addVehicle(lane.getVehicles(), v); }
            catch (Exception e) {}
        }        
        
        System.out.println("-----");
        
        for (Iterator<Vehicle> itr = lane.getVehicles().iterator(); itr.hasNext(); ) {
            System.out.println(itr.next().getHeadSegment().id());
        }
        
        System.out.println("-----");
        
        // 2 test cases!
        // at segment 0 
        // at segment 199 
        // manually
        
        for (int i = 0; i < 200; i++){
            Segment s = lane.getLaneSegments().get(i);
            v = VehicleBinarySearch.findVehicleAhead(lane.getVehicles(), s);
            
            boolean foundVehicleAhead = false;
            
            for (Iterator<Vehicle> itr = lane.getVehicles().iterator(); itr.hasNext(); ) {
                Vehicle v2 = itr.next();
                if (v2.getHeadSegment().id() > s.id()) {
                    System.out.println(v2.getHeadSegment().id() + ">" + s.id());
                    assert(v == v2);
                    foundVehicleAhead = true;
                    break;
                }
            }
            
            if (!foundVehicleAhead) {
                assert(v == null);
                System.out.println(lane.getVehicles().get(0).getHeadSegment().id() + "==" + s.id() + "||" + 
                        lane.getVehicles().get(lane.getVehicles().size()-1).getHeadSegment().id() + "<=" + s.id());
            }
        }
        
        Lane lane2 = new Lane(0, 0, 0, TurnDirection.ALL);
        RoadDesigner.buildLane(100, lane2);
        
        v = new Car();
        v.setHeadSegment(lane2.getLaneSegments().get(5));
        VehicleBinarySearch.addVehicle(lane2.getVehicles(), v);
        
        assert(VehicleBinarySearch.findVehicleAhead(lane2.getVehicles(), lane2.getLaneSegments().get(6)) == null);
        assert(VehicleBinarySearch.findVehicleAhead(lane2.getVehicles(), lane2.getLaneSegments().get(5)) == null);
        assert(VehicleBinarySearch.findVehicleAhead(lane2.getVehicles(), lane2.getLaneSegments().get(4)) != null);
        
        System.out.println("-----");
        
        for (int i = 0; i < 200; i++){
            Segment s = lane.getLaneSegments().get(i);
            v = VehicleBinarySearch.findVehicleBehind(lane.getVehicles(), s);
            
            boolean foundVehicleBehind = false;
            
            for (int j = lane.getVehicles().size() - 1; j >= 0; j--) {
                Vehicle v2 = lane.getVehicles().get(j);
                if (v2.getHeadSegment().id() < s.id()) {
                    System.out.println(v2.getHeadSegment().id() + "<" + s.id());
                    assert(v == v2);
                    foundVehicleBehind = true;
                    break;
                }
            }
                        
            if (!foundVehicleBehind) {
                assert(v == null);
                System.out.println(lane.getVehicles().get(0).getHeadSegment().id() + ">=" + s.id() + "||" + 
                        lane.getVehicles().get(lane.getVehicles().size()-1).getHeadSegment().id() + "==" + s.id());
            }
        }
        
        Lane lane3 = new Lane(0,0,0, TurnDirection.ALL);
        RoadDesigner.buildLane(100, lane3);
        
        v = new Truck();
        v.setHeadSegment(lane3.getLaneSegments().get(10));
        VehicleBinarySearch.addVehicle(lane3.getVehicles(), v);
        
        assert(VehicleBinarySearch.findVehicleBehind(lane3.getVehicles(), lane3.getLaneSegments().get(9)) == null);
        assert(VehicleBinarySearch.findVehicleBehind(lane3.getVehicles(), lane3.getLaneSegments().get(10)) == null);
        assert(VehicleBinarySearch.findVehicleBehind(lane3.getVehicles(), lane3.getLaneSegments().get(11)) != null);
    }
}
