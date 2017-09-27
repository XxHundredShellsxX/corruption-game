package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.BouncySlime;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * More slime, goes faster
 */
public class HardSlimeBounce extends Attack {

    public HardSlimeBounce() {
        super(500, 2);
        this.attackImage = new ImageIcon("src\\resources\\AttackSprites\\SlimeBall.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if (Game.getCount() % 50 == 0) {
            addObject(new BouncySlime(-150 + Math.random() * 100,
                    250 - Math.random() * 100, Math.random()+3,
                    Math.random() , 0.1 + Math.random()/1.5 ,
                    attackImage));
        }
        for (BattleObject b : objects) {
            b.tick(box);
        }
    }
}
