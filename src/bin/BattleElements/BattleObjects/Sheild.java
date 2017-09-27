package bin.BattleElements.BattleObjects;

import bin.DIRECTION;

import java.awt.*;

/**
 * Created by Fahim on 2016-05-19.
 */
//TODO: GET RID OF THIS SHITTY ASS CLASS
public class Sheild extends BattleObject {
    private int travelDistance;

    public Sheild(int x, int y, DIRECTION direction, int travelDistance, Image sprite){
        super(x, y, 0, 0);
        this.sprite = sprite;
        if(direction == DIRECTION.Right)
            velX = 5;
        else if (direction == DIRECTION.Left)
            velX = -5;
        this.travelDistance = travelDistance;
        collisionRect = new Rectangle(x,y,100, 200);
    }

    @Override
    public void tick(Rectangle box) {
        x += velX;
        travelDistance -= Math.abs(velX);
        collisionRect.setLocation((int)x, (int)y);
    }



    @Override
    public boolean isFinished() {
        return travelDistance == 0;
    }
}
