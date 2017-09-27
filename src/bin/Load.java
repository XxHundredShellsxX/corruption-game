package bin;

import bin.BattleElements.Attacks.*;
import bin.BattleElements.Enemy;
import bin.BattleElements.Item;
import bin.BattleElements.Weapon;
import bin.Events.*;
import bin.OverWorldElements.WorldMap;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Loads all the basic Maps, Weapons, Items, Events, and enemies, so that they may be accessed from
 * anywhere in the program.
 */
public final class Load {
    // Stored in hashmaps, so that objects can be retrieves with just their name (easier with file IO)
    private static HashMap<String, WorldMap> maps;
    private static HashMap<String, Weapon> weapons;
    private static HashMap<String, Item> items;
    private static HashMap<String, EventState> events;
    private static HashMap<String, Enemy> enemies;
    public static HashMap<String, Attack> possibleAttacks;
    public static int wizardsKilled = 0;


    // called in the beginning to populate maps
    public static void init(Player player){
        loadAttacks();
        loadItems();
        loadEnemies(player);
        loadMaps(player);
        loadWeapons();
        loadEvents(player);
    }

    //The map loads its own components with just its name
    public static void loadMaps(Player player){
        maps = new HashMap<String, WorldMap>();
        try {
            Scanner mapReader = new Scanner(new BufferedReader(new FileReader("src\\resources\\Backgrounds\\Maps.txt")));
            for(int i = mapReader.nextInt(); i > 0; i--){
                String name = mapReader.next();
                maps.put(name, new WorldMap(name, player));
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }

    //Events are manually loaded and assigned to a map
    public static void loadEvents(Player player){
        events = new HashMap<>();
        events.put("Forest", new E1_Knight_Attack(player));
        events.put("Town2", new E2_Enter_House_Warning(player));
        events.put("House24", new E3_Knight_Bullied(player));
        events.put("OutsideCastle", new E4_Beast_Attack(player));
        events.put("Castle", new E5_Royal_Attack(player));
        events.put("Boss", new E6_Final_Boss(player));
        for(String map:events.keySet()){
            maps.get(map).setMapEvent(events.get(map));
        }

    }

    //Items are loaded from a TextFile, containing all the critical Info
    public static void loadItems(){
        items = new HashMap<>();
        try {
            Scanner itemReader = new Scanner(new BufferedReader(new FileReader("src\\resources\\TextFiles\\Items.txt")));
            int numItems = Integer.valueOf(itemReader.nextLine());
            for(int i = 0; i < numItems; i++){
                String[] properties = itemReader.nextLine().split(",");
                String name = properties[0];
                int hpRestoration = Integer.valueOf(properties[1]);
                int cost = Integer.valueOf(properties[2]);
                String description = properties[3];
                items.put(name, (new Item(name,hpRestoration,cost,description)));
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }

    //Weapons are loaded from a TextFile, containing all the critical Info
    public static void loadWeapons(){
        weapons = new HashMap<>();
        try {
            Scanner weaponReader = new Scanner(new BufferedReader(new FileReader("src\\resources\\TextFiles\\Weapons.txt")));
            int numWeapons = Integer.valueOf(weaponReader.nextLine());
            for(int i = 0; i < numWeapons; i++){
                String[] properties = weaponReader.nextLine().split(",");
                String name = properties[0];
                String description = properties[1];
                int cost = Integer.valueOf(properties[2]);
                int strength = Integer.valueOf(properties[3]);
                int numHits = Integer.valueOf(properties[4]);
                int speed = Integer.valueOf(properties[5]);
                int hitInterval = Integer.valueOf(properties[6]);
                int type = Integer.valueOf(properties[7]);
                int spriteType = Integer.valueOf(properties[8]);
                weapons.put(name, (new Weapon(name,description,cost,strength,numHits,speed,hitInterval,type,spriteType)));
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }

    public static void loadAttacks(){
        possibleAttacks = new HashMap<>();
        String[] attackNames = new String[]{"Big Bounce","Blizzard","Bounce","Breaking Ice","Easy Feather Fall",
                                            "Easy Slime Bounce", "Easy Slime Rain", "Easy Wing Slash", "Hard Feather Fall",
                                            "Hard Slime Bounce", "Hard Slime Rain", "Hard Wing Slash", "Juggling Balls",
                                            "Lava Evade", "Quick Slash", "Rotating Pillars", "Shield Bash", "Shoot Player",
                                            "Sinusoidal Fire", "Smooth Fire Attack", "Swirling Agony", "Sword Rain","Shooting Star"};
        Attack[] attacks = new Attack[]{new BigBounce(), new Blizzard(), new Bounce(), new BreakingIce(), new EasyFeatherFall(),
                                        new EasySlimeBounce(), new EasySlimeRain(), new EasyWingSlash(), new HardFeatherFall(),
                                        new HardSlimeBounce(), new HardSlimeRain(), new HardWingSlash(), new JugglingBalls(),
                                        new LavaEvade(), new QuickSlash(), new RotatingPillars(), new SheildBash(), new ShootPlayer(),
                                        new SinusoidalFire(), new SmoothFireAttack(), new SwirlingAgony(), new SwordRain(),new ShootingStar()};
        for(int i = 0; i < attackNames.length; i++){
            possibleAttacks.put(attackNames[i],attacks[i]);
        }
    }

    //Enemies are loaded manually, and they load their own sprites/attack sprites.
    public static void loadEnemies(Player player){
        enemies = new HashMap<>();
        String[] allEnemyNames = new String[]{"Bird", "Boss", "Corrupted Knight", "Dinosaur", "Fire Wizard",
                                                "Guard Knight", "Ice Wizard", "King", "Knight", "Slime"};
        /** Text File Format Goes
         * Name
         * Hp
         * Boss or not
         * Attack #
         * Name of Attacks
         * Dialogue Lines
         * Actual Dialogue
         */
        try {

            for (String enemyName : allEnemyNames) {
                Scanner enemyReader = new Scanner(new BufferedReader(new FileReader(
                        "src\\resources\\Enemies\\" + enemyName + "\\data.txt")));
                int types = Integer.parseInt(enemyReader.nextLine());
                for(int type = 0; type < types;type++) {
                    String name = enemyReader.nextLine();
                    int hp = Integer.parseInt(enemyReader.nextLine());
                    boolean isBoss = enemyReader.nextLine().equals("BOSS");
                    ArrayList<Attack> enemyAttacks = new ArrayList<>();
                    int numAttacks = Integer.parseInt(enemyReader.nextLine());
                    for (int i = 0; i < numAttacks; i++) {
                        String attackname = enemyReader.nextLine();
                        enemyAttacks.add(possibleAttacks.get(attackname));
                    }

                    int dialogueNum = Integer.parseInt(enemyReader.nextLine());
                    String[] dialogue = new String[dialogueNum];
                    for (int j = 0; j < dialogueNum; j++) {
                        dialogue[j] = enemyReader.nextLine();
                    }
                    TextBoxState enemyDialogue = new TextBoxState(player, StateType.TextBox, dialogue,
                            Color.white, Color.black, Color.white, 25, 400, 550, 80, 22);
                    Image sprite = new ImageIcon("src\\resources\\Enemies\\" + enemyName + "\\sprite.png").getImage();
                    enemies.put(name, new Enemy(name, hp, isBoss, enemyAttacks, enemyDialogue, sprite));
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static WorldMap getMap(String mapName){
        return maps.get(mapName);
    }

    public static Item getItem(String itemName) {
        return items.get(itemName);
    }

    public static Weapon getWeapon(String weaponName) {
        return weapons.get(weaponName);
    }

    public static EventState getEvent(String mapName){
        return events.get(mapName);
    }

    public static ArrayList<Item> getBuyableItems(){
        ArrayList<Item>itemsList = new ArrayList<>();
        itemsList.addAll(items.values());
        return itemsList;
    }

    public static ArrayList<Weapon> getBuyableWeapons(){
        ArrayList<Weapon>itemsList = new ArrayList<>();
        itemsList.addAll(weapons.values());
        itemsList.remove(weapons.get("???")); //Special Item given later on
        return itemsList;
    }

    public static Enemy getEnemy(String enemyName){
        return enemies.get(enemyName);
    }

}
