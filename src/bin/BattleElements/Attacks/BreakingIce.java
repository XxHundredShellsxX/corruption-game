package bin.BattleElements.Attacks;

import bin.BattleElements.Battle;
import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Ice shards fall, and break into 2
 */
public class BreakingIce extends Attack{
    Image brokenIce;

    public BreakingIce(){
        super(750, 5);
        attackImage = new ImageIcon("src\\resources\\AttackSprites\\FallingIceShard.png").getImage();
        brokenIce = new ImageIcon("src\\resources\\AttackSprites\\BreakingIceShard.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if(Game.getCount()%50 == 0){
            addObject(new UniversalObject(50 + (int)(Math.random()*500), 0, Math.random()/2, 0, 36, 65, attackImage));
        }
        Iterator<BattleObject> it = objects.iterator();
        ArrayList<BattleObject> add = new ArrayList<>();
        // when big shard(we identify since their velocity X is low, and make is "dead", while spawning 2 new shards_
        while (it.hasNext()) {
            BattleObject b = it.next();
            if(b.getCollisionRect().getMaxY() > box.getMaxX() && Math.abs(b.getVelX()) < 1.9 && !b.getIsDead()) {
                add.add(new UniversalObject(b.getX(), b.getY(), 3, -b.getVelY(), 35, 35, brokenIce));
                add.add(new UniversalObject(b.getX(), b.getY(), -3, -b.getVelY(), 35, 35, brokenIce));
                b.die();
            }
            b.setVY(b.getVelY()+0.3);
        }
        objects.addAll(add);

        super.tick(box);
    }
}
