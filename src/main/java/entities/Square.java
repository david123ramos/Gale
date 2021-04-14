package entities;

/**
 *
 * @author David
 */
public class Square {
    
    private int posx;
    private int posy;
    private String color;
    
    private String info; //generic info
    public Square(int posY, int posX){
        this.posx = posX;
        this.posy = posY;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
