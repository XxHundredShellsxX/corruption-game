package bin.OverWorldElements;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import bin.*;
import bin.BattleElements.*;
import bin.Prompts.DeadEndScreen;
import bin.Prompts.TextBoxToState;

import javax.swing.*;

/**
 * Created by Sajid on 2016-04-29.
 */
//OverWorld which holds the current map and all of its objects, as well controls player movement from here
//Also handles the overworld menu which allows buying and equipping weapons and items
public class OverWorld extends GameState{
    //used to make sure keyboard actions aren't being triggered multiple times with just one press
    private boolean keysPressedDown;

    private NPC currNPC;
//    private Map maps;
    private WorldMap currMap;
    private float alpha, alphaIncrement;

    private enum OverWorldState{
        PlayerMove(),
        NPCTalk(),
        Menu(),
        MapSwitch()
    }
    private enum MenuState{
        None(),
        Buy(),
        Buy_Weapon(),
        Buy_Item(),
        Items(),
        Equip_Weapon(),
        Use_Item(),
    }
    private OverWorldState currState;
    //enums type ints to represent menu options when going through indexes
    private final int BUY = 0, ITEMS = 1, QUIT = 2;
    private final int BUY_WEAPON = 0, BUY_ITEM = 1;
    private final int EQUIP_WEAPON = 0, USE_ITEM = 1;
    //index for the options menu display
    private int MenuIndex;
    //index for the options in item/weapon display
    private int ItemIndex;

    private int spawnRate = 150;
    private boolean spawn;

    private ArrayList<Weapon> buyableWeapons;
    private ArrayList<Item> buyableItems;

    private MenuState menuState;
    //textbox for when hpisFull
    private TextBoxState hpIsFull;

    private boolean eventTest = true;
    //textbox for when sol is gonna reach below 0 warning
    private TextBoxState warning;
    //textbox for when player is trying to use an item but there are no items in inventory
    private TextBoxState noItems;

    private ConnectingMap nextMap;

    //initializing all properties

    public OverWorld(Player player, StateType type){
        super(player,type);
        currState = OverWorldState.PlayerMove;
        keysPressedDown = false;


        currMap = Load.getMap("Forest");

        menuState = MenuState.None;
        MenuIndex = 0;
        ItemIndex = 0;
        spawn = false;
        buyableItems = Load.getBuyableItems();
        buyableWeapons = Load.getBuyableWeapons();
        // Sorts Weapons/Items by sol Cost, so it is in order at the store
        Collections.sort(buyableWeapons,new Comparator<Weapon>(){
            public int compare(Weapon w1,Weapon w2){
                return new Integer(w1.getCost()).compareTo(
                        new Integer(w2.getCost()));
            }});
        Collections.sort(buyableItems,new Comparator<Item>(){
            public int compare(Item w1,Item w2){
                return new Integer(w1.getCost()).compareTo(
                        new Integer(w2.getCost()));
            }});

        //first buyable weapon is player's default weapon
        player.buyWeapon(buyableWeapons.get(0));
        buyableWeapons.remove(player.getWeapon());

        hpIsFull = new TextBoxState(player,StateType.TextBox,new String[]{"You can't eat that!",
        "Your HP is full!"}, Color.black,Color.white,Color.BLACK, 20, 550, 850, 100,30);



        warning = new TextBoxState(player,StateType.TextBox,new String[]{"You don't have enough SOL left",
                "Will you still continue with this decision?"}, Color.black,Color.white,Color.black, 20, 550, 850, 100,30);

        noItems = new TextBoxState(player,StateType.TextBox,new String[]{"You have no items in your inventory!"},
                Color.black,Color.white,Color.black, 20, 550, 850, 100,30);

    }



    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;	//uses graphics 2D
        //renders text
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.setRenderingHints(rh);
        g.setColor(Color.white);
        g.fillRect (0, 0, Game.WIDTH, Game.HEIGHT);
        currMap.render(g);

