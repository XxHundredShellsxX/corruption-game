package bin.BattleElements.Attacks;

import bin.BattleElements.Battle;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * a slashing move is warned to be at a specific location,
 * which is then followed my a slash
 */
public class QuickSlash extends Attack{
    private int nextX;
    private int nextCount;
    private Image warning;
    public QuickSlash(){
        super(750, 10);
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\QuickSlash.png").getImage();
        this.warning =  new ImageIcon(
                "src\\resources\\AttackSprites\\QuickSlashWarning.png").getImage();

    }

    public void tick(Rectangle box) {
//        System.out.println(nextY);
//        System.out.println(Game.getCount());
//        if(Game.getCount() == nextCount) {
//            System.out.println("he");
//            nextCount += 70;
//            addObject(new UniversalObject(nextY - 350, -100, 15, 15, 30, 30, attackImage));
//            nextY = 50 + (int)(Math.random()*200);
////            warning.setLocation(0, nextY);
//        }
        if(Game.getCount() + 1 == nextCount){
            nextX = 50 + (int)(Math.random()*500);
        }
        if(Game.getCount() == nextCount) {
//            System.out.println("he");
            nextCount += 40;
            addObject(new UniversalObject(nextX - 350, -100, 15, 15, 30, 30, attackImage));

//            warning.setLocation(0, nextX);
        }

        super.tick(box);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setComposite(Battle.makeTransparent(0.2f));
        g.drawImage(warning, nextX, 250, null);
        g2.setComposite(Battle.makeTransparent(1));
//        ((Graphics2D) g).draw(warning);

    }
    public void start() {
        super.start();
        nextCount = Game.getCount() + 1;
        nextX = 250 + (int)((Math.random()*500));
    }
}
