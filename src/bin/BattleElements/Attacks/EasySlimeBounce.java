package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.BouncySlime;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Slime bounces across the screen, with gravity
 */
public class EasySlimeBounce extends Attack {
    private boolean offset;

    public EasySlimeBounce(){
        super(500, 2);
        offset = false;
        this.attackImage= new ImageIcon("src\\resources\\AttackSprites\\SlimeBall.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if (Game.getCount() % 100 == 0){
            if(offset)
                addObject(new BouncySlime(-150 + Math.random()*100,
                        250 - Math.random()*100, 3, 0,0.1 + Math.random()/10,
                        attackImage));
            else
                addObject(new BouncySlime(0 + Math.random()*100,
                        250 - Math.random()*100, 3, 0, 0.1 + Math.random()/10,
                        attackImage));
            offset = !offset;
        }
        for(BattleObject b: objects){
            b.tick(box);
        }
    }

}
