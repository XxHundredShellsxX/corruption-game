package bin.BattleElements.BattleObjects;

import java.awt.*;

/**
 * Created by Fahim on 2016-05-15.
 */
public class UniversalObject extends BattleObject {
    public UniversalObject(double x, double y, double velX, double velY, int width, int height, Image sprite){
        super(x, y, velX, velY);
        this.sprite = sprite;
        collisionRect = new Rectangle((int) x, (int) y, width, height);
    }

    @Override
    public void tick(Rectangle box) {
        x += velX;
        y += velY;
        collisionRect.setLocation((int)x, (int)y);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

    }
}
