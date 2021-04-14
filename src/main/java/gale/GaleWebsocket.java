package gale;

import controller.GaleController;
import java.io.IOException;
import java.util.List;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import messages.Message;
import messages.MessageEncoder;
import messages.MessageDecoder;

/**
 *
 * @author David
 */

@ServerEndpoint(value = "/royale", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class GaleWebsocket {
    
    public static Session s1;
    public static Session s2;
    private static GaleController controller = new GaleController();;
    
    @OnOpen
    public void onOpen(Session session) throws IOException, EncodeException {
        
        if(s1 == null) {
            s1 = session;
           
        }else if(s2 == null) {
            s2 = session;

            GaleController.isRunning = true;
            Message startgame = new Message("onStart", controller.getBoard());
            Message turnChange = new Message("onTurnChange", controller.getTurn());
            s1.getBasicRemote().sendObject(new Message("whoiam", controller.getTurn()));
            s2.getBasicRemote().sendObject(new Message("whoiam", controller.getNextTurn()));
            broadcast(startgame, turnChange);
        }
    }

    @OnMessage
    public void onMessage(Session session, Message m) throws IOException, EncodeException {
            
        if(GaleController.isRunning) {
            switch(m.getEventName()) {
                case "onMove": 
                    List arr = (List) m.getData(); //get coords
                    
                    Message response = new Message("onMoveUp",controller.move(arr, session));
                    Message turnChange = new Message("onTurnChange", controller.getTurn());
                    broadcast(response, turnChange);
                    
                    if(GaleController.hasWinner){
                        
                        Object[] args = new Object[2];
                        
                        args[0] = controller.getBoard();
                        args[1] = GaleController.Winner;
                        Message winner = new Message("onWin", args);
                        broadcast(winner);
                    }
                    break;
                    
                //abandono de partida
                case "onLeave":
                    
                    controller.leaveGame(session);
                    Object[] args = new Object[2];
                    args[0] = controller.getBoard();
                    args[1] = GaleController.Winner;
                    Message winner = new Message("onWin", args);
                    broadcast(winner);
                    
                    break;
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        s1 = null;
        s2 = null;
        controller = new GaleController();
        GaleController.isRunning = false;
        GaleController.Winner = "";
        GaleController.hasWinner = false;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Closing websocket");
    }
    
    
    private static void broadcast(Message... messages)  throws IOException, EncodeException {
        
        for(Message m:  messages) {
            s1.getBasicRemote().sendObject(m);
            s2.getBasicRemote().sendObject(m);
        }   
    }
}
