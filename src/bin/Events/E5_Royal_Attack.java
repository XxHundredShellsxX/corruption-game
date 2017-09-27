package bin.Events;

import bin.*;
import bin.BattleElements.Battle;
import bin.Prompts.TextBoxtoUltimateWeapon;

import java.awt.*;

/**
 * Created by Sajid on 2016-06-16.
 */
//Royal guards attack player when he enters castle, player beats them both and then gets explanation and offer from prophet
public class E5_Royal_Attack extends EventState{

    private TextBoxState Halt;
    private TextBoxState Halt2;
    private TextBoxtoUltimateWeapon ultimateWeapon;
    private Image prophet;
    private Image guardFaceRight;
    private Image guardFaceLeft;
    private float alpha = 0.0f;
    private boolean rendered; //used to ensure graphics was being rendered before changing specific state

    enum State{
        royalGuardTalk(),
        battle1(),
        talk2(),
        battle2(),
        prophetTalk(),
        fadeOut()
    }

    private State state;

    public E5_Royal_Attack(Player player){
        super(player,"Event5");
        state = State.royalGuardTalk;
        Halt = textBoxes.get(0);
        Halt2 = textBoxes.get(1);
        TextBoxState prophetTalk = textBoxes.get(2);
        ultimateWeapon = new TextBoxtoUltimateWeapon(player,StateType.TextBox,prophetTalk,false);
        prophet = images.get(0);
        guardFaceLeft = images.get(1);
        guardFaceRight = images.get(2);
        rendered = false;
    }
    //is done to record that event is done playing (as being initialized means its being playeed and it only plays once)
    @Override
    public void init() {
        super.init();
        isDone = true;
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        renderBackGround(g2);
        renderPlayer(g2);
        player.faceUp();
        //both royal guards look at player
        if(state == State.royalGuardTalk){
            drawImage(g2,guardFaceLeft,playerX + 100,playerY - 80);
            drawImage(g2,guardFaceRight,playerX - 100,playerY - 80);
        }
        //one of them runs, leaving other royal guard looking at player
        else if(state == State.talk2){
            drawImage(g2,guardFaceLeft,playerX + 100,playerY - 80);
        }
        //after they both run, prophet talks to player
        else if(state == State.prophetTalk){
            drawImage(g2,prophet,playerX,playerY - 150);
        }
        else if(state == State.fadeOut){
            fadeOut(g2);
        }

    }

    public void drawImage(Graphics2D g, Image image,int x, int y){
        g.drawImage(image,x,y,image.getWidth(null),image.getHeight(null),null);
    }
    //handles between textboxes and battles
    public void tick(){
        if(state == State.royalGuardTalk){
            GameStateManager.addState(Halt);
            state = State.battle1;
        }
        else if(state == State.battle1){
            GameStateManager.addStateTransition(new Battle(player,
                Load.getEnemy("Royal Guard 1")),Color.green);
            state = State.talk2;
        }
        else if(state == State.talk2){
            GameStateManager.addState(Halt2);
            state = State.battle2;
        }
        else if(state == State.battle2){
            GameStateManager.addStateTransition(new Battle(player,
                    Load.getEnemy("Royal Guard 2")),Color.green);
            state = State.prophetTalk;
        }
        else if(state == State.prophetTalk){
            GameStateManager.addState(ultimateWeapon);
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
