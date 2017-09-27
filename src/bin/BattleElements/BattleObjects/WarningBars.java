package bin.BattleElements.BattleObjects;

import bin.BattleElements.Battle;
import bin.Game;

import java.awt.*;

/**
 * Created by Gateway on 2016-06-17.
 */
public class WarningBars extends BattleObject{
    int startTime;
    public WarningBars(int x, int y, int width, int height, int velX, int velY, Image sprite){
        super(x, y, velX, velY);
        isDead = true;
        startTime = Game.getCount() + 100;
        collisionRect = new Rectangle(x, y, width, height);
        this.sprite = sprite;
    }



    @Override
    public void tick(Rectangle box) {
        if(!isDead){
            x += velX;
            y += velY;
            collisionRect.setLocation((int)x, (int)y);
            if(Game.getCount()%75 == 0){
                velY = -velY;
            }
        }
        else if(startTime == Game.getCount()){
            isDead = false;
        }

    }

    @Override
    public void render(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;    //uses graphics 2D
        //renders text
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);
        g.setColor(Color.YELLOW);
        int imageX = (int) (x + collisionRect.getWidth() / 2 - sprite.getWidth(null) / 2);
        int imageY = (int) (y + collisionRect.getHeight() / 2 - sprite.getHeight(null) / 2);
        if(!isDead) {
            g.drawImage(sprite, imageX, imageY, null);
//            g2.draw(collisionRect);
        }
        else{
            g2.setComposite(Battle.makeTransparent(0.5f));
            g.drawImage(sprite, imageX, imageY, null);
            g2.setComposite(Battle.makeTransparent(1));
        }
    }
}
