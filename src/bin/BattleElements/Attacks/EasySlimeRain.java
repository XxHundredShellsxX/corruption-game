package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Small slime Droplets fall
 */
public class EasySlimeRain extends Attack {

    public EasySlimeRain(){
        super(500, 3);
        attackImage =  new ImageIcon("src\\resources\\AttackSprites\\SlimeDrop.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if (Game.getCount() % 8 == 0){
            addObject(new UniversalObject(50 + (int)(Math.random()*500),
                    100 + (int)(Math.random()*50), 0.5, 3, 20, 30, attackImage));
        }
        for(BattleObject b: objects){
            b.tick(box);
        }
    }
}
