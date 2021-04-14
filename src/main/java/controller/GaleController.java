package controller;


import entities.Board;
import entities.Square;
import gale.GaleWebsocket;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.websocket.Session;

/**
 *
 * @author David
 */
public class GaleController {
    private Board board;
    private final int rows = 11; 
    private final int cols = 11; 
    private final TurnController turnController = new TurnController();
    public static boolean isRunning = false;
    public static boolean hasWinner = false;
    public static String Winner;
    public GaleController(){
        this.board = new Board(rows, cols);
    }
    
    public Square[][] getBoard(){
        return this.board.getBoard();
    }
    
    
    /**
     * Responsável por realizar o movimento nas peças
     * @param moves
     * @param player
     * @return 
     */
    public Square[][] move(List moves, Session player){
        
        int x = Integer.parseInt(moves.get(0).toString());
        int y = Integer.parseInt(moves.get(1).toString());
        Square q = null;
        
        if(this.isSafe(x, y)){
            q  = this.board.getBoard()[x][y];
        }
        
        //verifica quem fez a jogada
        if( getTurn().equals("yellow") && player.equals(GaleWebsocket.s2) ||
            getTurn().equals("blue") && player.equals(GaleWebsocket.s1) ){
            
            //se certifica que a peça que está sendo pintada está em branco.
            if(q.getColor() == null) {
                q.setColor(getTurn());
                GaleController.hasWinner = this.hasAWinner();
                
                
                if(!GaleController.hasWinner) this.clearPath();
                
                turnController.toggle();
            }
        }
        
        
        return this.board.getBoard();
    }
    
    public String getTurn(){
        return turnController.getTurn();
    }
    public boolean hasAWinner() {
        return this.verifyPath(this.board.getBoard());
    }
    private boolean isSafe(int x, int y){
        return x >=0 && x < this.rows && y >= 0 && y < this.cols;
    }
    
    private boolean verifyPath(Square[][] graph) {
        
        
        List<List<Square>> shadow = new ArrayList<>();
        
        for (Square[] graph1 : graph) {
            List<Square> cols = new ArrayList<>();
            for (int j = 0; j < graph[0].length; j++) {
                cols.add(graph1[j]);
            }
            shadow.add(cols);
        }
        
        //return findPath(shadow, 0);
        boolean foundPath = bfs(shadow);
        
        if(foundPath){
            this.setSquarePathNum(shadow);
        }
        
        return foundPath;
    }
    
    
    public boolean bfs(List<List<Square>> mtx) {
        
        
        List<Square> queue = new ArrayList<>();
        int dir[][] = { {0,1}, {0,-1}, {1,0}, {-1,0}};
        
        if(getTurn().equals("blue")){
            queue.addAll( getLine(mtx, 0) );
        }else if(getTurn().equals("yellow")){
            queue.addAll( getColumn(mtx, 0) );
        }
        
        
        while(queue.size() >0){
            Square a = queue.remove(0);
            
            if(getTurn().equals("blue")){
                if(a.getPosx() == rows -1){
                    Winner = getTurn();
                    return true;
                }
            }else if(getTurn().equals("yellow")){
                if(a.getPosy() == cols -1){
                    Winner = getTurn();
                    return true;
                }
            }
            
            
            for(int i=0; i<4; i++){
                int x = a.getPosx() + dir[i][0];
                int y = a.getPosy() + dir[i][1];
                
                if(isSafe(x, y) && !"path".equals(board.getBoard()[x][y].getInfo()) && getTurn().equals( board.getBoard()[x][y].getColor() )){
                    a.setInfo("path");
                    board.getBoard()[x][y].setInfo("path");
                    queue.add(board.getBoard()[x][y]);
                }
                
            }   
        }
        
        return false;
    }
    
    private List<Square> getLine(List<List<Square>> mtx, int line){
        return mtx.get(line).stream().filter(el -> getTurn().equals( el.getColor())).collect(toList());
    }
    private List<Square> getColumn(List<List<Square>> mtx, int column){
        List<Square> response = new ArrayList<>();
        mtx.forEach(row -> {
            
            if(getTurn().equals(row.get(column).getColor())){
                response.add(row.get(column));
            }
            
        });
        
       return response;
    }
    
    private void setSquarePathNum(List<List<Square>> mtx){
        int counter =0;
        
        
        for(List<Square> row: mtx){
            for(Square s: row) {
                if("path".equals(s.getInfo())){
                    s.setInfo("path."+counter);
                    counter++;
                }
            }
        }
    }
     
    
    /**
     * Limpa as marcações feitas pelo algoritmo de busca.
     */
    private void clearPath(){
        Square[][] aux= this.board.getBoard();
        for(int i =0; i< rows; i++) {
            for(int j =0; j < cols; j++) {
                aux[i][j].setInfo("");
            }
        }
    }
    
    public String getNextTurn(){
        return this.turnController.getNext();
    }
    
    public void leaveGame(Session s){
        GaleController.hasWinner = true;
        GaleController.isRunning = false;
        if(s.equals(GaleWebsocket.s1)) {
            GaleController.Winner = "yellow";
        }else{
            GaleController.Winner = "blue";
        }
    }
}
