package bin.Prompts;

import bin.Game;
import bin.GameState;
import bin.Player;
import bin.StateType;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sajid on 2016-06-07.
 */
//Dead End Screen showing player getting corruped when their sol count reaches below 0
public class DeadEndScreen extends GameState {

    //properties
    private float alpha = 0.00f;
    private boolean deadEndText;
    private int flashDelay;
    private final int MAX_FLASH_DELAY_TIME = 30;

    private enum State{
        Agree(),
        Flash(),
        End(),
    }

    private State state;
    private Image deathSprite = new ImageIcon("src\\resources\\Game ENd\\death.png").getImage();

    public DeadEndScreen(Player player, StateType stateType){
        super(player,stateType);
        deadEndText = false;
        state = State.Agree;
        flashDelay = 0;
    }

    public void render(Graphics g){
        Graphics2D g2 =  (Graphics2D) g;
        //fades the screen red
        if(state == State.Agree){
            g2.setComposite(makeTransparent(alpha));

            g.setColor(Color.red);
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            //increase the transparency and repaint
            alpha += 0.01f;
            if(alpha >= 1.0f){
                g2.setComposite(makeTransparent(1));
                state = State.Flash;
                alpha = 0.0f;
            }
        }
        //flashes the screen with picture of corrupted player
        else if(state == State.Flash){
            g.setColor(Color.white);
            g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
            //delay for the flash
            if(flashDelay <= MAX_FLASH_DELAY_TIME){
                flashDelay++;
            }
            else{
                //before dead end texts displayed, the screen slowly fades black
                if(!deadEndText){
                    g2.setComposite(makeTransparent(alpha));
                    g.setColor(Color.black);
                    g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
                    alpha += 0.05f;
                    if(alpha >= 1.0f){
                        alpha = 1.0f;//when invisible, its done
                        g2.setComposite(makeTransparent(1));
                        deadEndText = true;
                        alpha = 0.0f;
                    }
                }
                //when its time to display the text, screen is all black underneath
                else{
                    g.setColor(Color.black);
                    g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
                }
            }
            //character of corrupted player
            g2.drawImage(deathSprite,0,80,deathSprite.getWidth(null),deathSprite.getHeight(null),null);
            //dead end text is slowly faded
            if(deadEndText){
                g2.setComposite(makeTransparent(alpha));
                drawText(g2, "Goudy Stout", "DEAD",330,100,50, Color.red);
                alpha += 0.01f;
                if(alpha >= 1.0f){
                    alpha = 1.0f;
                    g2.setComposite(makeTransparent(1));
                    state = State.End;  //The displaying is over
                }
            }
        }
    }

    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }

    public void drawText(Graphics2D g,String font, String text, int x ,int y, int size, Color color ){
        g.setColor(color);
        g.setFont(new Font(font, Font.PLAIN,size));
        g.drawString(text,x,y);
    }

    @Override
    public void tick(){
    }

    //when enter or z is pressed after the dead end screen goes through its animation, game is closed
    public void keyZPressed(){
        if(state == State.End){
            System.exit(0);
        }
    }

    public void keyEnterPressed(){
        if(state == State.End){
            System.exit(0);
        }
    }


}
