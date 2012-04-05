package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Lane;
import model.Segment;
import model.State;
import model.Vehicle;
import model.junctions.*;
import view.SettingsWindow;
import view.UserInterface;

/**
 *
 * @author Daniel Bond
 */
public class Simulation {

    public static final String DENSITY = "density";
    public static final String AGGRESSION = "aggression";
    public static final String CAR_RATIO = "car ratio";
    public static final String TRUCK_RATIO = "truck ratio";
    public static final String JUNCTION_TYPE = "junction type";
    public static final String TIME_STEP = "time step";
    private static boolean paused;
    private static boolean started;
    private static SettingsWindow settingsWindow;
    private static UserInterface ui;
    private static Map<String, Object> settings;
    private static SimulationThread simulationThread;

    public static void init() {
        settings = new HashMap<>();
        settings.put(DENSITY, 0);
        settings.put(AGGRESSION, 0);
        settings.put(CAR_RATIO, 0);
        settings.put(TRUCK_RATIO, 0);
        settings.put(JUNCTION_TYPE, null);
        settings.put(TIME_STEP, 0);
        paused = false;
        started = false;
        simulationThread = new SimulationThread();
        settingsWindow = new SettingsWindow();
    }

    //how to create a proper MVC compliant instantiation
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
        paused = !paused;
        if (ui != null) {
            ui.updateGUI();
        }
    }
    
    public static void setSimulationState(State state){
        Simulation.setOption(DENSITY, state.getDensity());
        Simulation.setOption(AGGRESSION, state.getAggression());
        Simulation.setOption(CAR_RATIO, state.getCarRatio());
        Simulation.setOption(TRUCK_RATIO, state.getTruckRatio());
        Simulation.setOption(TIME_STEP, state.getTimeStep());
        
        switch(state.getJunction()){
            case "Two-Lane Junction":
                Simulation.setOption(JUNCTION_TYPE, new TwoLaneJunction());
                break;
            case "Flyover Junction":
                Simulation.setOption(JUNCTION_TYPE, new FlyoverJunction());
                break;
            case "Plain Junction":
                Simulation.setOption(JUNCTION_TYPE, new PlainJunction());
                break;
            case "Traffic Light Junction":
                Simulation.setOption(JUNCTION_TYPE, new TrafficLightJunction());
                break;
            case "Roundabout Junction":
                Simulation.setOption(JUNCTION_TYPE, new RoundaboutJunction());
                break;
        }
        ui.updateGUI();
    }

    public static synchronized boolean isPaused() {
        return paused;
    }

    public static void Simulate() {
        if (simulationThread != null) {
            // tells the thread to terminate
            simulationThread.terminate();
        }
        // creates a new thread
        simulationThread = new SimulationThread();
        simulationThread.start();
        //SimulationStats.publishStats();
    }

    public static void settingsChanged() {
        if (ui == null) {
            ui = new UserInterface();
            return;
        }
        if(settingsWindow.isJuncDifferent()){
            reset();
            ui.reloadGUI();
            ui.updateGUI();
        }
        ui.reloadGUI();
        ui.updateGUI();
        if(paused == true){
            paused = false;
        }
    }

    private static void simulateOneStep() {
        
        
        //core simulation step progress.
         Junction junc = (Junction)getOption(Simulation.JUNCTION_TYPE); 
         int carsRatio = (Integer)getOption(Simulation.CAR_RATIO); 
         int trucksRatio = (Integer)getOption(Simulation.TRUCK_RATIO);
         junc.distributeNewCars(carsRatio, trucksRatio); 
         junc.manageJunction(); //goes through all lanes contained in the junction, and tells each car within each lane to "act" 
         //junc.updateDeletions();
         //when cars go outof the end of the junction, they get "deleted" and statistics are incremented.   
         
        
        
        /*// test code
        Junction currentJunction = (Junction)Simulation.getOption(Simulation.JUNCTION_TYPE);
        ArrayList<Lane> lanes = currentJunction.getLanes();
        for(Lane l : lanes){
            for(Vehicle v : l.getVehicles()){
                Segment head = v.getHeadSegment();
                if (head.getNextSegment() != null) {
                    v.setHeadSegment(head.getNextSegment());
                } else {
                    v.setHeadSegment(l.getFirstSegment());
                }
            }
        }
        */
        int currentStep = (Integer) getOption(TIME_STEP);
        currentStep++;
        setOption(TIME_STEP, currentStep);

    }

    public static void start() {
        started = (!started);
    }

    public static synchronized boolean isStarted() {
        return started;
    }
    
    public static void reset(){

        // cannot stop until the current step
        // and rendering is complete
        synchronized (ui) {
            setOption(TIME_STEP, 0); 
            started = false;
            paused = false;
            ui.updateGUI();
            ui.reloadGUI();
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
                synchronized (this) {
                    while (isPaused()) {
                        try {
                            this.wait();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
                try {
                    // synchronize on ui so that
                    // we can prevent reset() etc 
                    // while half-way through a step
                    synchronized (ui) {
                        simulateOneStep();
                        ui.updateGUI(); 
                    }
                    Thread.sleep(30);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}
