package bin.BattleElements;

import bin.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sajid on 2016-04-22.
 */
//The player attack which consists of random assortment of arrows that the player has to hit at the appropriate time
public class PlayerAttack extends BattleState {
    //properties
    private int numEvents;  //number of arrows
    private int strength;
    private int boundX,boundY;  //limits for where the arrows can still be between do deal damage when pressed
    private int hitsLanded;
    private int index;
    private int ArrowDist;
    private Arrow currArrow;
    private boolean finished;
    private boolean animate;
    private ArrayList<Arrow> keyArrows;
    private ArrayList<Image> weaponAnimation;
    private int weaponAnimIndex;
    private int weaponAnimationDelay;
    private int delay;
    private final int LEFT = 1, UP = 2, TOP_LEFT = 3;

    private int round;
    private boolean pressedDown;

    //initializes properties
    public PlayerAttack(Player player, Enemy enemy, int round){
        super(player, enemy, StateType.PlayerAttack);
        Weapon weapon = player.getWeapon();
        pressedDown = false;
        strength = weapon.getStrength();
        numEvents = weapon.getNumHits();
        ArrowDist = weapon.getHitInterval();
        int type = weapon.getType();
        int speed = weapon.getSpeed();
        weaponAnimation = weapon.attackSprites();
        weaponAnimIndex = 0;
        weaponAnimationDelay = weapon.getAnimationDelay();
        delay = 0;
        keyArrows = new ArrayList<>();
        boundX = 470;
        boundY = 350;
        index = 0;
        this.round = round;
        int dir;
        int startDist = ArrowDist * numEvents;
        for (int i = numEvents; i > 0; i--) {
            int key = (int) (Math.random() * 4 + 1);    //determines which arrow direction randomly
            if(type == 1){
                dir = 1;   //determines where arrow will come from randomly
            }
            else if(type == 2){
                dir = 2;   //determines where arrow will come from randomly
            }
            else if(type == 3){
                dir = (int) (Math.random() * 2 + 1);   //determines where arrow will come from randomly
            }
            else{
                dir = (int) (Math.random() * 3 + 1);   //determines where arrow will come from randomly
            }
            //determines what directions the arrows will come from
            if(dir == LEFT){                                          //LEFT
                int startX = i * ArrowDist - startDist;
                int startY = 350;
                keyArrows.add(new Arrow(startX,startY,key,speed,0,boundX,boundY));
            }
            else if(dir == UP){                                     //UP
                int startX = 470;
                int startY = i * ArrowDist - startDist;
                keyArrows.add(new Arrow(startX,startY,key,0,speed,boundX,boundY));
            }
            else if(dir == TOP_LEFT){                                     //TOP-LEFT
                int startX = i * ArrowDist - startDist + 120; //the +120 to properly align the arrows with the box (470-350 = 120)
                int startY = i * ArrowDist - startDist;
                keyArrows.add(new Arrow(startX,startY,key,speed,speed,boundX,boundY));
            }
        }
        hitsLanded = 0;
        currArrow = keyArrows.get(index);
        animate = false;
        finished = false;
    }


    public void tick() {

        if (keyArrows.isEmpty()){   //finished when no more arrows left
            if(hitsLanded == 0){
                GameStateManager.addState(new FoeAttack(player, enemy, round));
                GameStateManager.removeState(this);
                return;
            }
            animate = true;
            if(weaponAnimIndex == weaponAnimation.size()){
                enemyHit();
            }
            else{
                delay++;
                if(delay%weaponAnimationDelay == 0){
                    weaponAnimIndex ++;
                }
            }
        }
        else {
            //the player controls the next arrow after the current arrow passes a certain distance
            if (currArrow.getX() > boundX + ArrowDist/2 || currArrow.getY() > boundY + ArrowDist/2) {
                index++;
                if (index < numEvents) {
                    currArrow = keyArrows.get(index);
                }
            }
            //just removes all arrows when the last one is done fading or is off screen
            if(keyArrows.get(numEvents-1).getX() > 600 || keyArrows.get(numEvents-1).getY() > 600 || keyArrows.get(numEvents-1).isFinished()){
                keyArrows.clear();
            }
            //moves every arrow
            for (int i = 0; i < keyArrows.size(); i++) {
                Arrow arrow = keyArrows.get(i);
                arrow.tick();
            }
        }

    }

    public void drawCheckBox(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawRect(460,340,70,70);
    }


    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Battle.drawBasicBattle(g, player);
        enemy.render(g2);

