package controller;
import java.util.*;
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
    
    
    
    public static void start(){
        started = (!started);
    }
    
    public static boolean isStarted(){
        return started;
    }
    
    
    
    
}
