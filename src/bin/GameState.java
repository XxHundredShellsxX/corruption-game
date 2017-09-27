package bin;

import bin.BattleElements.BattleObjects.PlayerHeart;
import bin.OverWorldElements.WorldMap;

import java.awt.*;

public abstract class GameState {

    protected Player player;
    protected StateType type;

    public GameState(Player p,StateType type) {
        player = p;
        this.type = type;
    }

    public abstract void render(Graphics g);

    public abstract void tick();

    public StateType getState(){
        return type;
    }

    public Player getPlayer(){return player;}

    public PlayerHeart getHeart(){
        return player.getHeart();
    }

    public void setMap(WorldMap map) {}

    public void keyUpPressed(){}
    public void keyDownPressed(){}
    public void keyLeftPressed(){}
    public void keyRightPressed(){}
    public void noKeyUpPressed(){}
    public void noKeyDownPressed(){}
    public void noKeyLeftPressed(){}
    public void noKeyRightPressed(){}
    public void keyZPressed(){}
    public void keyXPressed(){}
    public void noKeyZPressed(){}
    public void noKeysPressed(){}
    public void keyEnterPressed(){}
    public void setPlayerStill(){}
    public void reset(){}
}