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
    private static UserInterface ui;
    private static Map<String, Object> settings;
    private static SimulationThread simulationThread;

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
    }

    public static boolean isPaused() {

        return paused;
    }

    public static void Simulate() {
        if (!simulationThread.isAlive()) {
            simulationThread = new SimulationThread();
        }
        simulationThread.start();
        //SimulationStats.publishStats();
    }

    public static void settingsChanged() {
        if (ui == null) {
            ui = new UserInterface();
            return;
        }
        if(paused == true){
            paused = false;
        }
    }

    private static void simulateOneStep() {
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

    public static SimulationThread getPausedThread() {
        return simulationThread;
    }

    public static class SimulationThread extends Thread {

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
                }
                try {
                    Thread.sleep(200);
                    simulateOneStep();
                    ui.updateGUI(); // more precisely invoke the repaint() method
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}

/*
 * TODO
 * 1. create automatic pausing when click on "change settings" and automatic unpausing when the change settings window is closed
 *
 *
 *
 * 
 */
