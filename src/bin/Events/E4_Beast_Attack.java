package bin.Events;

import bin.*;
import bin.BattleElements.Battle;

import java.awt.*;

/**
 * Created by 31800918 on 6/15/2016.
 */
//Beast appears infront of castle, flaps its wings and battles you
public class E4_Beast_Attack extends EventState {

    private TextBoxState battleCry;
    private Image beastStill;
    private Image Flap1;
    private Image Flap2;
    private Image beastRoar;
    private float alpha = 0.0f;
    private boolean rendered; //used to ensure graphics was being rendered before changing specific state
    private int counter;
    private boolean flapUp;

    enum State{
        stayStill(),
        wingFlap(),
        battleCry(),
        battleStart(),
        disappear(),
        fadeOut()
    }

    private State state;

    public E4_Beast_Attack(Player player){
        super(player,"Event4");
        state = State.stayStill;
        battleCry = textBoxes.get(0);
        beastStill = images.get(0);
        Flap1 = images.get(1);
        Flap2 = images.get(2);
        beastRoar = images.get(3);
        counter = 0;
        rendered = false;
    }

    @Override
    public void init() {
        super.init();
        isDone = true;
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        renderBackGround(g2);
        player.faceUp();
        renderPlayer(g2);
        //standing still
        if(state == State.stayStill){
            drawImage(g2,beastStill,playerX - 20,playerY - 200);
        }
        //flaps its wings for a limited time
        else if(state == State.wingFlap){
            //switches between 2 images for wing flap animation
            if(counter%10 == 0){
                flapUp = ! flapUp;
            }
            drawImage(g2,flapUp? Flap1 : Flap2,playerX - 20,playerY - 200);
        }
        //screeches for battle
        else if(state == State.battleCry){
            drawImage(g2,beastRoar,playerX - 20,playerY - 200);
            rendered = true;
        }
        //stands will with wings open
        else if(state == State.disappear){
            drawImage(g2,Flap2,playerX - 20,playerY - 200);
        }
        //fades out
        else if(state == State.fadeOut){
            fadeOut(g2);
        }
    }

    public void drawImage(Graphics2D g, Image image,int x, int y){
        g.drawImage(image,x,y,image.getWidth(null),image.getHeight(null),null);
    }
    //handles between textboxes and battles as well as time periods where specific actions occur
    public void tick(){
        //beast stands still for first 40 ticks
        if(state == State.stayStill){
            counter++;
            if(counter == 40){
                state = State.wingFlap;
            }
        }
        //flaps wing for next 110 ticks
        else if(state == State.wingFlap){
            counter++;
            if(counter >= 150){
                state = State.battleCry;
            }
        }
        else if(state == State.battleCry && rendered){
            counter = 0;
            GameStateManager.addState(battleCry);
            state = State.battleStart;
        }
        else if(state == State.battleStart){
            GameStateManager.addStateTransition(new Battle(player,
                    Load.getEnemy("Flying Beast")),Color.green);
            state = State.disappear;
        }
        //disapears after 40 ticks
        else if(state == State.disappear){
            counter++;
            if(counter == 40){
                state = State.fadeOut;
            }
        }

    }

    public void fadeOut(Graphics2D g){

        g.setComposite(makeTransparent(alpha));

        g.setColor(Color.white);
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

