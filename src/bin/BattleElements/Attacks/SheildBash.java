package bin.BattleElements.Attacks;

import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.Sheild;
import bin.DIRECTION;
import bin.Game;

import javax.swing.*;
import java.awt.*;

/**
 * Shields come from Either sides, in order to corner the player to one side
 */
public class SheildBash extends Attack {
    private DIRECTION direction;

    public SheildBash(){
        super(500, 4);
        direction = DIRECTION.Right;
        this.attackImage =  new ImageIcon("src\\resources\\AttackSprites\\HorizontalSheild.png").getImage();
    }

    @Override
    public void tick(Rectangle box) {
        if (Game.getCount() % 110 == 0) {
            if (direction == DIRECTION.Right) {
                addObject(new Sheild(-100, 250, direction, 500, attackImage));
                direction = DIRECTION.Left;
            } else {
                addObject(new Sheild(600, 250, direction, 500, attackImage));
                direction = DIRECTION.Right;
            }
        }
        for(BattleObject b: objects){
            b.tick(box);
            if(b.isFinished()){
                removeObject(b);
                break;
            }
        }
    }
}
