package bin;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//Handles all the keyboard inputs
//This is done by calling the specified key input methods for the current state
public class KeyInput extends KeyAdapter {

    //reference to the statemanager, boolean array of keys, arraylist of the significant keys for battle, and
    //the current state which key methods are being called
    private GameStateManager states;
    private boolean []keys;
    private ArrayList<Integer> sigKeys;
    private GameState currentState;


    public KeyInput(GameStateManager states) {

        this.states = states;
        keys = new boolean[KeyEvent.KEY_LAST+1];
        sigKeys = new ArrayList<>();
        sigKeys.add(KeyEvent.VK_LEFT);
        sigKeys.add(KeyEvent.VK_RIGHT);
        sigKeys.add(KeyEvent.VK_DOWN);
        sigKeys.add(KeyEvent.VK_UP);
        sigKeys.add(KeyEvent.VK_Z);
        sigKeys.add(KeyEvent.VK_X);
        currentState = null;
    }


    public void keyTyped(KeyEvent e) {}
    //checks which keys are pressed and triggers the methods for the current state
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        currentState = states.getCurrState();

        if(keys[KeyEvent.VK_LEFT]){
            currentState.keyLeftPressed();
        }
        if(keys[KeyEvent.VK_RIGHT]){
            currentState.keyRightPressed();
        }
        if(keys[KeyEvent.VK_Z]){
            currentState.keyZPressed();
        }
        if(keys[KeyEvent.VK_X]){
            currentState.keyXPressed();
        }
        if(keys[KeyEvent.VK_UP]){
            currentState.keyUpPressed();
        }
        if(keys[KeyEvent.VK_DOWN]){
            currentState.keyDownPressed();
        }
        if(keys[KeyEvent.VK_ENTER]){
            currentState.keyEnterPressed();
        }

    }
    //checks which keys are released and triggers the appropriate methods for the current state
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        //checks if all significant keys for battle are not pressed
        boolean keysPressed = false;
        for (int k : sigKeys) {
            if (keys[k]) {
                keysPressed = true;
            }
        }
        if(!keysPressed){
            currentState.noKeysPressed();
        }

        if (!(keys[KeyEvent.VK_LEFT])) {
            currentState.noKeyLeftPressed();
        }
        if (!(keys[KeyEvent.VK_UP])) {
            currentState.noKeyUpPressed();
        }
        if (!(keys[KeyEvent.VK_RIGHT])) {
            currentState.noKeyRightPressed();
        }
        if (!(keys[KeyEvent.VK_DOWN])) {
            currentState.noKeyDownPressed();
        }
        if (!(keys[KeyEvent.VK_Z])) {
            currentState.noKeyZPressed();
        }
    }

}