package bin.BattleElements.BattleObjects;

import java.awt.*;

/**
 * Created by Gateway on 2016-06-15.
 */
public class RotatingBall extends BattleObject{
    private double radius, speed, angle;
    public RotatingBall(int x, int y, int height, int width, double velX, double velY,
                        double radius, double speed, double initialAngle, Image sprite){
        super(x, y, velX, velY);
        this.sprite = sprite;
        this.radius = radius;
        this.speed = Math.toRadians(speed);
        this.angle = Math.toRadians(initialAngle);
        collisionRect = new Rectangle(width, height);

    }

    @Override
    public void tick(Rectangle box) {
        x += velX;
        y += velY;
        angle += speed;
        setPosition();

    }

    private void setPosition(){
        collisionRect.setLocation(
                (int)(x + radius*(Math.cos(angle))),
                (int)(y + radius*(Math.sin(angle))));
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
        int imageX = (int)( x + radius*(Math.cos(angle)) + collisionRect.getWidth()/2 - sprite.getWidth(null)/2);
        int imageY = (int)( y + radius*(Math.sin(angle)) + collisionRect.getHeight()/2 - sprite.getHeight(null)/2);
        g.drawImage(sprite, (int)(x + radius*(Math.cos(angle))),
                (int)(y + radius*(Math.sin(angle))), null);
//        g2.draw(collisionRect);
    }

    @Override
    public void addRadius(double radius) {
        this.radius += radius;
    }
}
