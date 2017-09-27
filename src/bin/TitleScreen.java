package bin;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Sajid on 2016-06-17.
 */
//Title Screen which fades in and then allows player to choose to start new game, see controls or quit
public class TitleScreen extends GameState{

    //properties
    private float alpha = 1.00f;
    private float increment = 0.02f;
    private int index;
    private boolean pressedDown;
    private final int NEW_GAME = 0, CONTROLS = 1, QUIT = 2;

    private enum State{
        FadeIn(),
        PressZtoStart(),
        ChooseOption(),
        ControlsPage1(),
        ControlsPage2()

    }
    private Image background = new ImageIcon("src\\resources\\Title Screen\\TitleScreen.png").getImage();

    private Image controlspage1 = new ImageIcon("src\\resources\\Controls\\Page1.png").getImage();

    private Image controlspage2 = new ImageIcon("src\\resources\\Controls\\Page2.png").getImage();

    private State state;

    public TitleScreen(Player player, StateType stateType){
        super(player,stateType);
        state = State.FadeIn;
        index = 0;
        pressedDown = false;
    }

    public void render(Graphics g) {
        Graphics2D g2 =  (Graphics2D) g;
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);
        //control page 1
        if(state == State.ControlsPage1){
            drawImage(g2,controlspage1,0,0);
        }
        //control page 2
        else if(state == State.ControlsPage2){
            drawImage(g2,controlspage2,0,0);
        }
        //title screen background
        else {
            drawImage(g2, background, 0, 0);
        }
        //fades in
        if(state == State.FadeIn){
            fadeIn(g2);
        }
        //text blinks to start
        else if(state == State.PressZtoStart){
            g2.setComposite(makeTransparent(alpha));
            alpha += increment;
            if(alpha >= 1){
                alpha = 1.0f;
                increment*=-1;
            }
            if(alpha <= 0){
                alpha = 0.0f;
                increment*=-1;
            }
            drawText(g2,"Segoe UI Black","Press Z to Start",306,590,32,Color.black);
            g2.setComposite(makeTransparent(1));
        }
        //all options are displayed to choose
        else if(state == State.ChooseOption){
            drawText(g2,"Segoe UI Black","NEW GAME",330,280,40,Color.yellow);
            drawText(g2,"Segoe UI Black","CONTROLS",330,330,40,Color.yellow);
            drawText(g2,"Segoe UI Black","QUIT",330,380,40,Color.yellow);
            g.setColor(Color.white);
            drawOptionArrow(g2,290,250 + 50*index);
        }
    }

    public void drawOptionArrow(Graphics2D g, int x, int y){
        Polygon arrow = new Polygon(new int[]{x, x + 35, x,}, new int[]{y, y + 15, y+ 30},3);
        g.fillPolygon(arrow);
    }

    public void fadeIn(Graphics2D g){

        g.setComposite(makeTransparent(alpha));

        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        //do the drawing here
        //increase the transparency and repaint
        alpha -= 0.005f;
        if(alpha <= 0.0f){
            alpha = 0;
            state = State.PressZtoStart;
        }
        g.setComposite(makeTransparent(1));
    }

    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }

    public void drawImage(Graphics2D g, Image image,int x, int y){
        g.drawImage(image,x,y,image.getWidth(null),image.getHeight(null),null);
    }

    public void drawText(Graphics2D g,String font, String text, int x ,int y, int size, Color color ){
        g.setColor(color);
        g.setFont(new Font(font, Font.PLAIN,size));
        g.drawString(text,x,y);
    }

    @Override
    public void tick(){
        if(state == State.ChooseOption){
            index = Game.clamp(index,2,0);
        }
    }

    //when enter or z is pressed after the dead end screen goes through its animation, game is closed
    public void keyZPressed(){
        if(state == State.PressZtoStart){
            state = State.ChooseOption;
        }
        else if(state == State.ChooseOption && !pressedDown){
            if(index == NEW_GAME){//removes title screen and starts the overworld which is the last event
                GameStateManager.removeState(this);
            }
            else if(index == CONTROLS){
                state = State.ControlsPage1;
            }
            else if(index == QUIT){
                System.exit(0);
            }
        }
        else if(state == State.ControlsPage1 && !pressedDown){
            state = State.ControlsPage2;
        }
        else if(state == State.ControlsPage2 && !pressedDown){
            state = State.ChooseOption;
        }
        pressedDown = true;
    }

    @Override
    public void keyDownPressed() {
        if(state == State.ChooseOption && !pressedDown){
            index++;
            pressedDown = true;
        }
    }

    @Override
    public void keyUpPressed() {
        if(state == State.ChooseOption && !pressedDown){
            index--;
            pressedDown = true;
        }
    }

    @Override
    public void noKeyDownPressed() {
        pressedDown = false;
    }
}
