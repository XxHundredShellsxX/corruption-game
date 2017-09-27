package bin.OverWorldElements;

import java.awt.*;
//base overworld object abstract class with common properties like x,y height width, direction, along with setter and
//getter methods
public abstract class OWObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected DIRECTION direction;

    protected OWObject(int x, int y, int w, int h,DIRECTION d) {
        this.x = x;
        this.y = y;
        height = h;
        width = w;
        direction = d;
    }

    public abstract void render(Graphics2D g, int x, int y);

    public Rectangle getRectangle(){return new Rectangle(x,y,width,height);}

    public void faceRight(){direction = DIRECTION.Right;}
    public void faceLeft(){direction = DIRECTION.Left;}
    public void faceUp(){direction = DIRECTION.Up;}
    public void faceDown(){direction = DIRECTION.Down;};
    public int getX(){
        return x;
    }
    public int getY(){return y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}

}
