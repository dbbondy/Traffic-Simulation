package controller;

import java.util.HashMap;
import java.util.Map;
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
    private SettingsWindow settingsWindow;
    private UserInterface ui;
    private static Map<String, Object> settings;
    private static PausedThread pausedThread;
    private static SimulationThread simThread;

    public Simulation() {
        settings = new HashMap<String, Object>();
        settings.put(DENSITY, 0);
        settings.put(AGGRESSION, 0);
        settings.put(CAR_RATIO, 0);
        settings.put(TRUCK_RATIO, 0);
        settings.put(JUNCTION_TYPE, null);
        settings.put(TIME_STEP, 0);
        paused = false;
        started = false;
        pausedThread = new PausedThread();
        simThread = new SimulationThread();
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
    }

    public static boolean isPaused() {

        return paused;
    }

    public static void Simulate() {
        pausedThread.start();

        //SimulationStats.publishStats();

    }

    private void simulateOneStep() {
        //core simulation step progress.

        /*
         * Junction junc = (Junction)getOption(Simulation.JUNCTION_TYPE); int carsRatio = (Integer)getOption(Simulation.CAR_RATIO); int trucksRatio = (Integer)getOption(Simulation.TRUCK_RATIO);
         * junc.distributeNewCars(carsRatio, trucksRatio); junc.update(); //goes through all lanes contained in the junction, and tells each car within each lane to "act" junc.updateDeletions();
         * //when cars go out of the end of the junction, they get "deleted" and statistics are incremented.
         *
         * temporarily commented out
         */

        int currentStep = (Integer) getOption(TIME_STEP);
        currentStep++;
        setOption(TIME_STEP, currentStep);

    }

    public static void start() {
        started = (!started);
    }

    public static boolean isStarted() {
        return started;
    }

    public static PausedThread getPausedThread() {
        return pausedThread;
    }

    public static SimulationThread getSimThread() {
        return simThread;
    }

    public class PausedThread extends Thread {

        @Override
        public void run() {
            while (isStarted()) {
                synchronized (this) {
                    while (isPaused()) {
                        try {
                            this.wait();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                    try {
                        this.wait(100);
                        simulateOneStep();
                        ui.updateGUI(); // more precisely invoke the repaint() method
                    } catch (InterruptedException ie) {
                    }
                }
            }
        }
    }

    public class SimulationThread extends Thread {

        @Override
        public void run() {
            while (settingsWindow.isVisible()) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            ui = new UserInterface();
        }
    }
}

/*TODO
 * 1. Remove SimulationThread and either use view->view calls or use a method on Simulation 
   that indirectly does the view->view call. 
	    * changeSettings() = end UI and start SettingsWindow
	    * settingsChanged() = end settingsWindow and start UI

2. The PausedThread (rename this) should end at the end of a simulation (as it does)
   and a new thread should be started at the start of each new simulation. 
   
3. this.wait() should be replaced by Thread.sleep() as this.wait() isn't designed 
   to be used just for making a thread idle for a period of time. It might have other
   unexpected behaviour on some systems. You will also have to be careful
   about the synchronized(this) block around that code. It needs to only be around the 
   while (isPaused()) block instead. It has nothing to do with the sleeping or simulateOneStep
 */






