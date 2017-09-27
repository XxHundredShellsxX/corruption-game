package bin.OverWorldElements;

import bin.BattleElements.Battle;
import bin.BattleElements.Item;
import bin.BattleElements.Weapon;
import bin.GameStateManager;
import bin.Player;
import bin.Prompts.TextBoxToState;
import bin.TextBoxState;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sajid on 2016-05-10.
 */
//NPC which the player can talk to and some give items or even battle
public class NPC extends OWObject {

    //properties
    private ArrayList<Image> sprites;
    private TextBoxState dialogue;
    private TextBoxState afterItem = null;
    private Item heldItem;
    private TextBoxToState battlePrompt;


    enum FACE_DIRECTION{
        Up(),
        Right(),
        Down(),
        Left(),
        None()
    }

    private FACE_DIRECTION faceDirection = FACE_DIRECTION.None; //direction which NPC faces

    //coordinates, dimensions and other properties are initalized
    public NPC(int x, int y, int h, int w, DIRECTION d, ArrayList<Image> s, TextBoxState dialogue){
        super(x, y, w, h, d);
        sprites = s;
        this.dialogue = dialogue;
    }
    //additional dialogue for after NPO loses item and item NPC gives
    public NPC(int x, int y, int h, int w, DIRECTION d, ArrayList<Image> s, TextBoxState dialogue,TextBoxState afterItem, Item item){
        super(x, y, w, h, d);
        sprites = s;
        this.dialogue = dialogue;
        this.afterItem = afterItem;
        heldItem = item;
    }
    //takes in battle that triggers after talking
    public NPC(int x, int y, int h, int w, DIRECTION d, ArrayList<Image> s, TextBoxToState battlePrompt,TextBoxState afterItem){
        super(x, y, w, h, d);
        sprites = s;
        this.dialogue = dialogue;
        this.afterItem = afterItem;
        this.battlePrompt = battlePrompt;
    }
    //renders appropriate sprite depending on direction
    public void render(Graphics2D g, int x, int y){

        Image currSprite;

        if(direction == DIRECTION.Up){
            currSprite = sprites.get(0);
        }
        else if(direction == DIRECTION.Right){
            currSprite = sprites.get(1);
        }
        else if(direction == DIRECTION.Down){
            currSprite = sprites.get(2);
        }
        else{
            currSprite = sprites.get(3);
        }

        int w = currSprite.getWidth(null);
        int h = currSprite.getHeight(null);
        g.drawImage(currSprite,x,y,w,h,null);
        g.setColor(Color.black);
        //g.drawRect(x,y,width,height);
    }

    public TextBoxState getDialogue(){return dialogue;}


    public void interact(Player p){
        //checks whether player is colliding with npc from above or below
        if(p.getX() < (x + width) && (p.getX() + p.getWidth() > x) && (p.getY() > y + height - p.getWalkSpeed() || p.getY() + p.getHeight() <= y + p.getWalkSpeed())) {
            if(p.getY() <= y + height + p.getWalkSpeed() && p.getY() > y + height - p.getWalkSpeed()){
                faceDirection = FACE_DIRECTION.Down; //keeps track of direction npc should face when talked to
                if(p.getY() <= (y + height)){ // when trying to move through npc, player is moved opposite direction
                    p.moveDown();
                }
            }
            else if((p.getY() + p.getHeight()) >= y - p.getWalkSpeed() && p.getY() + p.getHeight() <= y + p.getWalkSpeed()){
                faceDirection = FACE_DIRECTION.Up;
                if((p.getY() + p.getHeight()) >= y){
                    p.moveUp();
                }
            }
            else{
                faceDirection = FACE_DIRECTION.None;
            }
        }
        //checks whether player is colliding with np from left or right
        else if(p.getY() < (y + height) && (p.getY() + p.getHeight()) > y ){
            if(p.getX() <= x + width + p.getWalkSpeed() && p.getX() >= x + width - p.getWalkSpeed()){
                faceDirection = FACE_DIRECTION.Right;
                if(p.getX() <= x + width){
                    p.moveRight();
                }
            }
            else if(p.getX() + p.getWidth() >= x - p.getWalkSpeed() && p.getX() + p.getWidth() <= x + p.getWalkSpeed()){
                faceDirection = FACE_DIRECTION.Left;
                if(p.getX() + p.getWidth() >= x){
                    p.moveLeft();
                }
            }
            else{
                faceDirection = FACE_DIRECTION.None;
            }
        }
        else{
            faceDirection = FACE_DIRECTION.None;
        }
    }
    //makes sure player is facing the enemy when talking (rectangle of player hits border of enemy, but he also has
    //to be facing the enemy from the right direction)
    public boolean talks(Player p){

        if(faceDirection == FACE_DIRECTION.None){
            return false;
        }
        if(faceDirection == FACE_DIRECTION.Up && p.getDirection() == DIRECTION.Down){
            direction = DIRECTION.Up;
            return true;
        }
        else if(faceDirection == FACE_DIRECTION.Down && p.getDirection() == DIRECTION.Up){
            direction = DIRECTION.Down;
            return true;
        }
        else if(faceDirection == FACE_DIRECTION.Left && p.getDirection() == DIRECTION.Right){
            direction = DIRECTION.Left;
            return true;
        }
        else if(faceDirection == FACE_DIRECTION.Right && p.getDirection() == DIRECTION.Left){
            direction = DIRECTION.Right;
            return true;
        }

        return false;
    }
    //handles battle starting, or if NPC has to give an item and its dialogue changes, otherwise nothing happens but dialgue started
    public void itemInteraction(Player p){
        if(battlePrompt != null){
            if(battlePrompt.getIsDone()){
                battlePrompt = null;
                dialogue = afterItem;
                return;
            }
            battlePrompt.resetPrompt();
            GameStateManager.addState(battlePrompt);


            return;
        }
        if(afterItem == null){
            GameStateManager.addState(dialogue);
            return;
        }
        if(!(heldItem == null)){
            if(p.getItem(heldItem)){
                heldItem = null;
            }
        }
        else{
            dialogue = afterItem;
        }
        GameStateManager.addState(dialogue);
    }
    //makes the item the NPC holding to null
    public void setItemNull(){
        heldItem = null;
    }

}
