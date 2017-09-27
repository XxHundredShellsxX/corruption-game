package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.RotatingBall;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * pillars of fire rotate around a central point, whilst moving
 * as a whole horizontally
 */
public class RotatingPillars extends Attack {

    public RotatingPillars(){
        super(1000, 5);
        attackImage = new ImageIcon("src\\resources\\AttackSprites\\SmallFire.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if (Game.getCount() % 200 == 0) {
            int newX = (int)(Math.random()*50)-100;
            for (int i = -120; i < 120; i+=5) {
                addObject(new RotatingBall(newX, 330, 15, 15,
                        2, 0, i, 1, 90, attackImage));
            }
        }
        else if((Game.getCount() + 100) % 200 == 0){
            int newX = 600 + (int)(Math.random()*50);
            for (int i = -120; i < 120; i+=5) {
                addObject(new RotatingBall(newX, 330, 15, 15,
                        -2, 0, i, 1, 90, attackImage));
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
