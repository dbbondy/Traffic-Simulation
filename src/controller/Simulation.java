package controller;

import java.util.HashMap;
import java.util.Map;
import model.junctions.*;
import view.SettingsWindow;
import view.UserInterface;

/**
 *
 * @author Daniel Bond
 */
public class Simulation {

    public static final String MIN_DENSITY = "min-density";
    public static final String MAX_DENSITY = "max-density";
    public static final String AGGRESSION = "aggression";
    public static final String CAR_RATIO = "car-ratio";
    public static final String TRUCK_RATIO = "truck-ratio";
    public static final String MAXIMUM_SPEED = "maximum-speed";
    public static final String JUNCTION_TYPE = "junction-type";
    public static final String TIME_STEP = "time-step";
    public static final String FILE_EXT = ".tss";
    public static final String STATS_FILE_EXT = ".txt";
    
    private static boolean paused;
    private static boolean started;
    private static SettingsWindow settingsWindow;
    private static UserInterface ui;
    private static Map<String, Object> settings;
    private static SimulationThread simulationThread;
    private static int totalTimeSteps;

    public static void init() {        
        Junction.registerJunctionType(FlyoverJunction.class);
        Junction.registerJunctionType(PlainJunction.class);
        Junction.registerJunctionType(RoundaboutJunction.class);
        Junction.registerJunctionType(TrafficLightJunction.class);
        Junction.registerJunctionType(TwoLaneJunction.class);
        settings = new HashMap<>();
        settings.put(MIN_DENSITY, 0);
        settings.put(MAX_DENSITY, 0);
        settings.put(AGGRESSION, 0);
        settings.put(CAR_RATIO, 0);
        settings.put(TRUCK_RATIO, 0);
        settings.put(MAXIMUM_SPEED, 0);
        settings.put(JUNCTION_TYPE, null);
        settings.put(TIME_STEP, 0);
        paused = false;
        started = false;
        simulationThread = new SimulationThread();
        settingsWindow = new SettingsWindow();
        totalTimeSteps = 0;
    }

    // how to create a proper MVC compliant instantiation
    /*
     * simulation creates instance of settings window, and waits until processing on that window is complete simulation creates instance of main user interface window
     */
    public static void setOption(String option, Object value) {
        settings.put(option, value);
    }

    public static Object getOption(String option) {
        return settings.get(option);
    }

    public static void pause() {
        if (isStarted()) {
            paused = !paused;
            if (ui != null) {
                ui.updateGUI();
            }
        }
    }

    public static synchronized boolean isPaused() {
        return paused;
    }

    public static void settingsChanged() {
        if (ui == null) {
            ui = new UserInterface();
            return;
        }
        reset();
        ui.reloadGUI();
        ui.updateGUI();
        if(paused == true){
            paused = false;
        }
    }

    private static void simulateOneStep() {
        
        // prevent rendering while AI processes
        synchronized (Simulation.class) {
            
            // core simulation step progress.
            Junction junc = (Junction)getOption(Simulation.JUNCTION_TYPE); 
            int carsRatio = (Integer)getOption(Simulation.CAR_RATIO); 
            int trucksRatio = (Integer)getOption(Simulation.TRUCK_RATIO);
            junc.distributeNewCars(carsRatio, trucksRatio); 
            junc.manageJunction(); // goes through all lanes contained in the junction, and tells each car within each lane to "act" 
            junc.updateDeletions();
            // when cars go outof the end of the junction, they get "deleted" and statistics are incremented.   
            
            setOption(TIME_STEP, ((int) getOption(TIME_STEP) + 1)); 
            
        }      
        
    }

    public static void start() {
        
        started = true;
        
        if (simulationThread != null) {
            // tells the thread to terminate
            simulationThread.terminate();
        }
        
        // creates a new thread
        simulationThread = new SimulationThread();
        simulationThread.start();
        
    }

    public static synchronized boolean isStarted() {
        return started;
    }
    
    public static void stop() {
        reset();
    }
    
    public static int getTotalTimeSteps(){
        return totalTimeSteps;
    }
    
    public static void reset(){

        // cannot stop until the current step
        // and rendering is complete
        synchronized (ui) {
            Junction jn = (Junction) getOption(JUNCTION_TYPE);
            jn.removeVehicles();
            totalTimeSteps = (int) settings.get(TIME_STEP);
            setOption(TIME_STEP, 0); 
            started = false;
            paused = false;
            ui.reloadGUI();
            ui.updateGUI();            
        }
        
    }

    public static SimulationThread getSimulationThread() {
        return simulationThread;
    }

    public static class SimulationThread extends Thread {
        private boolean terminate = false;
        
        public void terminate() {
            this.terminate = true;
        }

        @Override
        public void run() {
            while (isStarted() && !terminate) {
                try {
                    // synchronize on ui so that
                    // we can prevent reset() etc 
                    // while half-way through a step
                    synchronized (ui) {
                        simulateOneStep();
                        ui.updateGUI(); 
                    }
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                synchronized (this) {
                    while (isPaused()) {
                        try {
                            this.wait();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
