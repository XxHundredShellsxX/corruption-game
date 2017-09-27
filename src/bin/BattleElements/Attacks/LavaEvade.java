package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.WarningBars;

import javax.swing.*;

/**
 * Pillars of fire are first are warned to be at a location(transparency),
 * they they appear and move up and down.
 */
public class LavaEvade extends Attack{

    public LavaEvade(){
        super(750, 7);
        attackImage = new ImageIcon("src\\resources\\AttackSprites\\FirePillar.png").getImage();

    }

    @Override
    public void start() {
        super.start();
        for(int i = 250; i < 451; i += 100){
            addObject(new WarningBars(50, i, 500, 15, 0, 2, attackImage));
        }
    }
}
