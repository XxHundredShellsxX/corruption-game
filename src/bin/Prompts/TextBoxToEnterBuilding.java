package bin.Prompts;

import bin.*;
import bin.OverWorldElements.*;
import bin.OverWorldElements.DIRECTION;

/**
 * Created by Sajid on 2016-06-16.
 */
//yes/no textbox which moves player up if they choose yet or moves player down if they choose no
//used to enter buildings or move away from building
public class TextBoxToEnterBuilding extends Yes_No_TextBox{

    private boolean finished;
    private boolean acceptAction;   //used to determine whether player accepted action to determine if this warning prompt is to be deleted or not

    public TextBoxToEnterBuilding(Player player, StateType stateType, TextBoxState textBoxState, boolean inBattle){
        super(player, stateType, textBoxState, inBattle);
        finished = false;
        acceptAction = false;
    }

    @Override
    public void agreeAction(){
        GameStateManager.removeState(this);
        player.setY(player.getY() - 25);
        player.setDirection(DIRECTION.Up);
        finished = true;
        acceptAction = true;
    }

    @Override
    public void disagreeAction() {
        GameStateManager.removeState(this);
        player.setY(player.getY() + 25);
        player.setDirection(DIRECTION.Down);
        finished = true;
        acceptAction = false;
    }

    public boolean isFinished(){
        return finished;
    }
    public boolean isActionAccepted(){
        return acceptAction;
    }

    public void reset(){
        resetPrompt();
        finished = false;
        acceptAction = false;
    }
}

