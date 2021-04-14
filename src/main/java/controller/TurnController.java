package controller;

/**
 *
 * @author David
 */
public class TurnController {
    private String turn = "blue";
    private String next = "yellow";
    
    public void toggle(){
        String aux = turn;
        turn = next;
        next = aux;
    }
    public String getTurn(){
        return turn;
    }
    public String getNext(){
        return this.next;
    }
}
