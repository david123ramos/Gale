/*
 *Direciona os eventos de acordo com a mensagem chegada do frontedn
 */
package controller;

import messages.Message;

/**
 *
 * @author David
 */
public class EventController {
    public static void direct(GaleController c, Message m){
        switch(m.getEventName()) {
            case "onMove": 
                
                break;
        }
    }
}
