package bin.BattleElements.BattleObjects;

import java.awt.*;

/**
 * Base Class for all BattleObjects, which holds basic fields such as position
 */
public abstract class BattleObject {

    protected double x, y, velX, velY;
    protected Image sprite;
    protected Rectangle collisionRect;
    protected boolean isDead;

    public BattleObject(double x, double y, double velX, double velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        isDead = false;
    }

    public void render(Graphics g) {
        if (!isDead) {
            Graphics2D g2 = (Graphics2D) g;
            RenderingHints rh =
                    new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

            rh.put(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2.setRenderingHints(rh);
            g.setColor(Color.YELLOW);
            int imageX = (int) (x + collisionRect.getWidth() / 2 - sprite.getWidth(null) / 2);
            int imageY = (int) (y + collisionRect.getHeight() / 2 - sprite.getHeight(null) / 2);
            g.drawImage(sprite, imageX, imageY, null);
        }
    }

    public abstract void tick(Rectangle box);

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void addRadius(double radius) {
    }

    public void setVX(double velX) {
        this.velX = velX;
    }

    public void setVY(double velY) {
        this.velY = velY;
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public boolean isFinished() {
        return false;
    }

    public boolean isOutOfBounds() {
        return false;
    }

    public void die() {
        isDead = true;
    }

    public boolean getIsDead() {
        return isDead;
    }
}
