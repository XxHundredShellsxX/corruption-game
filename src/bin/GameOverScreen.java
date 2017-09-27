package bin;

import bin.Prompts.DeadEndScreen;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sajid on 2016-06-17.
 */
public class GameOverScreen extends GameState{

    //properties
    private float alpha = 0.00f;
    private float increment = 0.02f;

    private enum State{
        FadeOut(),
        FadeIn(),
        DontGiveUp(),
        End()
    }
    private Image background = new ImageIcon("src\\resources\\Game Over\\GameOverScreen.png").getImage();
    private State state;
    private TextBoxState DontGiveUpText;

    public GameOverScreen(Player player, StateType stateType){
        super(player,stateType);
        state = State.FadeOut;
        DontGiveUpText = new TextBoxState(player,StateType.TextBox,new String[]{"It seems your journey has been cut short",
            "Worry not as this is not the end",
            "You can try again! Your efforts were not in vain"},Color.ORANGE,Color.black,Color.WHITE,20, 550, 850, 100,30);
    }

    public void render(Graphics g){
        Graphics2D g2 =  (Graphics2D) g;
        //fades the screen white
        if(state == State.FadeIn || state == State.FadeOut){
            if(state == State.FadeIn){
                drawImage(g2,background,0,0);
            }
            fade(g2);
        }
    }

    public void drawImage(Graphics2D g, Image image,int x, int y){
        g.drawImage(image,x,y,image.getWidth(null),image.getHeight(null),null);
    }

    public void fade(Graphics2D g){

        g.setComposite(makeTransparent(alpha));

        g.setColor(Color.white);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

        alpha += increment;
        if(alpha >= 0.5f){
            state = State.FadeIn;
            increment*= -1;
        }
        else if(alpha < 0){
            state = State.DontGiveUp;
            alpha = 0.0f;
        }
        g.setComposite(makeTransparent(1));
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
        if(state == State.DontGiveUp){
            GameStateManager.addState(DontGiveUpText);
            state = State.End;
        }
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
