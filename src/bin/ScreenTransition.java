package bin;
import java.awt.*;

/**
 * Created by Sajid on 2016-04-30.
 */
//Handles transitions between battles and other states by fading and resizing screen
public class ScreenTransition extends GameState{

    //properties for fading
    private float alpha = 0.0f;
    private float increment = 0.05f;
    private Color fadeColor;
    //to keep track of when to switch states
    private boolean switchStates;
    //states switching between
    private GameState state1;
    private GameState state2;
    private Game main;
    private StateType stateType;
    //enum type number to indicate whether states being added or removed
    private final int ADD = 1;
    private final int REMOVE = 2;
    //removed or added
    private int action;

    //all properties are initialized
    public ScreenTransition(Player player, StateType type,Color c,GameState s1, GameState s2, Game m, int action){
        super(player,type);
        fadeColor = c;
        switchStates = false;
        state1 = s1;
        state2 = s2;
        main = m;
        stateType = type;
        this.action = action;
    }

    //renders the first state while screen is fading to opacity, and as soon as its opaque, the next state starts rendering
    public void render(Graphics g){
        if(switchStates){
            state2.render(g);
        }
        else{
            state1.render(g);
        }
        Graphics2D g2 = (Graphics2D) g;
        //set the opacity
        g2.setComposite(makeTransparent(alpha >= 1.0f? 1.0f:alpha));
        //do the drawing here
        g2.setColor(fadeColor);
        g2.fillRect(0,0,main.getWidth(),main.getHeight());
    }

    //appropriate changes in alpha and resizing to small screen when switched to battle and then regular size to the other state
    //also adds or removes the appropriate state and then removes this screen transition state from displaying any further
    public void tick(){
        //increase the transparency and repaint
        alpha += increment;

        if(alpha >= 1.0f){
            if(type == StateType.Battle){
                main.setSize(Game.SMALL_WIDTH,Game.SMALL_HEIGHT);
            }
            if(type == StateType.InGame || type == StateType.Event){
                main.setSize(Game.WIDTH,Game.HEIGHT);
                player.standStill();
            }
            main.setLocationRelativeTo(null);
            switchStates = true;
            increment = increment*-1;
        }
        if(alpha <= 0 && switchStates){
            if(action == ADD){
                GameStateManager.addState(state2);
            }
            else if (action == REMOVE){
                GameStateManager.removeState(state1);
            }
            GameStateManager.removeState(this);
        }
    }

    //sets the transparency by specific alpha value
    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }
}
