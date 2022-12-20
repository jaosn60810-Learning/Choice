package gameObjects;

import camera.MapInformation;
import controllers.SceneController;

import static scenes.PlayScene.*;

import scenes.PlayScene;
import utils.*;

import static utils.Global.*;

import utils.MonsterAnimator.MonsterType;
import scenes.PlayScene.*;

import java.awt.*;
import java.util.ArrayList;

public class Monster extends GameObject {

    public enum State { // 怪獸狀態
        NORMAL, // 正常
        ATTACK; // 攻擊
    }

    private MonsterAnimator monsterAnimator;
    private Direction dir;
    private MonsterType monsterType;
    private Vector vector;
    private boolean moveReverse;
    private boolean remove;
    private Delay delay;
    private int count;
    private State state;
    private Image imgB;
    //花
    private Delay delayPiranha;
    private int[] arr;

    private int ySpeed; // 重力加速度
    private boolean canMove;

    private ArrayList<Player> players;//玩家


    public Monster(int x, int y, MonsterType monsterType, Vector vector, Delay delay) {
        super(x, y, 20, 20);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.delay = delay;
        this.delay.loop();
        this.delayPiranha = new Delay(32);
        this.delayPiranha.loop();
        this.dir = Direction.RIGHT;
        this.ySpeed = 0;
    }

    public Monster(int x, int y, int width, int height, MonsterType monsterType, Vector vector, Delay delay) {
        super(x, y, width, height);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.count = 0;
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.delay = delay;
        this.delay.loop();
        this.delayPiranha = new Delay(32);
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.arr = new int[]{0, 1, 2, 3, 4, 3, 2, 1}; // 噴火用
        this.delayPiranha.loop();
        this.dir = Direction.RIGHT;
        this.ySpeed = 0;
    }

    public Monster(int x, int y, int width, int height, MonsterType monsterType, Vector vector, Delay delay, ArrayList<Player> players) {
        super(x, y, width, height);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.count = 0;
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.delay = delay;
        this.delay.loop();
        this.delayPiranha = new Delay(32);
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.arr = new int[]{0, 1, 2, 3, 4, 3, 2, 1}; // 噴火用
        this.delayPiranha.loop();
        this.dir = Direction.RIGHT;
        this.ySpeed = 0;

        this.players = players;
    }

    public Monster(int x, int y, int width, int height, MonsterType monsterType, Vector vector, Delay delay, Direction dir) {
        super(x, y, width, height);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.delay = delay;
        this.delay.loop();
        this.delayPiranha = new Delay(32);
        this.delayPiranha.loop();
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.dir = dir;
        this.state = State.NORMAL;        // 怪物的攻擊狀態
        imgB = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterShootExplode()); // 怪物的攻擊爆炸
        this.ySpeed = 0;
    }

    public Monster(int x, int y, MonsterType monsterType, Vector vector) {
        super(x, y, 20, 20);  // (x, y, 寬, 高)
        this.monsterType = monsterType;
        this.monsterAnimator = new MonsterAnimator(monsterType);
        this.vector = vector;
        this.moveReverse = false;
        this.remove = false;
        this.dir = Direction.RIGHT;
        this.ySpeed = 0;
    }

    public MonsterType getMonsterType() {
        return this.monsterType;
    }

    public boolean getRemove() {
        return this.remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public void setState(State state) {         // 設定狀態
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public int getYSpeed() {
        return this.ySpeed;
    }

    public void setYSpeed(int y) {
        this.ySpeed = y;
    }

    public void fall() {//重力往下掉
        translateY(ySpeed);
    }

    public boolean getCanMove() {
        return this.canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public Direction getDirection() {
        return this.dir;
    }


    @Override
    public void paintComponent(Graphics g) {
        if (monsterType == MonsterType.MONSTER_SHOOT && this.state == State.ATTACK) {
            g.drawImage(imgB, painter().left() - painter().width() / 2 , painter().top() - painter().height() / 2 , painter().width() * 2,
                    painter().height() * 2, null);
        } else {
            monsterAnimator.paint(monsterType, dir, painter().left(), painter().top(),
                    painter().right(), painter().bottom(), g);
        }
    }

    @Override
    public void update() {

        // 移動的怪
        if (monsterType == MonsterType.BAT_UD || monsterType == MonsterType.BAT_LR
                || monsterType == MonsterType.DEMON_UD || monsterType == MonsterType.DEMON_LR
                || monsterType == MonsterType.DRAGON_UD || monsterType == MonsterType.DRAGON_LR
                || monsterType == MonsterType.GHOST_UD || monsterType == MonsterType.GHOST_LR
                || monsterType == MonsterType.GOBLIN_UD ||monsterType == MonsterType.GOBLIN_LR
                || monsterType == MonsterType.GREENELF_UD || monsterType == MonsterType.GREENELF_LR
                || monsterType == MonsterType.MINOTAUR_UD ||monsterType == MonsterType.MINOTAUR_LR
                || monsterType == MonsterType.PINKELF_UD || monsterType == MonsterType.PINKELF_LR
                || monsterType == MonsterType.SKELETON_UD ||monsterType == MonsterType.SKELETON_LR
                || monsterType == MonsterType.SLIME_UD || monsterType == MonsterType.SLIME_LR) {
            // 撞到左邊邊界向右走
            if (touchLeft()) {
                changeLeft(MapInformation.mapInfo().left());
                moveReverse = true;
            }
            // 撞到右邊邊界向左走
            if (touchRight()) {
                changeRight(MapInformation.mapInfo().right());
                moveReverse = false;
            }
            // 向右走
            if (moveReverse) {
                this.vector = new Vector(Math.abs(this.vector.vx()), -Math.abs(this.vector.vy()));
                this.dir = Direction.RIGHT;
            }
            // 向左走
            else {
                this.vector = new Vector(-Math.abs(this.vector.vx()), Math.abs(this.vector.vy()));
                this.dir = Direction.LEFT;
            }
            // 延遲時間到轉向
            if (delay.count()) {
                if (moveReverse) {
                    moveReverse = false;
                } else {
                    moveReverse = true;
                }
            }
            // X 軸移動速度
            translateX((int) this.vector.vx());
            // Y 軸移動速度
            translateY((int) this.vector.vy());
            // 怪獸動畫
            monsterAnimator.update();
        }

        /**天火*/
        if (monsterType == MonsterType.FIRELINE) {
            this.dir = Direction.DOWN;//方向：往下走
            if (players.get(0).getPlayerLiveTime() >= /*5.0*/-1) { // 玩家重生後時間大於5秒鐘
                    translateY((int) this.vector.vy()); // 天火開始移動 (Y軸移動速度)
            }
            monsterAnimator.update(); // 怪獸動畫 (就是取圖片跟閃爍的意思)
        }

        /**地水*/
        if (monsterType == MonsterType.WATERLINE) {
            this.dir = Direction.JUMP;
            if (players.get(0).getPlayerLiveTime() >= /*5.0*/-1) { // 玩家重生後時間大於5秒鐘
                translateY((int) this.vector.vy()*-1); // 天火開始移動 (Y軸移動速度)
            }
            monsterAnimator.update();// 怪獸動畫(就是取圖片跟閃爍的意思)
        }

        if (monsterType == MonsterType.MONSTER_SHOOT) {    // 怪物的攻擊的更新
            if (state == State.NORMAL) {    // 正常狀態
                translateX((int) vector.vx());
                translateY((int) vector.vy());
            } else if (state == State.ATTACK) {    // 攻擊狀態
                delay.play();
            }
            if (delay.count()) {
                remove = true;
            }
            monsterAnimator.update();
        }
    }
}
