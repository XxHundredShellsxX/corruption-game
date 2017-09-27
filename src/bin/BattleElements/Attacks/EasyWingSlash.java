package bin.BattleElements.Attacks;

import bin.BattleElements.Battle;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class EasyWingSlash extends Attack {
    private int nextY;
    private int nextCount;
    private Image warningImage;
    public EasyWingSlash(){
        super(500, 5);
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\Wing.png").getImage();
        warningImage =  new ImageIcon("src\\resources\\AttackSprites\\Warning.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if(Game.getCount() == nextCount) {
            nextCount += 70;
            addObject(new UniversalObject(-100, nextY, 10, 0, 70, 50, attackImage));
            nextY = 250 + (int)(Math.random()*200);
        }


        super.tick(box);
    }

    @Override
    public void start() {
        super.start();
        nextCount = Game.getCount() + 80;
        nextY = 250 + (int)((Math.random()*200));
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.render(g);
        g2.setComposite(Battle.makeTransparent(0.7f));
        g2.drawImage(warningImage, 0, nextY, null);
        g2.setComposite(Battle.makeTransparent(1));

    }
}
