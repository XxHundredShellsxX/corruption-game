package bin.BattleElements;

import bin.*;

/**
 * Created by Fahim on 2016-04-28.
 */
public abstract class BattleState extends GameState {

    protected BattleState nextState;
    protected BattleChoice nextChoice;
    protected Enemy enemy;

    public BattleState(Player player, Enemy enemy, StateType t) {
        super(player, t);
        this.enemy = enemy;
        this.nextChoice = nextChoice;
        this.nextState = nextState;
    }


//    public abstract void isFinished();
//
//    public void receiveInput(int key) {
//    }
//
//
//
//    public void addAttack(Attack a) {
//    }
//
//    public void start() {
//    }
//
//    public boolean isAnimating(){return false;}
//
//    public void finish(){}

    public BattleState getNextState() {
        return nextState;
    }

    public BattleChoice getNextChoice() {
        return nextChoice;
    }

//    public void keyUpPressed(){}
//    public void keyDownPressed(){}
//    public void keyLeftPressed(){}
//    public void keyRightPressed(){}
//    public void noKeyUpPressed(){}
//    public void noKeyDownPressed(){}
//    public void noKeyLeftPressed(){}
//    public void noKeyRightPressed(){}
//    public void keyZPressed(){}
//    public void keyXPressed(){}
//    public void noKeyZPressed(){}
//    public void noKeysPressed(){}
//    public void keyEnterPressed(){}

}
