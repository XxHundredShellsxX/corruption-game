package bin.Events;

import bin.*;
import bin.BattleElements.Battle;

import javax.swing.*;
import java.awt.*;

/**
 * Created by 31800918 on 6/14/2016.
 */
//Event where one knight is getting bullied by two others and the player battles the two knights
public class E3_Knight_Bullied extends EventState {

    private TextBoxState bullyingKnight;
    private TextBoxState intimidate;
    private TextBoxState revenge;
    private TextBoxState runAway;
    private Image knightFaceRight;
    private Image knightFaceLeft;
    private Image knightDefeated;
    private float alpha = 0.0f;
    private boolean rendered; //used to ensure graphics was being rendered before changing specific state

    enum State{
        bullyingKnight(),
        beforeFight(),
        firstBattleStart(),
        beforeSecondFight(),
        secondBattleStart(),
        runsAway(),
        fadeOut()
    }

    private State state;

    public E3_Knight_Bullied(Player player){
        super(player, "Event3");
        state = State.bullyingKnight;
        bullyingKnight = textBoxes.get(0);
        intimidate = textBoxes.get(1);
        revenge = textBoxes.get(2);
        runAway = textBoxes.get(3);
        knightFaceRight = images.get(0);
        knightFaceLeft = images.get(1);
        knightDefeated = images.get(2);
        rendered = false;
    }

    @Override
    public void init() {
        super.init();
        isDone = true;
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        renderBackGround(g2);
        player.faceRight();
        renderPlayer(g2);
        //knights looking at bullied knight
        if(state == State.bullyingKnight){
            drawImage(g2,knightFaceRight,playerX + 100,playerY - 50);
            drawImage(g2,knightDefeated,playerX + 140,playerY - 50);
            drawImage(g2,knightFaceLeft,playerX + 180,playerY - 50);
        }
        //knights looking at player
        else if(state == State.beforeFight){
            rendered = true;
            drawImage(g2,knightFaceLeft,playerX + 100,playerY - 50);
            drawImage(g2,knightDefeated,playerX + 140,playerY - 50);
            drawImage(g2,knightFaceLeft,playerX + 180,playerY - 50);
        }
        //one knight is defeated, other knight still looking at you
        else if(state == State.beforeSecondFight){
            drawImage(g2,knightDefeated,playerX + 100,playerY - 50);
            drawImage(g2,knightDefeated,playerX + 140,playerY - 50);
            drawImage(g2,knightFaceLeft,playerX + 180,playerY - 50);
        }
        //both knights defeated
        else if(state == State.runsAway){
            drawImage(g2,knightDefeated,playerX + 100,playerY - 50);
            drawImage(g2,knightDefeated,playerX + 140,playerY - 50);
            drawImage(g2,knightDefeated,playerX + 180,playerY - 50);
        }
        //fade where they ran away
        else if(state == State.fadeOut){
            fadeOut(g2);
        }

    }

    public void drawImage(Graphics2D g, Image image,int x, int y){
        g.drawImage(image,x,y,image.getWidth(null),image.getHeight(null),null);
    }
    //handles states between dialgoues and battles
    public void tick(){
        if(state == State.bullyingKnight){
            GameStateManager.addState(bullyingKnight);
            state = State.beforeFight;
        }
        else if(state == State.beforeFight && rendered){
            GameStateManager.addState(intimidate);
            state = State.firstBattleStart;
            rendered = false;
        }
        else if(state == State.firstBattleStart){
            GameStateManager.addStateTransition(new Battle(player,
                    Load.getEnemy("Bullying Knight 1")),Color.green);
            state = State.beforeSecondFight;
        }
        else if(state == State.beforeSecondFight){
            GameStateManager.addState(revenge);
            state = State.secondBattleStart;
        }
        else if(state == State.secondBattleStart){
            GameStateManager.addStateTransition(new Battle(player,
                   Load.getEnemy("Bullying Knight 2")),Color.green);
            state = State.runsAway;
        }
        else if(state == State.runsAway){
            GameStateManager.addState(runAway);
            state = State.fadeOut;
        }
    }

    public void fadeOut(Graphics2D g){

        g.setComposite(makeTransparent(alpha));

        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        //do the drawing here
        //increase the transparency and repaint
        alpha += 0.05f;
        if(alpha >= 1.0f){
            alpha = 1.0f;
            GameStateManager.removeState(this);
        }
        g.setComposite(makeTransparent(1));
    }

    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }
}
