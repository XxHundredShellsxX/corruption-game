package bin.OverWorldElements;


import java.awt.*;

/**
 * Used By WorldMap in order store next map rectangle, and new player coordinates
 */
public class ConnectingMap {
    private int newPlayerX, newPlayerY;
    private Rectangle travelRect;
    private String nextMap;
    public ConnectingMap(Rectangle travelRect, String nextMap, int newPlayerX, int newPlayerY){
        this.newPlayerX = newPlayerX;
        this.newPlayerY = newPlayerY;
        this.travelRect = travelRect;
        this.nextMap = nextMap;
    }

    public String getNextMap() {
        return nextMap;
    }

    public Rectangle getTravelRect() {
        return travelRect;
    }

    public int getNewPlayerY() {
        return newPlayerY;
    }

    public int getNewPlayerX() {
        return newPlayerX;
    }
}
