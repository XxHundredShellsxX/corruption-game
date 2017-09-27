package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.BouncyBall;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Ball gets bigger as it bounces
 */
public class BigBounce extends Attack {

    public BigBounce(){
        super(500, 5);
        attackImage =  new ImageIcon("src\\resources\\AttackSprites\\SmallBall.png").getImage();
        addObject(new BouncyBall(100, 350, 20, 20, 2, 2, attackImage));
        addObject(new BouncyBall(500, 400, 20, 20, 2, 2, attackImage));
        addObject(new BouncyBall(300, 350, 20, 20, 2, 2, attackImage));
    }

    @Override
    public void tick(Rectangle box){
        for(BattleObject b : objects){
            b.tick(box);
            if(Game.getCount() % 10 == 0)
                b.getCollisionRect().setSize(
                        (int)b.getCollisionRect().getHeight() + 1,
                        (int)b.getCollisionRect().getWidth() + 1
                );
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);
        g.setColor(Color.YELLOW);
        for(BattleObject b: objects) {
            int x = (int)b.getX();
            int y = (int)b.getY();
            Rectangle collisionRect = b.getCollisionRect();
            int imageX = (int) (x + collisionRect.getWidth() / 2 - attackImage.getWidth(null) / 2);
            int imageY = (int) (y + collisionRect.getHeight() / 2 - attackImage.getHeight(null) / 2);
            g.drawImage(attackImage, imageX, imageY, (int)collisionRect.getWidth(), (int)collisionRect.getHeight(), null);
        }
    }
}
