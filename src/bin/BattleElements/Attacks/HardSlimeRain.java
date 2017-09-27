package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * More rain falls faster
 */
public class HardSlimeRain extends Attack{
    public HardSlimeRain(){
        super(500, 3);
        attackImage =  new ImageIcon("src\\resources\\AttackSprites\\SlimeDrop.png").getImage();
//        this.interval = interval;
    }

    @Override
    public void tick(Rectangle box) {
        if (Game.getCount() % 5 == 0){
            addObject(new UniversalObject(50 + (Math.random()*500),
                    100 + (Math.random()*50), Math.random()-0.5 ,
                    3 + Math.random(), 20, 30, attackImage));
        }
        for(BattleObject b: objects){
            b.tick(box);
        }
    }
}
