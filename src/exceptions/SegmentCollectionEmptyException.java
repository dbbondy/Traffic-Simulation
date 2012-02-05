/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Dan
 */
public class SegmentCollectionEmptyException extends RuntimeException{
    
    private String message;
    
    public SegmentCollectionEmptyException(String msg){
        message = msg;
    }
    
    @Override
    public String toString(){
        return message;
    }
    
}
