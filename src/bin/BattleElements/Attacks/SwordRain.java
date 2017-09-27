package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Swords rain down upon the player, similarly to Slime rain, but projectiles are much bigger
 */
public class SwordRain extends Attack {

    public SwordRain(){
        super(950, 5);
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\FallingSword.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if (Game.getCount() % 25 == 0){
            addObject(new UniversalObject(50 +(int)(Math.random()*500), 100, 0,  5, 50, 120, attackImage));
        }
        for(BattleObject b: objects){
            b.tick(box);
        }
    }

}
