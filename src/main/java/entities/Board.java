package entities;

/**
 *
 * @author David
 */
public class Board {
    private int rows;
    private int cols;
    private Square[][] board;
    private final String player1Color = "yellow";
    private final String player2Color = "blue" ;
    
    public Board(int rows, int cols) {
        
        this.board = new Square[rows][cols];
        for(int i =0; i<rows; i++) {
            for(int j =0; j < cols; j++) {
                Square s = new Square(j, i);
                if(i % 2 != 0 && j % 2 == 0 ) {
                    s.setColor(this.player1Color);
                }else if(i % 2 == 0 && j % 2 != 0) {
                    s.setColor(this.player2Color);
                }
                this.board[i][j] = s;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Square[][] getBoard() {
        return board;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }
}
