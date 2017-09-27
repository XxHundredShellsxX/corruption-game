package bin.BattleElements.Attacks;
import bin.BattleElements.BattleObjects.BattleObject;
import bin.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * This is an abstract class, which handels some of the basic functions of all
 * attacks such as rendering and moving all the Battle Objects, adding and removing
 * objects to an arrayList, ETC.
 */

public abstract class Attack {
    protected ArrayList<BattleObject> objects;
    protected int attackLength, stopTime, startTime, damage;
    protected Image attackImage;

    public Attack(int attackLength, int damage){
        this.objects = new ArrayList<BattleObject>();
        this.attackLength = attackLength;
        this.damage = damage;
        this.stopTime = 0;
        this.startTime = 0;
    }

    public void addObject(BattleObject t){
        objects.add(t);
    }

    public void removeObject(BattleObject t){
        objects.remove(t);
    }

    public void tick(Rectangle box){
        for(BattleObject b : objects){
            b.tick(box);
        }
    }

    public void render(Graphics g){
        for(BattleObject b : objects){
            b.render(g);
        }
    }

    //Keep trac of time(frames) since the battle has begin, so that it knows when to stop
    public void start(){
        startTime = Game.getCount();
        stopTime = startTime + attackLength;
    }

    //Stops when time is up
    public boolean isFinished(){
        return (Game.getCount() > stopTime);
    }

    public ArrayList<BattleObject> getObjects(){
        return objects;
    }


    public void reset(){
        objects.clear();
    }

    public int getDamage(){
        return damage;
    }

}
