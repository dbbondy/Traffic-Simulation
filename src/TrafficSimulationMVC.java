
import controller.Simulation;
import javax.swing.UIManager;

/**
 * Main class of the project
 * @author Daniel Bond
 */
public class TrafficSimulationMVC {
 
    
    public static void main(String[] args) throws Exception {
        
        // set the system look and feel and then ask for update
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());        
        Simulation.init();
        
    }
    
}
