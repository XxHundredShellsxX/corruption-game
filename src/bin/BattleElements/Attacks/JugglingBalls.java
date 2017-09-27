package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.RotatingBall;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Balls go in a circle, while also traveling in the x axis
 */
public class JugglingBalls extends Attack {

    public JugglingBalls(){
        super(1000, 5);
        attackImage = new ImageIcon("src\\resources\\AttackSprites\\RotatingBall.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if(Game.getCount() % 100 == 0) {
            int newX = (int)(Math.random()*100) - 150;

            for (int i = 0; i < 300; i ++) {
                addObject(new RotatingBall(newX, 330, 30, 30,
                        5, 0, 75, 2, i, attackImage));
            }
        }
        super.tick(box);
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = ((Graphics2D) g);
        super.render(g);
        g.setColor(Color.black);
    }
}
