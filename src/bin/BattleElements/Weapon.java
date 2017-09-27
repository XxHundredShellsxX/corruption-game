package bin.BattleElements;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sajid on 2016-05-12.
 */
//basic weapon class, with common properties like its attack sprites, damage, cost, etc
public class Weapon {

    private String name;
    private String description;
    private int cost;
    private int speed;
    private int numHits;
    private int strength;
    private int hitInterval;
    private int type;
    private ArrayList<Image> weaponAttackSprites;
    private int animationDelay;
    //determines what type of attack sprite is used
    private final int BONK = 0, LIGHTNING = 1, MAGIC_SWORD = 2,DARK_SWORD = 3;

    public Weapon(String name, String description,int cost,int strength, int numHits, int spd, int hitInterval, int type,int spriteType){
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.strength = strength;
        this.numHits = numHits;
        speed = spd;
        this.hitInterval = hitInterval;
        this.type = type;
        weaponAttackSprites = new ArrayList<>();
        //loads and sets attack sprites based on sprite type
        if(spriteType == BONK){
            for(int i = 1; i < 3; i++){
                weaponAttackSprites.add(new ImageIcon("src\\resources\\Attacks\\iron_head" + String.valueOf(i)+".png").getImage());
            }
            animationDelay = 6;
        }
        else if(spriteType == LIGHTNING){
            for(int i = 0; i < 8; i++){
                weaponAttackSprites.add(new ImageIcon("src\\resources\\Attacks\\lightning" + String.valueOf(i)+".png").getImage());
            }
            animationDelay = 6;
        }
        else if(spriteType == MAGIC_SWORD){
            for(int i = 0; i < 38; i++){
                weaponAttackSprites.add(new ImageIcon("src\\resources\\Attacks\\Odyssey\\" + String.valueOf(i)+".png").getImage());
            }
            animationDelay = 2;
        }
        else if(spriteType == DARK_SWORD){
            for(int i = 0; i < 14; i++){
                weaponAttackSprites.add(new ImageIcon("src\\resources\\Attacks\\Dark Sword\\" + String.valueOf(i)+".png").getImage());
            }
            animationDelay = 5;
        }

    }

    public String getName(){
        return name;
    }

    public String getDescription(){return  description;}

    public int getCost(){return cost;}

    public int getSpeed(){return speed;}

    public int getNumHits(){return numHits;}

    public int getStrength(){return strength;}

    public int getHitInterval(){return hitInterval;}

    public int getType(){return type;}

    public ArrayList<Image> attackSprites(){
        return weaponAttackSprites;
    }

    public int getAnimationDelay(){ return animationDelay;}


}
