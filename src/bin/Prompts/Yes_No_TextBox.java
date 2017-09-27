package bin.Prompts;

import bin.*;

import java.awt.*;

/**
 * Created by Sajid on 2016-06-08.
 */
//abstract class which displays Textbox and then gives user a choice of yes or no which will bring about an action
public abstract class Yes_No_TextBox extends GameState {

    //indicates whether choice is on yes or no
    private final int YES = 0, NO = 1;
    private int choiceIndex;
    protected TextBoxState text;    //textbox which is displayed
    private enum State {
        Text(), //when text is being displayed
        Choice(),   //when player can choose yes or no
    }

    State state;
    //different colors for whether its created while in battle or in the overworld
    private Color textColor;
    private Color boxColor;

    //initializes properties
    public Yes_No_TextBox(Player player, StateType stateType, TextBoxState textBoxState, boolean inBattle){
        super(player, stateType);
        text = textBoxState;
        state = State.Text;
        choiceIndex = 1;
        //different color combinations for battle display and overworld display
        if(inBattle){
            textColor = Color.white;
            boxColor = Color.black;
        }
        else{
            textColor = Color.black;
            boxColor = Color.white;
        }
    }

    @Override
    //when choice is given, a box is displayed at top left of screen with "yes" and "no" with arrow indicating which choice
    //is currently being selected
    public void render(Graphics g){
        Graphics2D g2 =  (Graphics2D) g;
        if(state == State.Choice){
            text.render(g2);
            drawBox(g2,textColor,boxColor);
            g.setColor(textColor);
            drawOptionArrow(g2, 10, 22 + 30*choiceIndex );
        }
    }

    @Override
    //textbox is added followed by limiting the choice index (can only move up and down once - only 2 choices)
    public void tick(){
        if(state == State.Text){
            text.reset();
            GameStateManager.addState(text);
            state = State.Choice;
        }
        else if(state == State.Choice){
            choiceIndex = Game.clamp(choiceIndex,1,0);
        }
    }


    public void drawOptionArrow(Graphics2D g, int x, int y){
        Polygon arrow = new Polygon(new int[]{x, x + 15, x,}, new int[]{y, y + 5, y+ 10},3);
        g.fillPolygon(arrow);
    }

    public void drawBox(Graphics2D g, Color border,Color box){
        g.setColor(border);
        g.fillRect(0, 0, 100, 80);
        g.setColor(box);
        g.fillRect(5, 5, 90 , 70);

        drawText(g, "Berlin Sans FB Demi", "YES", 30, 35, 20, border);
        drawText(g, "Berlin Sans FB Demi", "NO", 30, 65, 20, border);
    }

    public void drawText(Graphics2D g,String font, String text, int x ,int y, int size, Color color ){
        g.setColor(color);
        g.setFont(new Font(font, Font.PLAIN,size));
        g.drawString(text,x,y);
    }

    //Yes becomes current choice
    public void keyUpPressed(){
        if(state ==  state.Choice){
            choiceIndex--;
        }
    }
    //No becomes current choice
    public void keyDownPressed(){
        if(state == state.Choice){
            choiceIndex++;
        }
    }
    //completes specific action depending on which choice player has selected
    public void keyZPressed(){
        if(state ==  state.Choice){
            if(choiceIndex == YES){
                agreeAction();
            }
            else if(choiceIndex == NO){
                disagreeAction();
            }
        }
    }

    //must be overrided as that is the whole point of this abstract class, to have different types of Yes/No textboxes
    //which would induce different actions
    public abstract void agreeAction();

    public abstract void disagreeAction();

    //resets text
    public void resetPrompt(){
        text.reset();
        state = State.Text;
        choiceIndex = 1;

    }




}
