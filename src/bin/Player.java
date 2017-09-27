package bin;

/**
 * Created by Sajid on 2016-05-01.
 */
import bin.BattleElements.Item;
import bin.BattleElements.BattleObjects.PlayerHeart;
import bin.BattleElements.Weapon;
import bin.OverWorldElements.DIRECTION;
import bin.OverWorldElements.OWObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Handles all player properties, handling his weapons and items, as well as player movement and rendering in overworld
public class Player extends OWObject {

    //sprites for movement and standing in overworld
    private ArrayList<Image> upSprites;
    private ArrayList<Image> rightSprites;
    private ArrayList<Image> leftSprites;
    private ArrayList<Image> downSprites;
    //counter for scrolling through movement animations
    private int index;
    private int delay;
    //whether player is standing or not
    private boolean standing;
    //walk increment
    private final int WALK_SPEED = 4;
    //which directions the player is moving
    private boolean moveUp,moveLeft,moveRight,moveDown;

    private final String name = "???";
    //currency used to buy weapons and items
    private int sol;
    private int hp;
    private int maxHp;
    //heart object used in battle
    private PlayerHeart heart;
    //weapons the player has
    private ArrayList<Weapon> weapons;
    //current weapon player has equipped
    private Weapon currWeapon;
    //all items the player has
    private ArrayList<Item> items;
    //notice for when inventory is full
    private TextBoxState itemsIsFull;
    //initializes player properties and sprites
    public Player(int x, int y, int h, int w, DIRECTION d, int hp, int sol, Weapon wep){
        super(x, y, w, h, d);
        String src; //image name thats gonna be used to load sprites
        upSprites = new ArrayList<>();
        rightSprites = new ArrayList<>();
        leftSprites = new ArrayList<>();
        downSprites = new ArrayList<>();
        for(int i = 1; i < 8; i++){
            src = "up"+Integer.toString(i)+".png";
            upSprites.add(new ImageIcon("src\\resources\\Images\\"+src).getImage());
            src = "left"+Integer.toString(i)+".png";
            leftSprites.add(new ImageIcon("src\\resources\\Images\\"+src).getImage());
            src = "right"+Integer.toString(i)+".png";
            rightSprites.add(new ImageIcon("src\\resources\\Images\\"+src).getImage());
            src = "down"+Integer.toString(i)+".png";
            downSprites.add(new ImageIcon("src\\resources\\Images\\"+src).getImage());
        }
        index = 1;
        delay = 0;
        standing = true;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        moveUp = false;
        this.hp = hp;
        maxHp = hp;
        this.sol = sol;
        currWeapon = wep;
        weapons = new ArrayList<>();
        items = new ArrayList<>();
        heart = new PlayerHeart();
        //item is full prompt
        itemsIsFull = new TextBoxState(this,StateType.TextBox,new String[]{"Sorry you can't hold that item",
                "Your inventory is full!"}, Color.black,Color.white,Color.BLACK, 20, 550, 850, 100,30);

    }
    //rendering the player according to direction they're facing and whether they are moving or not
    public void render(Graphics2D g, int x, int y){
        Image currSprite;
        //standing sprite render options
        if(standing){
            if(direction == DIRECTION.Down){
                currSprite = downSprites.get(0);
            }
            else if(direction == DIRECTION.Right){
                currSprite = rightSprites.get(0);
            }
            else if(direction == DIRECTION.Left){
                currSprite = leftSprites.get(0);
            }
            else{
                currSprite = upSprites.get(0);
            }

        }
        //movement sprite render options, index constantly changes for movement animation
        else{
            if(direction == DIRECTION.Down){
                currSprite = downSprites.get(index);
            }
            else if(direction == DIRECTION.Right){
                currSprite = rightSprites.get(index);
            }
            else if(direction == DIRECTION.Left){
                currSprite = leftSprites.get(index);
            }
            else{
                currSprite = upSprites.get(index);
            }
        }
        int w = currSprite.getWidth(null);
        int h = currSprite.getHeight(null);
        g.drawImage(currSprite,x,y,w,h,null);
//        g.setColor(Color.magenta);
//        g.drawRect(x,y,width,height);
//        int playerCenterX = x + width/2;
//        int playerCenterY = y + height/2;
//        g.setColor(Color.green);
//        g.fillRect(playerCenterX,playerCenterY,4,4);

    }

