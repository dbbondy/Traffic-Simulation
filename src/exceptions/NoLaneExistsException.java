/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Dan
 */
public class NoLaneExistsException extends Exception{
    
    private String message;
    
    public NoLaneExistsException(String msg){
        message = msg;
    }
    
    @Override
    public String toString(){
        return message;
    }
    
}
