package bin.Events;

import bin.*;
import bin.BattleElements.Battle;
import bin.Prompts.TextBoxtoUltimateWeapon;

import java.awt.*;

/**
 * Created by Sajid on 2016-06-16.
 */
public class E6_Final_Boss extends EventState {
    private TextBoxState KingTalk;
    private TextBoxState FairyPressure;
    private TextBoxState TrueColorsRevealed;
    private TextBoxState Defeat;
    private Image King;
    private Image kingInjured;
    private Image evilChar;
    private Image fairy;
    private Image[] bossDisappear;
    private float alpha = 0.0f;
    private float increment = 0.05f;
    private int index;
    private int counter;

    enum State{
        kingTalk(),
        kingBattle(),
        fairyTalk(),
        transformFadeOut(),
        transformFadeIn(),
        finalBossTalk(),
        finalBattle(),
        defeatedTalk(),
        disappear(),
        fadeOut()
    }

    private State state;

    public E6_Final_Boss(Player player){
        super(player,"Event6");
        state = State.kingTalk;
        KingTalk = textBoxes.get(0);
        FairyPressure = textBoxes.get(1);
        TrueColorsRevealed = textBoxes.get(2);
        Defeat = textBoxes.get(3);
        King = images.get(0);
        kingInjured = images.get(1);
        evilChar = images.get(2);
        fairy = images.get(3);
        bossDisappear = new Image[3];
        bossDisappear[0] = images.get(4);
        bossDisappear[1] = images.get(5);
        bossDisappear[2] = images.get(6);
        index = 0;
        counter = 0;
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
        renderPlayer(g2);
        player.faceUp();
        if(state == State.kingTalk){
            drawImage(g2,King,playerX,playerY - 200);
        }
        else if(state == State.fairyTalk || state == State.transformFadeOut){
            drawImage(g2,kingInjured,playerX,playerY - 200);
            drawImage(g2,fairy,playerX,playerY - 100);
        }
        else if(state == State.transformFadeIn || state == State.finalBossTalk || state == State.defeatedTalk){
            drawImage(g2,kingInjured,playerX,playerY - 200);
            drawImage(g2,evilChar,playerX,playerY - 100);
        }
        else if(state == State.disappear){
            counter++;
            drawImage(g2,kingInjured,playerX,playerY - 200);
            drawImage(g2,bossDisappear[index],playerX - 20 ,playerY - 100);
            if(counter%20 == 0){
                index++;
            }
            if(index == 3){
                state = State.fadeOut;
            }
        }
        else if(state == State.fadeOut){
            fadeOut(g2);
        }

        if(state == State.transformFadeOut || state == State.transformFadeIn){
            transform(g2);
        }


    }

    public void drawImage(Graphics2D g, Image image,int x, int y){
        g.drawImage(image,x,y,image.getWidth(null),image.getHeight(null),null);
    }

    public void tick(){
        if(state == State.kingTalk){
            GameStateManager.addState(KingTalk);
            state = State.kingBattle;
        }
        else if(state == State.kingBattle){
            GameStateManager.addStateTransition(new Battle(player,
                    Load.getEnemy("King")),Color.green);
            state = State.fairyTalk;
        }
        else if(state == State.fairyTalk){
            GameStateManager.addState(FairyPressure);
            state = State.transformFadeOut;
        }
        else if(state == State.finalBossTalk){
            GameStateManager.addState(TrueColorsRevealed);
            state = State.finalBattle;
        }
        else if(state == State.finalBattle){
            GameStateManager.addStateTransition(new Battle(player,
                    Load.getEnemy("???")),Color.green);
            state = State.defeatedTalk;
        }
        else if(state == State.defeatedTalk){
            GameStateManager.addState(Defeat);
            state = State.disappear;
        }

    }

    public void transform(Graphics2D g){

        g.setComposite(makeTransparent(alpha));

        g.setColor(Color.RED);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        alpha += increment;
        if(alpha >= 1.0f){
            alpha = 1.0f;
            state = State.transformFadeIn;
            increment*= -1;
        }
        else if(alpha < 0){
            state = State.finalBossTalk;
            alpha = 0.0f;
        }
        g.setComposite(makeTransparent(1));
    }

    public void fadeOut(Graphics2D g){

        g.setComposite(makeTransparent(alpha));

        g.setColor(Color.white);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        //do the drawing here
        //increase the transparency and repaint
        alpha += 0.02f;
        if(alpha >= 1.0f){
            alpha = 1.0f;
        }
        g.setComposite(makeTransparent(1));
    }

    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }
}