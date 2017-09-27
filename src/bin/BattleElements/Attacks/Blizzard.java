package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Snow falls at regular intervals, at set distances apart
 */
public class Blizzard extends Attack{

    public Blizzard(){
        super(500, 8);
        attackImage =  new ImageIcon("src\\resources\\AttackSprites\\SnowFlake.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if(Game.getCount()%12 == 0){
            int xOffset = (int)(Math.random()*100) -50;
            for(int i = 0; i < 600; i+= 150){
                addObject(new UniversalObject(i + xOffset, 0, Math.random()/4, 5, 20, 20, attackImage));
            }
        }

        super.tick(box);
    }
}
