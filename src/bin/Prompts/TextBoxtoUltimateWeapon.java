package bin.Prompts;

import bin.*;

/**
 * Created by Sajid on 2016-06-16.
 */
//a yes/no textbox which gives weapon to player if they choose yes and they have enough sol
//if they choose yes and not enough sol, leads to dead end screen and if they choose no, nothing happens
public class TextBoxtoUltimateWeapon extends Yes_No_TextBox{
    private DeadEndScreen deadEndScreen;

    public TextBoxtoUltimateWeapon(Player player, StateType stateType, TextBoxState textBoxState, boolean inBattle){
        super(player, stateType, textBoxState, inBattle);
        deadEndScreen = new DeadEndScreen(player,StateType.DeadEndScreen);

    }

    @Override
    public void agreeAction(){
        GameStateManager.removeState(this);
        if(player.getSol() >= 9000){
            player.buyWeapon(Load.getWeapon("???"));
        }
        else{
            GameStateManager.addState(deadEndScreen);
        }
    }

    @Override
    public void disagreeAction() {
        GameStateManager.removeState(this);
    }
}
