package bin.BattleElements.BattleObjects;

import java.awt.*;

/**
 * ball which bounces within the walls
 */
public class BouncyBall extends BattleObject{
    public BouncyBall(int x, int y, int width, int height, double velX, double velY, Image sprite){
        super(x, y, velX, velY);
        collisionRect = new Rectangle(x, y, width, height);
        this.sprite = sprite;


    }

    @Override
    public void tick(Rectangle box){
        x += velX;
        y += velY;
        if (x > (int)(box.getMaxX() - collisionRect.getWidth()) ||
                x < (int)box.getX()){
            velX *= -1;
        }
        if (y > (int)(box.getMaxY() - collisionRect.getHeight()) ||
                y < (int)box.getY()){
            velY *= -1;
        }

        collisionRect.setLocation((int)x, (int)y);
    }




    public void setSize(int width, int height){
        collisionRect.setSize(width, height);
    }

}
