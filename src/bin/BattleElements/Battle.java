package bin.BattleElements;

import java.awt.*;

import bin.*;
import bin.Prompts.DeadEndScreen;
import bin.Prompts.TextBoxToState;

/**
 * Handels all the diferent battleStates, such has when
 * the enemy is attacking, or when the player is attacking.
 * Allows Player to run away, and also handels item usage.
 */
public class Battle extends GameState {

    private final int ATTACK = 0, TALK = 1, ITEMS = 2, RUN = 3;
    private int currPos = ATTACK;
    private boolean pressedDown;
    private static BattleChoice currChoice;
    private Enemy enemy;
    private TextBoxState enemyDialogue;
    private TextBoxState defeatText;
    private TextBoxState noItems;
    private TextBoxState hpFull;
    private TextBoxState cantRun;
    private TextBoxState warning;
    private boolean enemyFade;
    private float alpha = 1.00f;
    private int itemIndex;
    private int round;

    public Battle(Player player,Enemy enemy) {
        super(player, StateType.Battle);
        this.enemy = enemy;
        pressedDown = false;
        currChoice = BattleChoice.NONE;
        enemyDialogue = enemy.getDialogue();
        defeatText = new TextBoxState(player,StateType.TextBox,(new String[]{"The " + enemy + " was defeated..."}),
                Color.white,Color.black,Color.white,25,400,550,80,22);
        noItems = new TextBoxState(player,StateType.TextBox,(new String[]{"You have no items to use...."}),
                Color.white,Color.black,Color.white,25,400,550,80,22);
        hpFull = new TextBoxState(player,StateType.TextBox,(new String[]{"Can't eat that! hp is full.."}),
                Color.white,Color.black,Color.white,25,400,550,80,22);
        cantRun = new TextBoxState(player,StateType.TextBox,(new String[]{"Can't run from battle!"}),
                Color.white,Color.black,Color.white,25,400,550,80,22);
        warning = new TextBoxState(player,StateType.TextBox,(new String[]{"You don't have enough SOL left",
                "Will you still continue with this decision?"}),
                Color.white,Color.black,Color.white,25,400,550,80,22);
        enemyFade = false;
        itemIndex = 0;
        round = 1;
    }

    public void tick() {

        if (currChoice == BattleChoice.NONE) {
            currPos = Game.clamp(currPos, 3, 0);
        }
        else if(currChoice == BattleChoice.ENEMYDEFEAT){
            if(enemyFade){
                GameStateManager.addState(defeatText);
                currChoice = BattleChoice.BATTLEEND;
            }
        }
        else if(currChoice == BattleChoice.BATTLEEND){
            player.standStill();
            GameStateManager.removeStateTransition(Color.WHITE);
            currChoice = BattleChoice.NONE;
            enemy.reset();
        }
        else if(currChoice == BattleChoice.ITEM){
            itemIndex = Game.clamp(itemIndex,player.getItems().size()-1,0);
        }

    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        drawBasicBattle(g, player);

        if(!(currChoice == BattleChoice.ENEMYDEFEAT || enemyFade)){
            enemy.render(g2);
        }
        if (currChoice == BattleChoice.NONE) {
            g.setColor(Color.yellow);
            g.drawRect(20 + 150 * currPos, 500, 110, 50);
        }
        else if(currChoice == BattleChoice.ITEM){
            g.setColor(Color.white);
            g.fillRect(20,395,560,90);
            g.setColor(Color.black);
            g.fillRect(25,400,550,80);
            for(int i = 0; i < player.getItems().size(); i++){
                String item = player.getItems().get(i).getName();
                if(i%4 == 0){
                    drawText(g2,"Berlin Sans FB Demi", item,50,385 + 40*((i+4)/4),20,Color.white);
                }
                else if((i+1)%4 == 0){
                    drawText(g2,"Berlin Sans FB Demi", item,440,385 + 40*((i+1)/4),20,Color.white);
                }
                else if((i+2)%4 == 0){
                    drawText(g2,"Berlin Sans FB Demi", item,310,385 + 40*((i+2)/4),20,Color.white);
                }
                else{
                    drawText(g2,"Berlin Sans FB Demi", item,180,385 + 40*((i+3)/4),20,Color.white);
                }
            }
            g.setColor(Color.white);
            //draws the option arrow to correct location based on item index
            //x value according mod 4 for every 130 pixels
            //y according to whether its in the first or 2nd row
            drawOptionArrow(g2,30 + 130*(itemIndex%4),375 + 40*((itemIndex + 4)/4));
        }
        else if(currChoice == BattleChoice.ENEMYDEFEAT){
            if(!enemyFade){
                //set the opacity
                g2.setComposite(makeTransparent(alpha));
                //do the drawing here
                //increase the transparency and repaint
                alpha -= 0.05f;
                enemy.render(g2);
                if(alpha <= 0.0f){
                    alpha = 0.0f;//when invisible, its done
                    enemyFade = true;
                }
                g2.setComposite(makeTransparent(1));
            }
        }
//        else if(currChoice != BattleChoice.BATTLEEND){
//            currState.render(g);
//        }
    }

