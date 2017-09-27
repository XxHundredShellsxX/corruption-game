package bin.OverWorldElements;

import bin.*;
import bin.BattleElements.Battle;
import bin.BattleElements.Enemy;
import bin.Events.EventState;
import bin.Prompts.TextBoxToState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Each NPC is loaded from the map datafile, as well as
 * Enemies that randomly spawn. This class also load all the maps, masks and NPCs
 * from the text files
 */
public class WorldMap {
    private ArrayList<ConnectingMap> otherMaps;
    private ArrayList<NPC> npcs;
    private ArrayList<Enemy> mapEnemies;
    private EventState mapEvent;
    private Player player;
    private Image currMap;
    private BufferedImage currMask;
    private String name;
    private int width, height;
    private int mapX,mapY,playerX,playerY;
    private NPC invisibleWall;



    public WorldMap(String name, Player player){
        currMap = new ImageIcon("src\\resources\\Backgrounds\\"+name+".png").getImage();
        try {
            currMask = ImageIO.read(new File("src\\resources\\Backgrounds\\"+name+"Mask.png"));
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        width = currMap.getWidth(null);
        height = currMap.getHeight(null);
        this.player = player;
        this.name = name;
        mapEnemies = new ArrayList<>();
        npcs = new ArrayList<NPC>();
        otherMaps = new ArrayList<>();
        load(name);

    }

    //Loads Backround, NPCs
    private void load(String name) {
        // NPC images go UP, RIGHT, DOWN, LEFT
        try {
            Scanner reader = new Scanner(new BufferedReader(new FileReader(
                    "src\\resources\\Backgrounds\\"+name+".txt")));
            for(int i = Integer.parseInt(reader.nextLine()); i > 0; i--){
                npcs.add(parseNPC(reader.nextLine()));
            }
            for(int i = Integer.parseInt(reader.nextLine()); i > 0; i--){
                String enemyName = reader.nextLine();
                mapEnemies.add(Load.getEnemy(enemyName));
            }

//            System.out.println("doneNPC");
            /**
             * Other Maps data file format:
             * ~~~~~~~~~~~~~~~~~~~~~
             * OtherMapsNum
             * RectX, RectY, RectW, RectH, otherMap, newPlayerX, newPlayerY []
             * ~~~~~~~~~~~~~~~~~~~~~
             */
            int otherMapNum = Integer.parseInt(reader.nextLine());
            for(int i = otherMapNum; i > 0; i--){
                String[] properties = reader.nextLine().split(" ");
                otherMaps.add(new ConnectingMap(new Rectangle(Integer.parseInt(properties[0]), Integer.parseInt(properties[1]),
                        Integer.parseInt(properties[2]), Integer.parseInt(properties[3])), properties[4], Integer.parseInt(properties[5]),
                        Integer.parseInt(properties[6])));
            }

        }
        catch (FileNotFoundException e){
            System.out.println(e.getLocalizedMessage());
        }

        //Blockade for final boss room.
        if(name.equals("Castle")){
            for(NPC n : npcs){
                if(n.getX() == 915){
                    invisibleWall = n;
                    break;
                }
            }
        }
    }

    /**
     * NPC data file format:
     * ~~~~~~~~~~~~~~~~~~~~~
     * dialogueNum
     * dialogue[]
     * direction
     * x y height width
     * TYPE
     * [if type]
     *
     * ~~~~~~~~~~~~~~~~~~~~~
     * Makes an NPC from textFile
     */
    private NPC parseNPC(String name) throws FileNotFoundException{
        Scanner npcReader = new Scanner(new BufferedReader(new FileReader(
                "src\\resources\\NPCs\\"+name+"\\data.txt")));

        ArrayList<Image> npcImages = new ArrayList<Image>();
        for(int j = 0; j < 4; j++) {
            npcImages.add(new ImageIcon("src\\resources\\NPCs\\"+ name +"\\" + j + ".png").getImage());
        }
        TextBoxState npcDialogue = parseDialogue(npcReader);
        String dir = npcReader.nextLine();
        DIRECTION NPCdir;
        if (dir.equals("UP")){ NPCdir = DIRECTION.Up; }
        else if (dir.equals("RIGHT")){ NPCdir = DIRECTION.Right; }
        else if (dir.equals("LEFT")){ NPCdir = DIRECTION.Left; }
        else { NPCdir = DIRECTION.Down; }

        String NPCType = npcReader.nextLine();
        if(NPCType.equals("ITEM")){
            String[] coordinates = npcReader.nextLine().split(" ");
            return new NPC(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]),
                    Integer.parseInt(coordinates[3]), NPCdir, npcImages, npcDialogue, parseDialogue(npcReader),
                    Load.getItem(npcReader.nextLine()));
        }
        else if(NPCType.equals("ICE WIZARD")){
            return new NPC(npcReader.nextInt(), npcReader.nextInt(), npcReader.nextInt(),
                    npcReader.nextInt(), NPCdir, npcImages, new TextBoxToState(player,
                    StateType.TextBox, npcDialogue, false, new Battle(player, Load.getEnemy("Ice Wizard"))), new TextBoxState(player,
                    StateType.TextBox, new String[]{"It doesn't appear to move"}, Color.black, Color.WHITE, Color.black, 20, 550, 850, 100, 30));
        }

