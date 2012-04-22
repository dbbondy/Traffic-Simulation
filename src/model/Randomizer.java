package model;

import java.util.Random;

/**
 * Class for a system wide random number generator
 * @author Daniel Bond
 */
public class Randomizer {
    
    private static Random rnd; // the random number generator
    
    private Randomizer(){}
    
    /**
     * Gets the instance of the random number generator
     * If no instance exists, then we create one and return it
     * @return the random number generator
     */
    public static Random getInstance(){
        if(rnd == null){
            rnd = new Random(System.nanoTime());
        }
        return rnd;
    }
}
