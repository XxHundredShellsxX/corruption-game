package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.Feather;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * More Feathers fall faster
 */
public class HardFeatherFall extends Attack {
    private int startCount;
    public HardFeatherFall(){
        super(750, 5);
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\Feather.png").getImage();
        addObject(new Feather(300, 100, 200, 1, attackImage));
        startCount = Game.getCount();

    }

    @Override
    public void tick(Rectangle box) {
        super.tick(box);
        if(Game.getCount() % 100 == 0){
            addObject(new Feather(50 + (int)(Math.random()*500), 200,
                    150 + (int)(Math.random()*75),
                    1+(Math.random()), attackImage));
        }
    }
}
