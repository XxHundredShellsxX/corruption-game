package bin.Events;

import bin.*;
import bin.OverWorldElements.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Sajid on 2016-06-14.
 */
//Base abstract Event Class which handles the baseline for "cutscenes" which are triggered all throughout multiple points
//of the game
public abstract class EventState extends GameState{

    //takes in crucial coordinates to draw scene
    protected int mapX,mapY,playerX,playerY;
    protected Rectangle eventRect;
    //background put into scene
    protected Image background;
    //the text that are said and images used
    protected ArrayList<TextBoxState> textBoxes;
    protected ArrayList<Image> images;
    protected WorldMap map;
    protected boolean isDone;

    //additionally takes in the name of the event to load all of its properties
    public EventState(Player player, String name){
        super(player,StateType.Event);
        textBoxes = new ArrayList<>();
        images = new ArrayList<>();
        try{
            loadEvent(name);
        }
        catch (FileNotFoundException e){
            System.out.println(e.getLocalizedMessage());
        }
        isDone = false;

    }
    //initializes the scene according to map
    public void init(){
        mapX = map.getMapX();
        mapY = map.getMapY();
        playerX = map.getPlayerX();
        playerY = map.getPlayerY();
        background = map.getCurrMap();
    }


    public void render(Graphics g){}

    public void tick(){}

    //convenient methods to render the player and background
    public void renderPlayer(Graphics2D g){
        player.render(g,playerX,playerY);
    }

    public void renderBackGround(Graphics2D g){
        g.drawImage(background,mapX,mapY,null);
    }

    //loads the textbox dialogues and images according to folder with textfile containing the dialogue
    //and images used in the event
    public void loadEvent(String eventName) throws FileNotFoundException {
        Scanner eventReader = new Scanner(new BufferedReader(new FileReader(
                "src\\resources\\Events\\" + eventName + "\\data.txt")));
        map = Load.getMap(eventReader.nextLine());
        String[] rectData = eventReader.nextLine().split(" ");
        eventRect = new Rectangle(Integer.parseInt(rectData[0]), Integer.parseInt(rectData[1]),
                Integer.parseInt(rectData[2]), Integer.parseInt(rectData[3]));
        int numTextBoxes = Integer.parseInt(eventReader.nextLine());
        for (int i = 0; i < numTextBoxes; i++) {
            int dialogueNum = Integer.parseInt(eventReader.nextLine());
            String[] dialogue = new String[dialogueNum];
            for (int j = 0; j < dialogueNum; j++) {
                dialogue[j] = eventReader.nextLine();
            }
            textBoxes.add(new TextBoxState(player, StateType.TextBox, dialogue, Color.black, Color.white,
                    Color.BLACK, 20, 550, 850, 100, 30));
        }
        int numImages = Integer.parseInt(eventReader.nextLine());
        for (int i = 1; i <= numImages; i++) {
            images.add(new ImageIcon("src\\resources\\Events\\" + eventName + "\\" + i + ".png").getImage());
        }
    }

    public Rectangle getEventRect(){
        return eventRect;
    }

    public boolean isDone(){
        return isDone;
    }

    public void setIsDone(Boolean isDone){
        this.isDone = isDone;
    }

}
