package bin.BattleElements;

import java.awt.*;

import bin.*;
import bin.BattleElements.Attacks.Attack;
import bin.BattleElements.BattleObjects.BattleObject;
import bin.BattleElements.BattleObjects.PlayerHeart;

public class FoeAttack extends BattleState {
    private Rectangle box;
    private Attack attack;
    private PlayerHeart heart;

//    public FoeAttack(Player player, Attack attack, KeyInput keys){
//        super(player, null, BattleChoice.NONE, StateType.FoeAttack, keys);
//        box = new Rectangle(50, 300, 500, 200);
//        this.attack = attack;
//    }

    public FoeAttack(Player player, Enemy enemy, int round) {
        super(player, enemy, StateType.FoeAttack);
        box = new Rectangle(50, 250, 500, 200);
        this.attack = enemy.getAttack(round);
        heart = player.getHeart();
        attack.start();
    }

    public void addAttack(Attack a) {
        attack = a;
    }


    public void checkCollision(){
        if(!heart.isHit()) {
            for (BattleObject b : attack.getObjects()) {
                if (heart.getHitBox().intersects(b.getCollisionRect()) && !b.getIsDead()) {
                    player.loseHp(attack.getDamage());
//                    System.out.println(player.getHp());
                    heart.hit();
                    return;
                }
            }
        }
    }

    public void tick() {

        if(attack.isFinished()) {
            attack.reset();
            heart.reset();
            GameStateManager.removeState(this);
        }
        heart.tick(box);
        attack.tick(box);
        checkCollision();
        if(player.getHp() <= 0){
            GameStateManager.addState(new GameOverScreen(player, StateType.DeadEndScreen));
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Battle.drawBasicBattle(g, player);
        enemy.render(g2);

        //renders text
        RenderingHints rh =
                new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHints(rh);
        g2.setColor(Color.BLACK);
        g2.fill(box);
        g2.setColor(Color.white);
        g2.draw(box);
        heart.render(g);
        attack.render(g);
    }

    public void changeRect(int x, int y, int width, int height) {
        box.setRect(x, y, width, height);
    }

    @Override
    public void keyUpPressed() {
        heart.setGoingUp(true);
    }

    @Override
    public void keyDownPressed() {
        heart.setGoingDown(true);
    }

    @Override
    public void keyLeftPressed() {
        heart.setGoingLeft(true);
    }

    @Override
    public void keyRightPressed() {
        heart.setGoingRight(true);
    }

    @Override
    public void noKeyUpPressed() {
        heart.setGoingUp(false);
    }

    @Override
    public void noKeyDownPressed() {
        heart.setGoingDown(false);
    }

    @Override
    public void noKeyLeftPressed() {
        heart.setGoingLeft(false);
    }

    @Override
    public void noKeyRightPressed() {
        heart.setGoingRight(false);
    }

    @Override
    public String toString() {
        return "FoeAttack";
    }
}
