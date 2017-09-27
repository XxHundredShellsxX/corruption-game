package bin.Events;

import bin.*;
import bin.BattleElements.Battle;

import java.awt.*;

/**
 * Created by 31800918 on 6/7/2016.
 */
//This event has the player waking up, versing the knight that has been attacking everyone and then having the fairy joining him
public class E1_Knight_Attack extends EventState {

    private TextBoxState wakeUpCall;
    private TextBoxState knightTalk;
    private TextBoxState fairyTalk;
    private float alpha = 1.00f;
    private Image knightStanding;
    private Image knightKneeling;
    private Image fairy;

    enum State{
        beforeWake(),
        wakingUp(),
        knightTalk(),
        battleStart(),
        repositionSprites(),
        fairyTalk(),
    }

    private State state;

    public E1_Knight_Attack(Player player){
        super(player, "Event1");
        state = State.beforeWake;
        wakeUpCall = textBoxes.get(0);
        knightTalk = textBoxes.get(1);
        fairyTalk = textBoxes.get(2);
        knightStanding = images.get(0);
        knightKneeling = images.get(1);
        fairy = images.get(2);
        isDone = false;
    }

    @Override
    public void init() {
        super.init();
        isDone = true;
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        renderBackGround(g2);
//        renderPlayer(g2);
        int playerX = 450;
        int playerY = 350;
//        drawImage(g2, player.ge);
        player.render(g2, playerX, playerY);
        //players eyes closed so screen black
        if(state == State.beforeWake){
            g.setColor(Color.black);
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        }
        //knight threatening to attack player while screen fading
        else if(state == State.wakingUp){
            drawImage(g2,knightStanding,playerX,playerY+76);
            fadeIn(g2);
        }
        //knight talking to player
        else if(state == State.knightTalk){
            drawImage(g2,knightStanding,playerX,playerY+76);
        }
        //knight fallen and fairy talking to player
        else if(state == State.fairyTalk){
            drawImage(g2,fairy,playerX,playerY - 40);
            drawImage(g2,knightKneeling,playerX,playerY+76);
        }

    }

    public void drawImage(Graphics2D g, Image image,int x, int y){
        g.drawImage(image,x,y,image.getWidth(null),image.getHeight(null),null);
    }
    //transitions between textboxes and battles
    @Override
    public void tick(){
        if(state == State.beforeWake){
            GameStateManager.addState(wakeUpCall);
            state = State.wakingUp;
        }
        else if(state == State.knightTalk){
            GameStateManager.addState(knightTalk);
            state = State.battleStart;
        }
        else if(state == State.battleStart){
            GameStateManager.addStateTransition(new Battle(player,
                    Load.getEnemy("Random Agressive Knight")),Color.green);
            state = State.repositionSprites;
        }
        //this state repositions the sprite to appropriate position and has to be done here or else it'd be rendered before battle starts
        else if(state == State.repositionSprites){
            player.faceUp();
            state = State.fairyTalk;
        }
        else if(state == State.fairyTalk){
            GameStateManager.addState(fairyTalk);
            GameStateManager.removeState(this);
        }
    }

    public void fadeIn(Graphics2D g){

        g.setComposite(makeTransparent(alpha));

        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        //do the drawing here
        //increase the transparency and repaint
        alpha -= 0.01f;
        if(alpha <= 0.0f){
            alpha = 0.0f;//when invisible, its done
            state = State.knightTalk;
        }
        g.setComposite(makeTransparent(1));
    }

    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }
}