    //methods to stop movement, adjusts movement and direction the character is facing according to if user is holding any
    //other keys (which makes the boolean for that move direction true)
    public void noMoveRight(){
        moveRight = false;
        if(moveLeft){
            direction =DIRECTION.Left;
        }
        else{
            if(moveUp){
                direction = DIRECTION.Up;
            }
            if(moveDown){
                direction = DIRECTION.Down;
            }
        }
    }
    public void noMoveLeft(){
        moveLeft = false;
        if(moveRight){
            direction =DIRECTION.Right;
        }
        else{
            if(moveUp){
                direction = DIRECTION.Up;
            }
            if(moveDown){
                direction = DIRECTION.Down;
            }
        }

    }
    public void noMoveDown(){
        moveDown = false;
        if(moveUp){
            direction = DIRECTION.Up;
        }
        else{
            if(moveRight){
                direction = DIRECTION.Right;
            }
            if(moveLeft){
                direction = DIRECTION.Left;
            }
        }
    }
    public void noMoveUp(){
        moveUp = false;
        if(moveDown){
            direction = DIRECTION.Down;
        }
        else{
            if(moveRight){
                direction = DIRECTION.Right;
            }
            if(moveLeft){
                direction = DIRECTION.Left;
            }
        }
    }

    //used to check whether theres vertical or horizontal movement when movement methods are called to make sure
    //3 directions are not being triggered at once (ie up, left, down)
    public boolean verticalMovement(){
        return moveUp || moveDown;
    }

    public boolean horizontalMovement(){
        return moveRight || moveLeft;
    }

    //method to walk for all directions
    //only gets triggered if there isnt already a vertical and horizontal and adjusts the character facing that specific direction
    //if he is not moving any other direction
    public void walkRight(){
        if(verticalMovement() && horizontalMovement()){
            return;
        }
        standing = false;
        moveRight = true;
        if(!(moveUp || moveDown)){
            direction = DIRECTION.Right;
        }
    }

    public void walkLeft(){
        if(verticalMovement() && horizontalMovement()){
            return;
        }
        standing = false;
        moveLeft = true;
        if(!(moveUp || moveDown)){
            direction = DIRECTION.Left;
        }
    }

    public void walkUp(){
        if(verticalMovement() && horizontalMovement()){
            return;
        }
        standing = false;
        moveUp = true;
        if(!(moveRight || moveLeft)){
            direction = DIRECTION.Up;
        }
    }

    public void walkDown(){
        if(verticalMovement() && horizontalMovement()){
            return;
        }
        standing = false;
        moveDown = true;
        if(!(moveRight || moveLeft)){
            direction = DIRECTION.Down;
        }
    }


    //methods to coordinates of character
    public void moveUp(){
        y-=WALK_SPEED;
    }
    public void moveDown(){
        y+=WALK_SPEED;
    }
    public void moveRight(){
        x+= WALK_SPEED;
    }
    public void moveLeft(){
        x-= WALK_SPEED;
    }
    //continuously updates player coordinate depending on which directions is being moved (boolean is true)
    //"else if" statements used to make sure there is no conflict with moving opposite directions
    //also increases index used which scrolls through images for movement animation
    public void tick(){
        if(moveDown){
            y+=WALK_SPEED;
        }
        else if(moveUp){
            y-=WALK_SPEED;
        }
        if(moveRight){
            x+=WALK_SPEED;
        }
        else if(moveLeft){
            x-=WALK_SPEED;
        }
        if(!(moveUp || moveRight || moveLeft || moveDown)){ //no direction movement means player is standing
            standing = true;
            //counter for animation is reset
            delay = 0;
            index = 1;
        }
        else{
            standing = false;
            delay++;
            if(delay%6 == 0){   //delay ensures animations are't too fast
                index = index == 6? 1: index+1;
            }
        }
    }

