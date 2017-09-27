package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Sajid on 2016-06-27.
 */
public class ShootingStar extends Attack{


    public ShootingStar(){
        super(750, 5);
        attackImage =  new ImageIcon("src\\resources\\AttackSprites\\SnowFlake.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if(Game.getCount()%50 == 0){
            addObject(new UniversalObject(600 + Math.random()*100,250,-6,2, 20, 20, attackImage));
        }
        Iterator<BattleObject> it = objects.iterator();
        ArrayList<BattleObject> add = new ArrayList<>();
        while (it.hasNext()) {
            BattleObject b = it.next();
            //random points where the comet might explode
            double explodeY = 400 + Math.random()*200;
            double explodeX = 50 + Math.random()*300;
            //when the comet meets that point and its not already destroyed, and it is a comet (width is 20, the exploding ones width is 21)
            if((b.getCollisionRect().getMaxY() > explodeY  || b.getCollisionRect().getMaxX() < explodeX)&& !b.getIsDead() && b.getCollisionRect().getWidth() == 20) {
                //10 stars explode in all directions from point where comet disappeared
                double offset = Math.random()*36;
                for(double angle = 0; angle <= 360; angle += 36){
                    add.add(new UniversalObject(b.getX(),b.getY(),6*Math.cos(angle+offset),6*Math.sin(angle+offset),21,21,attackImage));
                }
                //the comet is dead
                b.die();
            }
        }
        objects.addAll(add);

        super.tick(box);
    }
}
