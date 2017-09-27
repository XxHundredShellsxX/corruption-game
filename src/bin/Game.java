package bin;

import bin.BattleElements.Battle;
import bin.BattleElements.Weapon;
import bin.Events.*;
import bin.OverWorldElements.*;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;

/**
 * USES INTELLIJ
 *
 * OUR GAME IS AN RPG TYPE GAME WITH A STORY WHERE YOU CAN INTERACT WITH NPCS, ENVIRONENTS,SCROLL THROUGH MAPS,
 * ETC, YOU CAN BUY WEAPONS, ITEMS, etc
 */

public class Game extends JFrame implements ActionListener{

    private Timer timer;
    private GameStateManager stateManager;  //manages game "states"
    private static int counter;     //counter used for timing in attacks

    public static final int WIDTH = 900, HEIGHT = 700, SMALL_WIDTH = 600, SMALL_HEIGHT  = 600;

    private Player player;
    private GameState s2;

    public Game(){

        super("...");
        counter = 0;
        stateManager = new GameStateManager(this);
        setSize(WIDTH,HEIGHT);
        timer = new Timer(15,this);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player = new Player(600,350,72,40, bin.OverWorldElements.DIRECTION.Down, 60, 16000,null);
        Load.init(player); //initializes properties for game
        s2 = new OverWorld(player,StateType.InGame);
        GameStateManager.addState(s2);
        GameStateManager.addState(new TitleScreen(player,StateType.DeadEndScreen));
        add(stateManager);


        setVisible(true);
    }

    //state manager handles all drawing and updates
    @Override
    public void actionPerformed(ActionEvent evt){
        counter++;
        stateManager.repaint();
        stateManager.tick();
    }

    public void loadSave(){
        try {
            Scanner inFile = new Scanner(new BufferedReader(new FileReader("src\\bin\\Save.txt")));
        }
        catch (FileNotFoundException f){
            System.out.println(f.toString());
        }
    }

    public static int getCount(){
        return counter;
    }

    //starts timer of game
    public void start(){
        timer.start();
    }

    //Game is created
    public static void main(String[] args) {
        Game m = new Game();
    }

    //universal clamping method to limit integer variables
    //ex. x and y coordinate, indexes for scrolling through menus
    public static int clamp(int var, int max, int min){
        if(var < min){
            return min;
        }
        else if(var > max){
            return max;
        }
        else{
            return var;
        }
    }


}