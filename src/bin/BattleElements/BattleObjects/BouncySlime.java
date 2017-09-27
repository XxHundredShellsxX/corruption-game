package bin.BattleElements.BattleObjects;

import java.awt.*;

/**
 *
 */
public class BouncySlime extends BattleObject {
    public double accelY;

    public BouncySlime(double x, double y, double velX, double velY, double accelY, Image sprite){
        super(x, y, velX, velY);
        this.sprite = sprite;
        this.accelY = accelY;
        this.sprite = sprite;
        collisionRect = new Rectangle((int)x, (int)y, 50, 50);
    }


    @Override
    public void tick(Rectangle box) {
        x += velX;
        velY += accelY;
        y += velY;
        if(y > box.getMaxY()){
            y -= velY;
            velY *= -1;
        }

        collisionRect.setLocation((int)x, (int)y);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
