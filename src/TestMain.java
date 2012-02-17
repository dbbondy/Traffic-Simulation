
import model.RoadDesigner;
import model.Segment;

/**
 *
 * @author Dan
 */
public class TestMain {

    public static void main(String[] args) {
      
       Segment[] s = RoadDesigner.getStraight(6, null);
        System.out.println(s.length);
        
    }

}
