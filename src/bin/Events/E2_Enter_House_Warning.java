package bin.Events;

import bin.*;
import bin.Prompts.TextBoxToEnterBuilding;

import java.awt.*;

/**
 * Created by Sajid on 2016-06-16.
 */
//Event that sees prompts whether character should enter house or not
public class E2_Enter_House_Warning extends EventState {

    //textbox to warn the player about entering
    private TextBoxToEnterBuilding fairyWarning;
    private boolean actionAccepted;
    private boolean promptFinished;

    public E2_Enter_House_Warning(Player player){
        super(player,"Event2");
        fairyWarning = new TextBoxToEnterBuilding(player,StateType.TextBox,textBoxes.get(0),false);
    }
    //resets booleans for prompt, so prompt can be called again
    @Override
    public void init() {
        super.init();
        fairyWarning.reset();
        actionAccepted = false;
        promptFinished = false;
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        renderBackGround(g2);
        renderPlayer(g2);
    }
    //handles ordering of warning to checking whether going into house was accepted (to see if even was triggered)
    @Override
    public void tick(){
        if(fairyWarning.isFinished()){
            promptFinished = true;
        }
        if(!promptFinished){
            GameStateManager.addState(fairyWarning);

        }
        else{
            isDone = fairyWarning.isActionAccepted();
            GameStateManager.removeState(this);
            player.standStill();
        }

    }

    //resets textbox and event booleans
    public void reset(){
        fairyWarning.reset();
        actionAccepted = false;
        promptFinished = false;
    }
}
