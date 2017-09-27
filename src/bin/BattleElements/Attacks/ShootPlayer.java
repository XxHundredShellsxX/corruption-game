package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.UniversalObject;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Numerous random projectiles appear from abuve and from the right
 */
public class ShootPlayer extends Attack{
    Image verticalAttackImage;
    public ShootPlayer(){
        super(750, 6);
        attackImage =  new ImageIcon("src\\resources\\AttackSprites\\HorizontalIceShard.png").getImage();
        verticalAttackImage = new ImageIcon("src\\resources\\AttackSprites\\VerticalIceShard.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if(Game.getCount()%30 == 0) {
            addObject(new UniversalObject(50 + (int) (Math.random() * 500), 0, Math.random() / 2, 6,
                    15, 30, verticalAttackImage));
        }
        if(Game.getCount()%15 == 0){
            addObject(new UniversalObject(-50, 250 + (int)(Math.random()*200),6, Math.random()/2,
                    30, 15, attackImage));
        }

        super.tick(box);
    }
}
