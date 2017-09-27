package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.Feather;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Feathers fall similarly with how they do in real life
 * (velocity of y is sinusoidal, while position of x
 * is sinusoidal)
 */
public class EasyFeatherFall extends Attack {
    private int startCount;
    public EasyFeatherFall(){
        super(1000, 3);
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\Feather.png").getImage();
        addObject(new Feather(300, 100, 200, 1, attackImage));
        startCount = Game.getCount();

    }

    @Override
    public void tick(Rectangle box) {
        super.tick(box);
        if(Game.getCount() % 200 == 0){
            addObject(new Feather(100 + (int)(Math.random()*400), 100,
                    200, 1, attackImage));
        }
    }
}
