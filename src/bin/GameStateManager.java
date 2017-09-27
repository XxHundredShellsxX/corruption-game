package bin;

import bin.BattleElements.Battle;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

//Manages which "state" to draw and update and also is used to transition between "states"
//"states" = what type of scene the game is currently rendering and running
public class GameStateManager extends JPanel implements KeyListener{

    //states are held in LinkedList to layer them on top of eachother for convenience
    private static LinkedList<GameState> states;
    private static GameState currState = null;
    private static Game main;   //reference to game
    //integers used to specify whether state is being added or removed
    private static final int ADD_STATE = 1;
    private static final int REMOVE_STATE = 2;

    //initializes the game and adds the key listener used for all keyboard inputs in the states
    public GameStateManager(Game game) {
        states = new LinkedList<>();
        main = game;
        requestFocus();
        addKeyListener(new KeyInput(this));
    }

    public void addNotify(){
        super.addNotify();
        requestFocus();
        main.start();
    }

    public void paintComponent(Graphics g){
        currState.render(g);
    }

    public void tick(){
        currState.tick();
    }
    //adds new specified state, which also becomes the current state
    public static void addState(GameState s){
        states.add(s);
        currState = states.getLast();
        if(currState.getState() == StateType.DeadEndScreen){    //screen is resized when the new state is the dead end screen
            main.setSize(main.WIDTH,main.HEIGHT);
            main.setLocationRelativeTo(null);       //also becomes centred
        }
        else if(currState.getState() == StateType.TextBox){
            s.reset();
        }

    }

    //transition used to go from any state to battle
    public static void addStateTransition(GameState s,Color color){
        currState = new ScreenTransition(currState.getPlayer(),s.getState(),color, currState, s,main,ADD_STATE);
    }
    //transition from battle back to the previous state
    public static void removeStateTransition(Color color){
        GameState removedState = currState; //the state being removed is the current one
        states.remove(currState);
        GameState newState = states.getLast();  //the last state from that is the new one that's gonna transitioned to
        currState = new ScreenTransition(currState.getPlayer(),newState.getState(),color, removedState, newState,main,REMOVE_STATE);
    }

    //removes the current state and the one before that becomes the current one
    public static void removeState(GameState s){
        states.remove(s);
        currState = states.getLast();
    }

    public static void setState(GameState state){
        currState = state;
    }

    public static GameState getCurrState(){
        return currState;
    }

    //just overloaded methods as this extends keyListener
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e){}
    public void keyPressed(KeyEvent e) {}

}
