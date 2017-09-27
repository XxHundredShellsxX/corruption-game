package bin.BattleElements.BattleObjects;

import bin.Game;

import javax.swing.*;
import java.awt.*;


public class PlayerHeart extends BattleObject {
    private final int ORIGINX = 290, ORIGINY = 340;
    private boolean isHit, hitImage;
    private int hitTimer;
    private Image heart;
    private Image hurtHeart;
    private boolean goingUp, goingDown, goingRight, goingLeft;


    public PlayerHeart() {
        super(290, 290, 0, 0);
        heart = new ImageIcon("src\\resources\\Images\\Heart.png").getImage();
        hurtHeart = new ImageIcon("src\\resources\\Images\\HeartHurt.png").getImage();
        hitBox = new Rectangle((int)x, (int)y, 19 ,20);
    }

    public void reset(){
        goingDown = false;
        goingUp = false;
        goingLeft = false;
        goingRight = false;
        x = ORIGINX;
        y = ORIGINY;
        hitBox.setLocation((int)x, (int)y);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    Rectangle hitBox;

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;	//uses graphics 2D
        //renders text
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(rh);
        g2.setColor(Color.RED);
        if(!hitImage)
            g2.drawImage(heart, (int)x, (int)y, null);
        else
            g2.drawImage(hurtHeart, (int) x, (int) y, null);
        g2.draw(hitBox);
    }



    public void tick(Rectangle box){
        if (goingUp)
            y -= 6;
        else if (goingDown)
            y += 6;
        if (goingRight)
            x += 6;
        else if (goingLeft)
            x -= 6;

        x = Game.clamp((int)x , (int)(box.getMaxX() - hitBox.getWidth()), (int)box.getX());
        y = Game.clamp((int)y , (int)(box.getMaxY() - hitBox.getHeight()), (int)box.getY());
        hitBox.setLocation((int) x, (int) y);
        if(isHit){
            if(Game.getCount() % 10 == 0)
                hitImage = !hitImage;
            if(Game.getCount() > hitTimer){
                isHit = false;
                hitImage = false;
            }
        }
    }

    public void hit(){
        isHit = true;
        hitImage = true;
        hitTimer = 60 + Game.getCount();
//        System.out.println("HIT");
    }



    public boolean isHit(){
        return isHit;
    }

    public void setGoingDown(boolean goingDown) {
        this.goingDown = goingDown;
    }

    public void setGoingLeft(boolean goingLeft) {
        this.goingLeft = goingLeft;
    }

    public void setGoingRight(boolean goingRight) {
        this.goingRight = goingRight;
    }

    public void setGoingUp(boolean goingUp) {
        this.goingUp = goingUp;
    }
}