    public static void drawBasicBattle(Graphics g, Player player){
        Graphics2D g2 = (Graphics2D) g;    //uses graphics 2D
        //renders text
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.SMALL_WIDTH, Game.SMALL_HEIGHT);
//        if(currChoice == bin.BattleElements.BattleChoice.NONE) {
        drawBattleOptions(g2, "ATTACK", 20, 500, 20);
        drawBattleOptions(g2, "TALK", 170, 500, 20);
        drawBattleOptions(g2, "ITEMS", 320, 500, 20);
        drawBattleOptions(g2, "RUN", 470, 500, 20);
//        }
        drawPlayerHp(g2,260,470,20, player);
    }

    public static void drawOptionArrow(Graphics2D g, int x, int y){
        Polygon arrow = new Polygon(new int[]{x, x + 15, x,}, new int[]{y, y + 5, y+ 10},3);
        g.fillPolygon(arrow);
    }

    //sets the transparency by specific alpha value
    public static AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }

    public static void drawText(Graphics2D g,String font, String text, int x ,int y, int size, Color color ){
        g.setColor(color);
        g.setFont(new Font(font, Font.PLAIN,size));
        g.drawString(text,x,y);
    }

    public static void drawBattleOptions(Graphics g2, String text, int x, int y, int size) {
        g2.setColor(Color.white);
        g2.setFont(new Font("Eras Bold ITC", Font.PLAIN, size));
        g2.drawRect(x, y, 110, 50);//bottom part of screen
        g2.drawString(text, x + (7 - text.length()) * 9, y + 30);
    }

    public static void drawPlayerHp(Graphics g2,int x, int y, int size, Player player){
        g2.setColor(Color.white);
        g2.setFont(new Font("Eras Bold ITC", Font.PLAIN, size));
        g2.drawString("HP: " + player.getHp() + " / " + player.getMaxHp(), x , y);
    }

    public Enemy getEnemy(){return enemy;}


    @Override
    public void keyLeftPressed() {
        if (currChoice == BattleChoice.NONE) {
            if (!pressedDown) {
                currPos--;
                pressedDown = true;
            }
        }
//        else if (currChoice == BattleChoice.PLAYERATTACK && !pressedDown) {
//            currState.receiveInput(4);
//            pressedDown = true;
//        }

        // TODO: FOEATTACK
//        else if (currChoice == BattleChoice.FOEATTACK){
//            currState.keyLeftPressed();
//        }
        if(currChoice == BattleChoice.ITEM){
            itemIndex--;
        }
    }

    @Override
    public void keyRightPressed() {
        if (currChoice == BattleChoice.NONE) {
            if (!pressedDown) {
                currPos++;
                pressedDown = true;
            }
        }
//        else if (currChoice == BattleChoice.PLAYERATTACK && !pressedDown) {
//            currState.receiveInput(2);
//            pressedDown = true;
//        }
//        else if (currChoice == BattleChoice.FOEATTACK){
//            currState.keyRightPressed();
//        }
        if(currChoice == BattleChoice.ITEM){
            itemIndex++;
        }
    }

    @Override
    public void keyUpPressed() {
//        if (currChoice == BattleChoice.PLAYERATTACK && !pressedDown) {
//            currState.receiveInput(1);
//            pressedDown = true;
//        }
//        else if (currChoice == BattleChoice.FOEATTACK){
//            currState.keyUpPressed();
//        }
        if(currChoice == BattleChoice.ITEM){
            itemIndex-=4;
        }
    }

    @Override
    public void keyDownPressed() {
//        if (currChoice == BattleChoice.PLAYERATTACK && !pressedDown) {
//            currState.receiveInput(3);
//            pressedDown = true;
//        }
//        else if (currChoice == BattleChoice.FOEATTACK){
//            currState.keyDownPressed();
//        }
        if(currChoice == BattleChoice.ITEM){
            itemIndex+=4;
        }
    }

    public void noKeyUpPressed(){
//        if (currChoice == BattleChoice.FOEATTACK){
//            currState.noKeyUpPressed();
//        }

    }
    public void noKeyDownPressed(){
//        if (currChoice == BattleChoice.FOEATTACK){
//            currState.noKeyDownPressed();
//        }

    }
    public void noKeyLeftPressed(){
//        if (currChoice == BattleChoice.FOEATTACK){
//            currState.noKeyLeftPressed();
//        }
    }
    public void noKeyRightPressed(){
//        if (currChoice == BattleChoice.FOEATTACK){
//            currState.noKeyRightPressed();
//        }
    }

    @Override
    public void keyZPressed() {
        if (currChoice == BattleChoice.NONE) {
            if (currPos == ATTACK) {
                currChoice = BattleChoice.NONE;
                GameStateManager.addState(new PlayerAttack(player, enemy, round));

                round++;

            } else if (currPos == TALK) {
                enemyDialogue.reset();
                GameStateManager.addState(enemyDialogue);
            } else if (currPos == ITEMS) {
                if( player.itemAmount() > 0){
                    currChoice = BattleChoice.ITEM;
                }
                else{
                    noItems.reset();
                    GameStateManager.addState(noItems);
                }

            } else if (currPos == RUN) {
                if(enemy.isBoss()){
                    cantRun.reset();
                    GameStateManager.addState(cantRun);
                }
                else{
                    if(player.canAfford(500)){
                        player.loseSol(500);
                        player.standStill();
                        GameStateManager.removeStateTransition(Color.WHITE);
                        enemy.reset();
                    }
                    else{
                        TextBoxToState useSol = new TextBoxToState(player,StateType.Yes_No_TextBox,warning,true,new DeadEndScreen(player,StateType.DeadEndScreen));
                        GameStateManager.addState(useSol);
                    }
                }
            }
        }
        //if z is pressed while the attack is animating, it is skipped
//        else if(currChoice == BattleChoice.PLAYERATTACK && !pressedDown && !currState.isFinished() && currState.isAnimating()){
//            currState.finish();
//        }
        else if(currChoice == BattleChoice.TALK && !pressedDown){
            enemyDialogue.proceed();
            pressedDown = true;
        }
        else if(currChoice == BattleChoice.ITEM && !pressedDown){
            if(player.getHp() == player.getMaxHp()){
                hpFull.reset();
                GameStateManager.addState(hpFull);
            }
            else{
                player.useItem(itemIndex);
                pressedDown = true;
            }
            //CHANGE THIS AS IT WILL BE ENEMIES TURN AFTER USING ITEM SO ITS NOT NECESSARY
            if(player.itemAmount() == 0){
                currChoice = BattleChoice.NONE;
            }
        }

    }

    @Override
    public void keyXPressed() {
        if (currChoice == BattleChoice.ITEM || currChoice == BattleChoice.TALK) {
            currChoice = BattleChoice.NONE;
        }
    }

    @Override
    public void noKeysPressed() {
        pressedDown = false;
    }


    @Override
    public String toString(){
        return "Battle";
    }

    public static void setCurrChoice(BattleChoice currChoice) {
        Battle.currChoice = currChoice;
    }

//    public static void setCurrState(BattleState currState) {
//        Battle.currState = currState;
//    }
}