package bin.BattleElements.Attacks;

import bin.BattleElements.Battle;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * 3 warnings given for upcoming wings, which are more plentiful
 */
public class HardWingSlash extends Attack {
    private int[] nextY;
    //    private Rectangle warning;
    private Image warningImage;
    public HardWingSlash(){
        super(500, 5);
        nextY = new int[3];
//        warning = new Rectangle(0, nextY, 40, 50);
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\Wing.png").getImage();
        warningImage =  new ImageIcon("src\\resources\\AttackSprites\\Warning.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
//        System.out.println(nextCount);
//        System.out.println(Game.getCount());
//        if(Game.getCount() == nextCount-1)
//            nextY = 250 + (int)(Math.random()*200);
        if(Game.getCount() % 60 == 0) {
            addObject(new UniversalObject(-100, nextY[0], 8, Math.random(), 70, 50, attackImage));
            nextY[0] = 250 + (int)(Math.random()*200);
        }
        if((Game.getCount()+20) % 60 == 0) {
            addObject(new UniversalObject(-100, nextY[1], 8, Math.random(), 70, 50, attackImage));
            nextY[1] = 250 + (int)(Math.random()*200);
        }
        if((Game.getCount()+40) % 60 == 0) {
            addObject(new UniversalObject(-100, nextY[2], 8, Math.random(), 70, 50, attackImage));
            nextY[2] = 250 + (int)(Math.random()*200);
        }



        super.tick(box);
    }

    @Override
    public void start() {
        super.start();
        nextY[0] = 250 + (int)((Math.random()*200));
        nextY[1] = 250 + (int)((Math.random()*200));
        nextY[2] = 250 + (int)((Math.random()*200));
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.render(g);
        g2.setComposite(Battle.makeTransparent(0.6f));
        g2.drawImage(warningImage, 0, nextY[0], null);
        g2.drawImage(warningImage, 0, nextY[1], null);
        g2.drawImage(warningImage, 0, nextY[2], null);
        g2.setComposite(Battle.makeTransparent(1));
//        ((Graphics2D) g).draw(warning);

    }
}
