package bin.BattleElements;

import bin.BattleElements.Attacks.Attack;
import bin.Game;
import bin.TextBoxState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Fahim on 2016-05-04.
 */
public class Enemy  {
    protected String name;
    protected double hp;
    protected double maxHp;

    protected ArrayList<Attack> attacks;
    private TextBoxState dialogue;
    private Image sprite;
    private boolean isHit;
    private boolean rumbleRight = true;
    private boolean losingHp ;
    private boolean doneTakingDamage;
    private final int maxHitTime = 20;
    private int hitTime;
    private int hpLost;
    private double prevHp;
    private boolean damageFade;
    private float alpha;
    private int HP_DEPLETION_INCREMENT;
    private boolean boss;
//    protected Player player;

    public Enemy(String name, double hp, boolean boss, ArrayList<Attack> attacks,TextBoxState dialogue,Image sprite){
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        HP_DEPLETION_INCREMENT = (int)(hp/20);
        this.sprite = sprite;
        this.attacks = attacks;
        this.dialogue = dialogue;
        System.out.println(name);
        hitTime = maxHitTime;
        losingHp = false;
        doneTakingDamage = false;
        prevHp = hp;
        damageFade = false;
        alpha = 1.00f;
        this.boss = boss;
    }

    public void render(Graphics2D g){
//        int w = sprite.getWidth(null)*3;
//        int h = sprite.getHeight(null)*3;
        int x = Game.SMALL_WIDTH/2 - (sprite.getWidth(null)/2);
        int y = Game.SMALL_HEIGHT/3 - (sprite.getHeight(null)/2);
        displayHp(g, x + sprite.getWidth(null)/2, y + sprite.getHeight(null) + 10);
        if((isHit || losingHp) && damageFade){
            //set the opacity
            g.setComposite(makeTransparent(alpha));
            //do the drawing here
            //increase the transparency and repaint
            alpha -= 0.02f;
            if(alpha <= 0.0f){
                alpha = 0.0f;//when invisible, its done
                damageFade = false;
            }
            //shows the amount of hp enemy lost from the attack
            drawText(g, "Berlin Sans FB Demi", String.valueOf(hpLost),x + 10,y - 30,30,Color.red);
            g.setComposite(makeTransparent(1));

        }
        if(isHit){  //is Hit triggers the enemy rumbling as a "getting hit effect"
            hitTime--;
            if(hitTime%4 == 0){
                rumbleRight = !rumbleRight;
                x += rumbleRight? 4: -4;
            }
            if(hitTime == 0){   //after enemy is done getting hit, the losing hp flag is triggered
                losingHp = true;
                isHit = false;
            }
        }
        if(losingHp){   //the enemy loses hp in 20 increments unless losing 20 hp is over the amount of hp its supposed
            //to lose in which its hp is set to how much its supposed to lose
            if(hp - HP_DEPLETION_INCREMENT < prevHp - hpLost){
                hp = prevHp - hpLost;
            }
            else{
                hp -= HP_DEPLETION_INCREMENT;
            }
            //when the hp is done trailing to how much its supposed to lose and the damage taken fade is done, enemy
            //getting hit is over
            if(hp == prevHp - hpLost || hp <= 0){
                losingHp = false;
                alpha = 1.00f;
                doneTakingDamage = true;
            }
        }
        g.drawImage(sprite,x ,y ,null);
    }

    public AlphaComposite makeTransparent(float alpha){
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type,alpha));
    }

    public void drawText(Graphics2D g,String font, String text, int x ,int y, int size, Color color ){
        g.setColor(color);
        g.setFont(new Font(font, Font.PLAIN,size));
        g.drawString(text,x,y);
    }

    public void takesDamage(int damageTaken){
        isHit = true;
        damageFade = true;
        hitTime = maxHitTime;
        hpLost = damageTaken;
        prevHp = hp;
    }

    public boolean isBoss(){return boss;}

    public boolean isHit(){
        return isHit;
    }

    public boolean isDoneTakingDamage(){
        return doneTakingDamage;
    }

    public void resetDamageDoneTaken(){
        doneTakingDamage = false;
    }

    public boolean isLosingHp(){return losingHp;}

    public void displayHp(Graphics2D g,int x,int y){
        g.setColor(Color.white);
        g.fillRect(x - 104,y - 4 , 208, 38);
        g.setColor(Color.green);
        g.fillRect(x - 100,y , (int)(((double)(hp/maxHp)) * 200), 30);

    }

    public TextBoxState getDialogue(){return dialogue;}

    public String toString(){
        return name;
    }

    public void setHp(int hp){
        this.hp = hp;
    }

    public int getHp(){
        return (int)hp;
    }

    public void loseHp(int dmg){
        hp -= dmg;
    }

    public int getMaxHp(){return (int)maxHp;}


//    public abstract void drawImage(Graphics g);
    public Attack getAttack(int round){
        return attacks.get(round%attacks.size());
    }

    public void reset(){
        losingHp = false;
        doneTakingDamage = false;
        prevHp = hp;
        damageFade = false;
        alpha = 1.00f;
        this.hp = maxHp;
    }

}
