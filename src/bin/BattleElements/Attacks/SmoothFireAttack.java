package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.SwordSlash;

import javax.swing.*;

/**
 * Smoothly moves objects from one side of the screen to the other
 */
public class SmoothFireAttack extends Attack{
    public SmoothFireAttack(){
        super(500, 5);
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\BigFire.png").getImage();
    }

    @Override
    public void start() {
        super.start();
        for(int i = 0; i < 300; i += 50){
            addObject(new SwordSlash(i, 350, 125, 100, 2, attackImage));
        }
    }
}
