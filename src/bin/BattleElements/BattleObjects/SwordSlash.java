package bin.BattleElements.BattleObjects;

import java.awt.*;

/**
 * Created by Gateway on 2016-06-14.
 */
public class SwordSlash extends BattleObject {
    private int counter,  ampX, ampY, perX, perY;
    private double speed, addX, addY;

    public SwordSlash(int x, int y, int xTravel, int yTravel, double speed, Image sprite){
        super(x, y, 0, 0);
        this.sprite = sprite;
        ampX = xTravel;
        perX = 100;
        ampY = yTravel;
        this.speed = speed;
        collisionRect = new Rectangle(x, y, 50, 50);
        counter = 0;
    }

    @Override
    public void tick(Rectangle box) {
        counter++;
        x += 5;
        if(x > 600)
            x -= 700;

        addX = ampX*Math.sin((1/30.0 * speed)*(counter));
        addY = ampY*Math.sin((1/30.0 * speed)*(counter));
//        System.out.println(addX);
        collisionRect.setLocation((int)(x + addX), (int)(y + addY));
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;    //uses graphics 2D
        //renders text
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);
        g.setColor(Color.YELLOW);
        int imageX = (int) (x + + addX + collisionRect.getWidth() / 2 - sprite.getWidth(null) / 2);
        int imageY = (int) (y + addY + collisionRect.getHeight() / 2 - sprite.getHeight(null) / 2);
        g.drawImage(sprite, imageX, imageY, null);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
