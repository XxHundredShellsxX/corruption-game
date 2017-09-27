package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.WaveFire;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Fire DIsks Sinusoidally move in the horizontal direction(right)
 */
public class SinusoidalFire extends Attack{

    public SinusoidalFire(){
        super(750, 6);
        attackImage = new ImageIcon("src\\resources\\AttackSprites\\FireDisk.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if(Game.getCount()%30 == 0)
            addObject(new WaveFire(0,300 + Math.random()*75, 2, 0, 100 + Math.random()*50, attackImage));
        super.tick(box);
    }
}