        else if(NPCType.equals("FIRE WIZARD")){
            return new NPC(npcReader.nextInt(), npcReader.nextInt(), npcReader.nextInt(),
                    npcReader.nextInt(), NPCdir, npcImages, new TextBoxToState(player,
                    StateType.TextBox, npcDialogue, false, new Battle(player, Load.getEnemy("Fire Wizard"))), new TextBoxState(player,
                    StateType.TextBox, new String[]{"It doesn't appear to move"}, Color.black, Color.WHITE, Color.black, 20, 550, 850, 100, 30));
        }

        return new NPC(npcReader.nextInt(), npcReader.nextInt(), npcReader.nextInt(),
                npcReader.nextInt(), NPCdir, npcImages, npcDialogue);


    }

    public TextBoxState parseDialogue(Scanner fileReader){
        int dialogueNum = Integer.parseInt(fileReader.nextLine());
        String[] dialogue = new String[dialogueNum];
        for(int j = 0; j < dialogueNum ; j++){
            dialogue[j] = fileReader.nextLine();
        }
        return new TextBoxState(player, StateType.TextBox, dialogue,
                Color.BLACK, Color.WHITE, Color.BLACK, 20, 550, 850, 100, 30);
    }

    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        if(width >= Game.WIDTH && height >= Game.HEIGHT) {
            mapX = -(player.getX() - Game.WIDTH / 2 + player.getWidth() / 2);
            mapY = -(player.getY() - Game.HEIGHT / 2 + player.getHeight() / 2);
            playerX = player.getX() + mapX;
            playerY = player.getY() + mapY;

            if (mapX > 0) {
                playerX -= mapX;
                mapX = 0;
            }
            if (mapY > 0) {
                playerY -= mapY;
                mapY = 0;
            }

            if (-mapX + Game.WIDTH >= width) {
                playerX += Game.WIDTH - (width + mapX);
                mapX = Game.WIDTH - width;
            }

            if (-mapY + Game.HEIGHT >= height) {
                playerY += Game.HEIGHT - (height + mapY);
                mapY = Game.HEIGHT - height;
            }
        }
        else {
            mapX = Game.WIDTH/2 - width/2 ;
            mapY = Game.HEIGHT/2 - height/2;
            playerX = player.getX() + mapX;
            playerY = player.getY() + mapY;
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        }

        g.drawImage(currMap, mapX, mapY, null);

        for (NPC n : npcs) {
            n.render((Graphics2D) g, n.getX() + mapX, n.getY() + mapY);
            n.interact(player);
        }



        player.backgroundCollision(currMask);
        player.render((Graphics2D) g, playerX, playerY);
        if(Load.wizardsKilled == 2 && invisibleWall != null){
            npcs.remove(invisibleWall);
        }
    }

    public ConnectingMap checkNextMap(){
        for(ConnectingMap m: otherMaps){
            if(m.getTravelRect().contains(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2))
                return m;
        }

        return null;
    }


    public void removeNPC(NPC removed){
        npcs.remove(removed);
    }
    public Image getCurrMap() {
        return currMap;
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    public int getMapX(){return mapX;}

    public int getMapY(){return mapY;}

    public int getPlayerX(){return playerX;}

    public int getPlayerY(){return playerY;}

    public void setMapEvent(EventState event){
        this.mapEvent = event;
    }

    public String getName(){
        return name;
    }

    public boolean inEventRect(){
        if(mapEvent != null){
            if(mapEvent.getEventRect().contains(player.getX() + player.getWidth()/2, player.getY() + player.getHeight()/2))
                return true;
        }
        return false;
    }

    public Enemy getAnEnemy(){
        return (!mapEnemies.isEmpty())?mapEnemies.get((int)(Math.random()*mapEnemies.size())): null;
    }

    public EventState getMapEvent(){
        return mapEvent;
    }
}
