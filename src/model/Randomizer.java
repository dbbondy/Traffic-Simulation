/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Random;

/**
 *
 * @author Dan
 */
public class Randomizer {
    
    private static Random rnd;
    
    private Randomizer(){}
    
    public static Random getInstance(){
        if(rnd == null){
            rnd = new Random(System.nanoTime());
        }
        return rnd;
    }
}
