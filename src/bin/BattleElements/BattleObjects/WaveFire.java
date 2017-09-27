package bin.BattleElements.BattleObjects;

import bin.Game;

import java.awt.*;

/**
 * Created by Gateway on 2016-06-17.
 */
public class WaveFire extends BattleObject{
    private double amp, addY;
    private int count;
    public WaveFire(double x, double y, double velX, double velY, double amp, Image sprite){
        super(x, y, velX, velY);
        this.amp = amp;
        this.sprite = sprite;
        collisionRect = new Rectangle((int)x, (int)y, 45, 15);
        count = 0;
    }

    @Override
    public void tick(Rectangle box) {
        count ++;
        addY = amp*Math.sin((1/30.0 * velX)*(count));
        x += velX;
        y += velY;
        collisionRect.setLocation((int)x, (int)(y+addY));
    }

    @Override
    public void render(Graphics g) {
        if(!isDead) {
            Graphics2D g2 = (Graphics2D) g;    //uses graphics 2D
            //renders text
            RenderingHints rh =
                    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

            rh.put(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2.setRenderingHints(rh);
            g.setColor(Color.YELLOW);
            int imageX = (int) (x + collisionRect.getWidth() / 2 - sprite.getWidth(null) / 2);
            int imageY = (int) (y + addY+ collisionRect.getHeight() / 2 - sprite.getHeight(null) / 2);
            g.drawImage(sprite, imageX, imageY, null);
            g2.draw(collisionRect);
        }
    }
}