        if(currState == OverWorldState.MapSwitch){
            g2.setComposite(makeTransparent(alpha >= 1.0f? 1.0f:alpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0 ,0 , Game.WIDTH, Game.HEIGHT);
        }
        else if(currState == OverWorldState.Menu){
            //coordinates and dimensions used as baseline for menu drawing
            int x = Game.WIDTH - Game.WIDTH/3;
            int y = 0;
            int w = Game.WIDTH/3;
            int h = Game.HEIGHT/2 - 50;
            //menu box with player stats rendering
            g.setColor(Color.black);
            g2.fillRect(x,y,w - 10,h-40);
            g.setColor(Color.WHITE);
            g2.fillRect(x+5,y + 5,w-20,h-50);
            drawText(g2, "Berlin Sans FB Demi", "HP: " + player.getHp() + "/" + player.getMaxHp() ,x + 10,35,25,Color.black);
            drawText(g2, "Berlin Sans FB Demi", "SOL: " + player.getSol(),x + 140,35,25,Color.black);
            drawText(g2, "Berlin Sans FB Demi", "WEAPON: " + player.getWeapon().getName(),x + 10,65,25,Color.black);
            g2.fillRect(x, 80, w - 10, 10);
            //standard menu page render
            if(menuState == MenuState.None){
                drawOptionArrow(g2, x + 20, 110 + 40*MenuIndex);
                drawText(g2, "Berlin Sans FB Demi", "BUY",x + 50,130,30,Color.black);
                drawText(g2, "Berlin Sans FB Demi", "ITEMS",x + 50,130 + 40,30,Color.black);
                drawText(g2, "Berlin Sans FB Demi", "QUIT",x + 50,130 + 80,30,Color.black);
            }
            //the rendering of rest of pages
            else if(menuState == MenuState.Buy || menuState == MenuState.Buy_Item || menuState == MenuState.Buy_Weapon
                    || menuState == MenuState.Items || menuState == MenuState.Use_Item || menuState == MenuState.Equip_Weapon){
                drawOptionArrow(g2, x + 20, 110 + 40*MenuIndex);
                //Buying page render
                if(menuState == MenuState.Buy || menuState == MenuState.Buy_Item || menuState == MenuState.Buy_Weapon){
                    drawText(g2, "Berlin Sans FB Demi", "BUY WEAPON",x + 50,130,30,Color.black);
                    drawText(g2, "Berlin Sans FB Demi", "BUY ITEM",x + 50,130 + 40,30,Color.black);
                }
                //Equipping Weapon/Using item page render
                else{
                    drawText(g2, "Berlin Sans FB Demi", "EQUIP WEAPON",x + 50,130,30,Color.black);
                    drawText(g2, "Berlin Sans FB Demi", "USE ITEM",x + 50,130 + 40,30,Color.black);
                }
                //Inventory or Shop (Weapon/Item) render which is displayed beside main menu page
                if(menuState == MenuState.Buy_Item || menuState == MenuState.Buy_Weapon
                        || menuState == MenuState.Use_Item || menuState == MenuState.Equip_Weapon){
                    g.setColor(Color.black);
                    g2.fillRect(x - w,y + 80,w - 10,h);
                    g.setColor(Color.WHITE);
                    g2.fillRect(x - w +5,y + 85,w-20,h-10);
                    g.setColor(Color.black);
                    drawOptionArrow(g2, x - w + 10, 110 + 30*ItemIndex);
                    //all buyable weapons render
                    if(menuState == MenuState.Buy_Weapon){
                        displayWeapons(g2, x, y, w, h, buyableWeapons);
                    }
                    //all of players weapons render
                    else if(menuState == MenuState.Equip_Weapon){
                        displayWeapons(g2, x, y, w, h, player.getAllWeapons());
                    }
                    //all buyable weapons render
                    else if(menuState == MenuState.Buy_Item){
                        displayItems(g2, x, y, w, h, buyableItems);
                    }
                    //all player item render (only displays as long as player has items)
                    else if(menuState == MenuState.Use_Item && !player.getItems().isEmpty()){
                        displayItems(g2, x, y, w, h, player.getItems());
                    }

                }
            }
        }
        //placed here so NPC sprite would turn facing player direction before textbox appears
        //handles any item interaction NPC may have with player
        else if(currState == OverWorldState.NPCTalk){
             currNPC.itemInteraction(player);
             currState = OverWorldState.PlayerMove;
         }
    }

    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }
    //displays list of weapons according to a specified coordinate and dimensions
    //also displays the current weapon the arrow is hovering over below the menu pagee
    public void displayWeapons(Graphics2D g2, int x, int y, int w, int h, ArrayList<Weapon> weps){
        for(int i = 0; i < weps.size(); i++){
            Weapon wep = weps.get(i);
            drawText(g2, "Berlin Sans FB Demi", wep.getName(),x - w + 45,130 + 30*i,30,Color.black);
        }
        g2.setColor(Color.black);
        g2.fillRect(x - w + 50,y + h + 100,w + 200,h/2 - 20);
        g2.setColor(Color.WHITE);
        g2.fillRect(x - w + 55,y + h + 105,w + 190,h/2 - 30);
        Weapon currWeapon = weps.get(ItemIndex);
        drawText(g2, "Berlin Sans FB Demi", "COST: "+ currWeapon.getCost(),x - w + 70,y + h + 135 ,25,Color.black);
        drawText(g2, "Berlin Sans FB Demi", "HIT INTERVAL: "+ currWeapon.getHitInterval(),x - w + 220,y + h + 135 ,25,Color.black);
        drawText(g2, "Berlin Sans FB Demi", "DMG: "+ currWeapon.getStrength(),x - w + 70,y + h + 175 ,25,Color.black);
        drawText(g2, "Berlin Sans FB Demi", "NUM HITS: "+ currWeapon.getNumHits(),x - w + 200,y + h + 175 ,25,Color.black);
        drawText(g2, "Berlin Sans FB Demi", "SPEED: "+ currWeapon.getSpeed(),x - w + 390,y + h + 175 ,25,Color.black);
        drawText(g2, "Berlin Sans FB Demi", currWeapon.getDescription(),x - w + 70,y + h + 215,28,Color.black);
    }
    //same as weapon display but for items
    public void displayItems(Graphics2D g2, int x, int y, int w, int h, ArrayList<Item> items){
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            drawText(g2, "Berlin Sans FB Demi", item.getName(),x - w + 45,130 + 30*i,30,Color.black);
        }
        g2.setColor(Color.black);
        g2.fillRect(x - w + 50,y + h + 100,w + 200,h/2 - 60);
        g2.setColor(Color.WHITE);
        g2.fillRect(x - w + 55,y + h + 105,w + 190,h/2 - 70);
        Item currItem = items.get(ItemIndex);
        drawText(g2, "Berlin Sans FB Demi", "COST: "+ currItem.getCost(),x - w + 70,y + h + 135 ,25,Color.black);
        drawText(g2, "Berlin Sans FB Demi", "HEALS FOR: "+ currItem.healAmount() + " HP",x - w + 220,y + h + 135 ,25,Color.black);
        drawText(g2, "Berlin Sans FB Demi", currItem.getDescription(),x - w + 70,y + h + 175,28,Color.black);
    }


    //draws small arrow according to x and y which is used to indicate which options the player is currently on
    public void drawOptionArrow(Graphics2D g, int x, int y){
        Polygon arrow = new Polygon(new int[]{x, x + 25, x,}, new int[]{y, y + 10, y+ 20},3);
        g.fillPolygon(arrow);
    }
    //convenient text draw method
    public void drawText(Graphics2D g,String font, String text, int x ,int y, int size, Color color ){
        g.setColor(color);
        g.setFont(new Font(font, Font.PLAIN,size));
        g.drawString(text,x,y);
    }

    public void tick(){
        if(!eventTest){
            Load.getEvent("Forest").init();
            GameStateManager.addState(Load.getEvent("Forest"));
            //GameStateManager.addState(new E1_Knight_Attack(player,StateType.Event,backGround));
            eventTest = false;
        }

        if(currState == OverWorldState.PlayerMove){
            player.tick();
//            player.backgroundCollision(backGroundMask);
//            test2.interact(player);

//            TODO: REactivate
            if(currMap.inEventRect() && !currMap.getMapEvent().isDone()){
                Load.getEvent(currMap.getName()).init();
                GameStateManager.addState(Load.getEvent(currMap.getName()));
                return;
            }
//            System.out.println(currState.toString());
            nextMap = currMap.checkNextMap();
            if(nextMap != null){
//                currMap = (WorldMap)maps.get(nextMap);
                currState = OverWorldState.MapSwitch;
                alpha = 0.0f;
                alphaIncrement = 0.03f;
            }

            if(spawn && currMap.getAnEnemy() != null){
                GameStateManager.addStateTransition(new Battle(
                        player,currMap.getAnEnemy()),Color.green);
                spawnRate = 150;
                spawn = false;
            }
        }
        else if(currState == OverWorldState.MapSwitch){
            alpha += alphaIncrement;
            if(alpha >= 1.0f){
                currMap = Load.getMap(nextMap.getNextMap());
                player.setX(nextMap.getNewPlayerX());
                player.setY(nextMap.getNewPlayerY());
                alphaIncrement *= -1;
            }
            if(alpha <=0){
                currState = OverWorldState.PlayerMove;
                player.standStill();
            }
        }
        //clamping of all the indexes for the appropriate menu pages
        else if(currState == OverWorldState.Menu){
            if(menuState == MenuState.None){
                MenuIndex = Game.clamp(MenuIndex, 2, 0);
            }
            else if(menuState == MenuState.Buy){
                MenuIndex = Game.clamp(MenuIndex,1,0);
            }
            else if(menuState == MenuState.Items){
                MenuIndex = Game.clamp(MenuIndex,1,0);
            }
            else if(menuState == MenuState.Buy_Weapon){
                ItemIndex = Game.clamp(ItemIndex,buyableWeapons.size()-1,0);
            }
            else if(menuState == MenuState.Equip_Weapon){
                ItemIndex = Game.clamp(ItemIndex,player.getAllWeapons().size()-1,0);
            }
            else if(menuState == MenuState.Buy_Item){
                ItemIndex = Game.clamp(ItemIndex,buyableItems.size()-1,0);
            }
            else if(menuState == MenuState.Use_Item){
                //limits at 0 when there is no items
                ItemIndex = Game.clamp(ItemIndex,player.getItems().isEmpty()? 0:player.getItems().size()-1,0);
            }
        }
    }
    //random spawn chance
    public void randomSpawn(){
        spawnRate--;
        int chance = (int) (Math.random()*spawnRate);
        if(chance == 1)
            spawn = true;
    }

    public void setPlayerPosition(int x,int y){
        player.setX(x);
        player.setY(y);
    }

    public void setPlayerDirection(DIRECTION d){
        player.setDirection(d);
    }

    public void setPlayerStill(){
        player.standStill();
    }

    //When State is PlayerMove - keys move the player, or set off the movement for keys not pressed methods
    //When state is Menu and the keys have been released, it is scrolled through at the appropriate pages

    @Override
    public void keyLeftPressed() {
        if(currState == OverWorldState.PlayerMove){
            player.walkLeft();
            randomSpawn();
        }
    }

    @Override
    public void keyRightPressed() {

        if(currState == OverWorldState.PlayerMove){
            player.walkRight();
            randomSpawn();
        }
    }
    @Override
    public void keyUpPressed(){
        if(currState == OverWorldState.PlayerMove){
            player.walkUp();
            randomSpawn();
        }
        else if(currState == OverWorldState.Menu && !keysPressedDown){
            if(menuState == MenuState.None || menuState == MenuState.Buy || menuState == MenuState.Items){
                MenuIndex--;
            }
            else{
                ItemIndex--;
            }
            keysPressedDown = true;

        }
    }
    @Override
    public void keyDownPressed(){
        if(currState == OverWorldState.PlayerMove){
            player.walkDown();
            randomSpawn();
        }
        else if(currState == OverWorldState.Menu && !keysPressedDown){
            if(menuState == MenuState.None || menuState == MenuState.Buy || menuState == MenuState.Items){
                MenuIndex++;
            }
            else{
                ItemIndex++;
            }
        }
        keysPressedDown = true;
    }

    @Override
    public void noKeyLeftPressed() {
        if(currState == OverWorldState.PlayerMove) {
            player.noMoveLeft();
        }
    }

    @Override
    public void noKeyRightPressed() {
        if (currState == OverWorldState.PlayerMove) {
            player.noMoveRight();
        }
    }
    @Override
    public void noKeyUpPressed(){
        if(currState == OverWorldState.PlayerMove) {
            player.noMoveUp();
        }
    }
    @Override
    public void noKeyDownPressed(){
        if(currState == OverWorldState.PlayerMove) {
            player.noMoveDown();
        }
    }

    @Override
    public void keyZPressed() {
        //npc talks when touching them (close enough proximity to talk)
        if(currState == OverWorldState.PlayerMove) {
            for(NPC n:currMap.getNpcs()) {
                if (n.talks(player)) {
                    player.standStill();
                    currState = OverWorldState.NPCTalk;
                    currNPC = n;
                }
            }
        }
        //Menu pages flip to appropriate menue pages by changing menustates
        //Weapons and items are also bought/used/equipped depending on which one chosen on the appropriate menu pages
        else if(currState == OverWorldState.Menu && !keysPressedDown){
            if(menuState == MenuState.None){
                if(MenuIndex == BUY){
                    menuState = MenuState.Buy;
                }
                else if(MenuIndex == ITEMS){
                    menuState = MenuState.Items;
                    MenuIndex = 0;
                }
                else if(MenuIndex == QUIT){
                    System.exit(0);
                }
            }
            else if(menuState == MenuState.Buy){
                if(MenuIndex == BUY_WEAPON){
                    menuState = MenuState.Buy_Weapon;
                }
                else if(MenuIndex == BUY_ITEM){
                    menuState = MenuState.Buy_Item;
                }
            }
            else if(menuState == MenuState.Items){
                if(MenuIndex == EQUIP_WEAPON){
                    menuState = MenuState.Equip_Weapon;
                }
                else if(MenuIndex == USE_ITEM){
                    if(player.getItems().isEmpty()){    //no item in inventory prompt
                        GameStateManager.addState(noItems);
                    }
                    else{
                        menuState = MenuState.Use_Item;
                    }
                }
            }
            else if(menuState == MenuState.Buy_Weapon){
                if(player.canAfford(buyableWeapons.get(ItemIndex).getCost())){
                    player.buyWeapon(buyableWeapons.get(ItemIndex));
                    buyableWeapons.remove(player.getWeapon());
                }
                else{   //cant afford weapon but prompts dead end screen
                    TextBoxToState useSol = new TextBoxToState(player,StateType.Yes_No_TextBox,warning,false,new DeadEndScreen(player,StateType.DeadEndScreen));
                    GameStateManager.addState(useSol);
                }
            }
            else if(menuState == MenuState.Equip_Weapon){
                player.equipWeapon(player.getAllWeapons().get(ItemIndex));
            }
            else if(menuState == MenuState.Buy_Item){
                if(player.canAfford(buyableItems.get(ItemIndex).getCost())){
                    player.buyItem(buyableItems.get(ItemIndex));
                }
                else{//cant afford item but prompts dead end screen
                    TextBoxToState useSol = new TextBoxToState(player,StateType.Yes_No_TextBox,warning,false,new DeadEndScreen(player,StateType.DeadEndScreen));
                    GameStateManager.addState(useSol);
                }
            }
            else if(menuState == MenuState.Use_Item){
                if(player.getHp() == player.getMaxHp()){
                    GameStateManager.addState(hpIsFull);
                }
                else{
                    player.useItem(ItemIndex);
                }
            }
            keysPressedDown = true;
        }
    }

    @Override
    public void keyXPressed(){
        //flips back from current menu page to appropriate menu pages
        if(currState == OverWorldState.Menu && !keysPressedDown){
            if(menuState == MenuState.None){    //goes back to overworld state at front menu state
                currState = OverWorldState.PlayerMove;
            }
            else if(menuState == MenuState.Buy){
                MenuIndex = 0;
                menuState = MenuState.None;
            }
            else if(menuState == MenuState.Buy_Weapon || menuState == MenuState.Buy_Item){
                ItemIndex = 0;
                menuState = MenuState.Buy;
            }
            else if(menuState == MenuState.Items){
                MenuIndex = 1;
                menuState = MenuState.None;
            }
            else if(menuState == MenuState.Equip_Weapon || menuState == MenuState.Use_Item){
                MenuIndex = 0;
                menuState = MenuState.Items;
            }
            keysPressedDown = true;
        }
    }
    //used to make sure user has to let go of the key and press in order to scroll through menu options
    //(or else it'd go straight from the top to bottom option, vice versa
    @Override
    public void noKeysPressed(){keysPressedDown = false;}
    //Used to open the menu or close it instantly instead of having to flip back by pressing X
    @Override
    public void keyEnterPressed(){
        if(currState == OverWorldState.PlayerMove){
            currState = OverWorldState.Menu;
            //properties for menu are reset
            menuState = MenuState.None;
            MenuIndex = 0;
            ItemIndex = 0;
        }
        else if(currState == OverWorldState.Menu){
            currState = OverWorldState.PlayerMove;
        }
    }

}