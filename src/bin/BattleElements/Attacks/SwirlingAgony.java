package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.RotatingBall;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Objects sowrl around a central point, as it gets further and further away from said point
 */
public class SwirlingAgony extends Attack{
    private Image attackImage;
    public SwirlingAgony(){
        super(1000, 7);
        //TODO: CHANGE IMAGE
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\FireBall.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        super.tick(box);
        if(Game.getCount()% 50 == 0){
            addObject(new RotatingBall((int)(280 + Math.random()*40), (int)(340 + Math.random()*20),
                    40, 40, 0, 0, 1, 3, 0, attackImage));
        }
        for (BattleObject b : objects){
            b.addRadius(0.7);
        }
    }

    @Override
    public void start() {
        super.start();

    }
}
