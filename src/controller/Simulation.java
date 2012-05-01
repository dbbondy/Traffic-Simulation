package controller;

import java.util.HashMap;
import java.util.Map;
import model.junctions.*;
import view.SettingsWindow;
import view.UserInterface;

/**
 * The main simulation controller class
 *
 * @author Daniel Bond
 */
public class Simulation {

    // the key to access the minimum density value of the simulation
    public static final String MIN_DENSITY = "min-density";
    // the key to access the maximum density value of the simulation
    public static final String MAX_DENSITY = "max-density";
    // the key to access the vehicle aggression value of the simulation
    public static final String AGGRESSION = "aggression";
    // the key to access the car ratio value of the simulation
    public static final String CAR_RATIO = "car-ratio";
    // the key to access the truck ratio value of the simulation
    public static final String TRUCK_RATIO = "truck-ratio";
    // the key to access the maximum speed value of the simulation
    public static final String MAXIMUM_SPEED = "maximum-speed";
    // the key to access the type of junction the simulation is currently simulating.
    public static final String JUNCTION_TYPE = "junction-type";
    // the key to access the current time step of the simulation
    public static final String TIME_STEP = "time-step";
    // constant for the default file extension for outputting our saved states.
    public static final String FILE_EXT = ".tss";
    // constant for the default file extension for writing the statistics to a file.
    public static final String STATS_FILE_EXT = ".txt";
    private static boolean paused; // is the simulation paused?
    private static boolean started; // has the simulation started
    private static UserInterface ui; // the user interface
    private static Map<String, Object> settings; // the main storage container for our environment variables
    private static SimulationThread simulationThread; // the secondary thread for processing the simulation loop
    private static int totalTimeSteps; // the total time steps a simulation had processed. used for output of statistics

    /**
     * Initialisation of the simulation.
     */
    public static void init() {
        Junction.registerJunctionType(BlockedLaneJunction.class);
        Junction.registerJunctionType(TwoLaneJunction.class);
        Junction.registerJunctionType(TurnJunc.class);
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
        new SettingsWindow();
        totalTimeSteps = 0;
    }

    /**
     * Sets a value for one of the environment variables We typically pass a class constant from the simulation class to this method as the "option" parameter
     *
     * @param option the option we want to change.
     * @param value the new value the option will have.
     */
    public static void setOption(String option, Object value) {
        settings.put(option, value);
    }

    /**
     * Gets the value of an environment variable by passing in a class constant pertaining to the value we want to retrieve.
     *
     * @param option the option we want to retrieve the value of.
     * @return an object representation of the value.
     */
    public static Object getOption(String option) {
        return settings.get(option);
    }

    /**
     * Bi-directional pause. A call to this method will invert the current pausing state and update the GUI to reflect this if the simulation is currently running.
     */
    public static void pause() {
        if (isStarted()) { // if we're started.
            paused = !paused;
            if (ui != null) {
                ui.updateGUI();
            }
        }
    }

    /**
     * Synchronised predicate that determines whether the simulation is paused at this current time.
     *
     * @return
     * <code> true </code> if the simulation is paused.
     * <code> false </code> otherwise.
     */
    public static synchronized boolean isPaused() {
        return paused;
    }

    /**
     * Performs necessary calculations that need to update the user interface after environment variables have changed
     */
    public static void settingsChanged() {
        if (ui == null) { // if we don't have a user interface reference
            ui = new UserInterface(); // make one.
            return;
        }
        reset();
        ui.clearJunctionCache();
        ui.updateGUI();
        if (paused == true) { // if we were paused beforehand
            paused = false; // we are not paused anymore
        }
    }

    /**
     * Simulates a single step in the simulation.
     */
    private static void simulateOneStep() {

        // prevent rendering while AI processes
        synchronized (Simulation.class) {

            // core simulation step progress.
            Junction junc = (Junction) getOption(Simulation.JUNCTION_TYPE);
            int carsRatio = (Integer) getOption(Simulation.CAR_RATIO);
            int trucksRatio = (Integer) getOption(Simulation.TRUCK_RATIO);
            junc.distributeNewCars(carsRatio, trucksRatio);
            junc.manageJunction(); // goes through all lanes contained in the junction, and tells each car within each lane to "act" 
            junc.updateDeletions();
            // when cars go outof the end of the junction, they get "deleted" and statistics are incremented.   

            setOption(TIME_STEP, ((int) getOption(TIME_STEP) + 1));
        }
    }

    /**
     * Begin the simulation.
     */
    public static void start() {
        started = true;

        if (simulationThread != null) { // if we already have an instance of a secondary simulation thread.
            simulationThread.terminate(); // tell the thread to terminate
        }

        // create a new thread
        simulationThread = new SimulationThread();
        simulationThread.start();

    }

    /**
     * Synchronised predicate that determines if the simulation is started 
     * @return <code> true </code> if the simulation is currently running. <code> false </code> if it is not.
     */
    public static synchronized boolean isStarted() {
        return started;
    }

    /**
     * Stop the simulation.
     */
    public static void stop() {
        reset();
    }

    /**
     * Gets the total time steps of the last simulation.
     * @return the total time steps of the last simulation.
     */
    public static int getTotalTimeSteps() {
        return totalTimeSteps;
    }

    /**
     * Resets the simulation to default values.
     */
    public static void reset() {

        // cannot stop until the current step
        // and rendering is complete
        synchronized (ui) {
            Junction jn = (Junction) getOption(JUNCTION_TYPE);
            jn.removeVehicles();
            totalTimeSteps = (int) settings.get(TIME_STEP);
            setOption(TIME_STEP, 0);
            started = false;
            paused = false;
            ui.clearJunctionCache();
            ui.updateGUI();
        }

    }

    /**
     * Gets the instance of the SimulationThread
     * @return the SimulationThread
     */
    public static SimulationThread getSimulationThread() {
        return simulationThread;
    }

    /**
     * Inner class for a SimulationThread.
     * This class processes the simulation loop in a separate thread so that UI updating 
     * does not block when we process.
     * 
     * @author Daniel Bond
     */
    public static class SimulationThread extends Thread {

        private boolean terminate = false; // terminate flag.

        /**
         * Sets the terminate flag to true.
         * Indicates that the thread needs to terminate.
         */
        public void terminate() {
            this.terminate = true;
        }

        /**
         * Overridden version of the run method.
         * Processes the simulation main loop.
         */
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
