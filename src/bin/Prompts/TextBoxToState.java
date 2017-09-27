package bin.Prompts;

import bin.*;

import java.awt.*;

/**
 * Created by Sajid on 2016-06-16.
 */
//yes/no textbox which leads to a different state when player chooses yes (ie. battle, event), (nothing happens when chosen no)
public class TextBoxToState extends Yes_No_TextBox {

    private GameState nextState;
    private boolean isDone;

    public TextBoxToState(Player player, StateType stateType, TextBoxState textBoxState, boolean inBattle, GameState nextState){
        super(player, stateType, textBoxState, inBattle);
        this.nextState = nextState;
        isDone = false;
    }

    @Override
    public void agreeAction() {
        isDone = true;
        GameStateManager.removeState(this);
        if (nextState.getState() == StateType.Battle) {     //if its battle, then the state is transitioned to, not just added
            GameStateManager.addStateTransition(nextState, Color.blue);
            Load.wizardsKilled++;
        }
        else {
            GameStateManager.addState(nextState);
        }
    }

    @Override
    public void disagreeAction() {
        GameStateManager.removeState(this);
    }

    public boolean getIsDone(){
        return isDone;
    }
}
