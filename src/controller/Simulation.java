package controller;
import java.util.*;
import model.SimulationStats;
import model.junctions.Junction;
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
   
    
    private static Map<String, Object> settings;
    
    public Simulation(){
        settings = new HashMap<String, Object>();
        settings.put(DENSITY, 0);
        settings.put(AGGRESSION, 0);
        settings.put(CAR_RATIO, 0);
        settings.put(TRUCK_RATIO, 0);
        settings.put(JUNCTION_TYPE, null);
        settings.put(TIME_STEP, 0);
        paused = false;
        started = false;
        
    }
    
    
    public static void setOption(String option, Object value){
        settings.put(option, value);
    }
    
    public static Object getOption(String option){
        return settings.get(option);
    }
    
    public static void pause(){
        paused = !paused;
    }
    
    public static boolean isPaused(){
       
        return paused;
    }
    
    public void Simulate(){
        while(started == true){
            simulateOneStep();
            if(paused == true){
                while(paused == true){
                }
            }
        }
        //SimulationStats.publishStats();
        
    }
    
    private void simulateOneStep(){
        //core simulation step progress.
        
       /* Junction junc = (Junction)getOption(Simulation.JUNCTION_TYPE);
        int carsRatio = (Integer)getOption(Simulation.CAR_RATIO);
        int trucksRatio = (Integer)getOption(Simulation.TRUCK_RATIO);
        junc.distributeNewCars(carsRatio, trucksRatio);
        junc.update(); //goes through all lanes contained in the junction, and tells each car within each lane to "act"
        junc.updateDeletions(); //when cars go out of the end of the junction, they get "deleted" and statistics are incremented.
       
        */
        
        int currentStep = (Integer)getOption(TIME_STEP);
        currentStep++;
        setOption(TIME_STEP, currentStep);
        
    }
    
    
    public static void start(){
        started = (!started);
    }
    
    public static boolean isStarted(){
        return started;
    }
    
    
    
    
}