    //handles collision between player and background using pixel collision of mask
    //pixels from mask from surrounding directions are kept track of and if its black, the player is moved in opposite direction
    public void backgroundCollision(BufferedImage mapMask){
        //base point is from the center of the player
        int playerCenterX = x + width/2;
        int playerCenterY = y + height/2;
        //surrounding pixel value
        int upPixel = mapMask.getRGB(playerCenterX,y - WALK_SPEED);
        int rightPixel = mapMask.getRGB(x + WALK_SPEED + width,playerCenterY);
        int leftPixel = mapMask.getRGB(x - WALK_SPEED,playerCenterY);
        int downPixel = mapMask.getRGB(playerCenterX,y + WALK_SPEED + height);
        if(sumPixelARGB(upPixel) == 0){ //checks whether its black and moves in opposite direction
            y += WALK_SPEED;
        }
        if(sumPixelARGB(leftPixel) == 0){
            x += WALK_SPEED;
        }
        if(sumPixelARGB(rightPixel) == 0){
            x -= WALK_SPEED;
        }
        if(sumPixelARGB(downPixel) == 0){
            y -= WALK_SPEED;
        }
    }
    //converts hexadecimal value color value and the sum is returned to indicate whether the pixel is black or not
    //(black pixel color sum would be 0)
    public int sumPixelARGB(int pixel) {
        //int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        return red + green + blue;
    }


    public DIRECTION getDirection(){
        return direction;
    }

    public int getWalkSpeed(){
        return WALK_SPEED;
    }
    public void setX(int x){this.x = x;}

    public void setY(int y){this.y = y;}

    public void setDirection(DIRECTION d){direction = d;}

    //makes player stand still
    public void standStill(){
        standing = true;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        moveUp = false;
    }

    public String toString(){
        return name;
    }

    public void setHp(int hp){
        this.hp = hp;
    }

    public int getHp(){
        return hp;
    }

    public void loseHp(int dmg){
        hp -= dmg;
    }

    public int getMaxHp(){return maxHp;}

    public PlayerHeart getHeart(){
        return heart;
    }

    public Weapon getWeapon(){return currWeapon;}

    public ArrayList<Weapon> getAllWeapons(){return weapons;}

    public int getSol(){return sol;}

    public void equipWeapon(Weapon weapon) {
        currWeapon = weapon;
    }

    //equips weapon and adds to inventory of weapons while subtracting cost
    public void buyWeapon(Weapon weapon){
        weapons.add(weapon);
        sol -= weapon.getCost();
        currWeapon = weapon;
    }
    //item is added to inventory and cost is subtracted unless inventory is full in which player is prompted
    public void buyItem(Item item){
        if(items.size() == 8){
            itemsIsFull.reset();
            GameStateManager.addState(itemsIsFull);
        }
        else{
            items.add(item);
            sol -= item.getCost();
        }

    }
    //same as buying items but no sol cost subtracted (its given duh)
    public boolean getItem(Item item){
        if(items.size() == 8){
            itemsIsFull.reset();
            GameStateManager.addState(itemsIsFull);
            return false;
        }
        else{
            items.add(item);
            return true;
        }
    }

    public void getWeapon(Weapon weapon){weapons.add(weapon);}
    //item is used and taken away from inventory and hp restored
    public void useItem(int index){
        Item item = items.get(index);
        hp += item.healAmount();
        hp = hp > maxHp? maxHp : hp;    //caps at max hp
        items.remove(item);
    }

    public int itemAmount(){
        return items.size();
    }

    //whether player can afford something depending on specified amount of sol
    public boolean canAfford(int amount){
        return sol - amount >= 0;
    }

    public ArrayList<Item> getItems(){
        return items;
    }

    public void loseSol(int amt){
        sol -= amt;
    }

    public void setSol(int amt){sol = amt;}

    public void setCurrWeapon(Weapon weapon){
        this.currWeapon = weapon;
    }

    public void setWeapons(ArrayList<Weapon> weapons){
        this.weapons = weapons;
    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
    }

}