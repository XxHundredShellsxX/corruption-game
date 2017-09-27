package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BouncyBall;

import javax.swing.*;

/**
 * Created by Fahim on 2016-04-28.
 */
public class Bounce extends Attack {

    public Bounce(){
        super(700, 2);
        attackImage =  new ImageIcon("src\\resources\\AttackSprites\\SmallBall.png").getImage();
    }

    @Override
    public void start() {
        super.start();
        addObject(new BouncyBall(100, 350, 20, 20, 3, 3, attackImage));
        addObject(new BouncyBall(500, 400, 20, 20, 3, 3, attackImage));
        addObject(new BouncyBall(300, 350, 20, 20, 3, 3, attackImage));
    }
}