        g2.setStroke(new BasicStroke(5));   //set arrow thickness
        drawCheckBox(g);
        if(!animate){
            for (int i = 0; i < keyArrows.size(); i++) {    //draw every arrow
                Arrow arrow = keyArrows.get(i);
                arrow.draw(g2);
            }
        }
        else if(animate && weaponAnimIndex < weaponAnimation.size()){
            Image currSprite = weaponAnimation.get(weaponAnimIndex);
            int w = currSprite.getWidth(null);
            int h = currSprite.getHeight(null);
            g.drawImage(currSprite,Game.SMALL_WIDTH/2 - w/2,Game.SMALL_HEIGHT/2 - h/2 - 100,w,h,null);
        }

    }



    public void enemyHit() {
        if(enemy.isDoneTakingDamage()){
            enemy.resetDamageDoneTaken();   //boolean for enemy being allowed to take damage is reset
            if(enemy.getHp() <= 0){ //battle state changes to defeat if its hp is zero
                Battle.setCurrChoice(BattleChoice.ENEMYDEFEAT);
            }
            else{   //otherwise goes to enemies attack
                GameStateManager.addState(new FoeAttack(player, enemy, round));
            }
            GameStateManager.removeState(this);

        }//tales damage only once
        else if(!(enemy.isHit() || enemy.isLosingHp() || enemy.isDoneTakingDamage())){
            enemy.takesDamage(hitsLanded *strength);
        }
    }


    public void receiveInput(int key) {
        //adds appropriate total hitsLanded according to the key pressed
        //and within how much of the key coordinates it was within
        if(!(index == numEvents - 1 && currArrow.isFading())) {
            if (key == currArrow.getKey()) {        //good
                hitsLanded++;
                currArrow.setFade(Color.YELLOW);
            } else if (key * 5 == currArrow.getKey()) {   //excellent
                hitsLanded += 2;
                currArrow.setFade(Color.GREEN);
            } else {                                  //miss
                currArrow.setFade(Color.RED);
            }
            index++;
            if (index < numEvents) {               //the current arrow becomes the next one
                currArrow = keyArrows.get(index);
            }
        }
    }

    @Override
    public void keyLeftPressed(){
        if(!pressedDown) {
            receiveInput(4);
            pressedDown = true;
        }
    }

    @Override
    public void keyRightPressed(){
        if(!pressedDown) {
            receiveInput(2);
            pressedDown = true;
        }
    }

    @Override
    public void keyUpPressed(){
        if(!pressedDown) {
            receiveInput(1);
            pressedDown = true;
        }
    }

    @Override
    public void keyDownPressed(){
        if(!pressedDown) {
            receiveInput(3);
            pressedDown = true;
        }
    }

    @Override
    public void keyZPressed(){
        if(!pressedDown && !finished && isAnimating()){
            animate = false;
            finished = true;
        }
    }

    @Override
    public void noKeysPressed() {
        pressedDown = false;
    }

    public boolean isAnimating(){
        return animate;
    }


    class Arrow{
        //moving arrow class that is used for player attack

        //properties
        private float alpha = 1.00f;
        private int x, y, dx, dy,key,boundX,boundY;
        private Color fadeColor;
        private final int INNERBOUNDSIZE = 20;
        private final int OUTTERBOUNDSIZE = 60;
        private boolean finished;
        private boolean startFade;
        private Polygon arrow;

        //initializes properties
        public Arrow(int x,int y,int key,int dx,int dy,int boundX,int boundY){
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.key = key;
            this.boundX = boundX;
            this.boundY = boundY;
            //different shape for each direction
            if (key == 1) {
                arrow = new Polygon(new int[]{x + 15, x+ 15, x, x + 25, x + 50, x + 35, x+ 35}, new int[]{y+ 50, y + 25, y+ 25, y, y + 25, y + 25, y + 50}, 7);
            } else if (key == 2) {
                arrow = new Polygon(new int[]{x, x+ 25, x + 25, x + 50, x + 25, x + 25, x}, new int[]{y + 15, y + 15, y, y + 25, y + 50, y + 35, y + 35}, 7);
            } else if (key == 3) {
                arrow = new Polygon(new int[]{x + 15, x + 15, x, x + 25, x + 50, x + 35, x + 35}, new int[]{y, y + 25, y + 25, y + 50, y + 25, y + 25, y}, 7);
            } else if (key == 4) {
                arrow = new Polygon(new int[]{x + 50, x + 25, x + 25, x, x + 25, x + 25, x + 50}, new int[]{y + 15, y + 15, y, y + 25, y + 50, y + 35, y + 35}, 7);
            }
            finished = false;
            startFade = false;

        }

        public void draw(Graphics2D g){
            //when fading
            if(startFade){
                //set the opacity
                g.setComposite(makeTransparent(alpha));
                //do the drawing here
                g.setColor(fadeColor);
                g.drawPolygon(arrow);
                //increase the transparency and repaint
                alpha -= 0.05f;
                if(alpha <= 0.0f){
                    alpha = 0.0f;//when invisible, its done
                    finished = true;
                }

            }
            //when not fading
            else{
                //keeps non-fading arrows opaque
                g.setComposite(makeTransparent(1));
                g.setColor(Color.white);
                g.draw(arrow);
            }
        }
        //sets the transparency by specific alpha value
        public AlphaComposite makeTransparent(float alpha){
            int type = AlphaComposite.SRC_OVER;
            return(AlphaComposite.getInstance(type,alpha));
        }
        //moves arrows when its not faded
        public void tick(){
            if(!startFade){
                arrow.translate(dx,dy);
                x += dx;
                y += dy;
            }
        }

        public int getKey(){
            //keeps the arrow centered to the bounds
            //returns what "key" it is holding
            if(x >= boundX - INNERBOUNDSIZE/2  && x <= boundX + INNERBOUNDSIZE/2
                    && y >= boundY - INNERBOUNDSIZE/2 && y <= boundY + INNERBOUNDSIZE/2){
                return key*5;   //twice the keys value when its within the inner bounds
            }
            else if(x >= boundX - OUTTERBOUNDSIZE/2 && x <= boundX + OUTTERBOUNDSIZE/2
                    && y >= boundY - OUTTERBOUNDSIZE/2 && y <= boundY + OUTTERBOUNDSIZE/2){
                return key;     //just the key value when its within outer bounds
            }
            return 0;           //otherwise it returns nothing, meaning the arrow wasn't within the bounds
        }

        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        //makes the arrow start fading and sets what colour its going to fade into
        public void setFade(Color color){
            fadeColor = color;
            startFade = true;
        }

        public boolean isFading(){
            return  startFade;
        }

        public boolean isFinished(){
            return finished;
        }

    }


    @Override
    public String toString(){
        return "PlayerAttack";
    }

}

