/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element.exception;

/**
 *
 * Exception for attempt of creating not complite Human.
 */
public class HumanPreperationException 
extends Exception
{
    public HumanPreperationException(String msg)
    {
        super(msg);
    }
}
