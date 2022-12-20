package gameObjects;

import camera.MapInformation;
import controllers.AudioResourceController;
import controllers.SceneController;
import utils.*;
import utils.PlayerAnimator;

import java.awt.*;

import static utils.Global.*;

public class Player extends GameObject implements CommandSolver.KeyListener {

    private PlayerAnimator animator; // 動畫
    private Direction dir; // 方向
    private boolean canMove;
    private boolean isJumping; // 跳躍中
    private boolean isSquating; // 蹲下中

    private int[] rebornPoint; // 重生點{x,y}
    private Delay deathDelay; // 死亡的delay

    public void setPlayerLiveTime(double playerLiveTime) {
        this.playerLiveTime = playerLiveTime;
    }

    public double getPlayerLiveTime() {
        return playerLiveTime;
    }

    private double playerLiveTime; // 角色的每次復活時間 (玩家每次復活都重算)

    // 死亡鏡頭調控
    public boolean isHaveBeenDead() {
        return haveBeenDead;
    }

    private boolean haveBeenDead = false; // true為已經死過至少一次

    //記分區
    private int endingPoint; // 決定結局的分數
    private int heartCount; // 獲得生命/愛心數
    private int monsterCount; // 擊殺怪物數
    private int eventCount; // 經歷過幾次事件
    private int deathCount; // 死亡次數

    //速度區
    private Vector Speed; // 移動速度
    private int ySpeed; // 重力
    private boolean[] pressed = {false, false}; // 用按鍵判斷給予的向量
    private int brickSpeed; // 有速度的方塊給予的速度

    //技能區
    private boolean canBeEffect; // 可得到狀態
    private Delay effectDelay; // 狀態持續的時間
    private boolean invincible; // 無敵狀態
    private boolean paint; // 是否閃爍
    private Delay paintDelay; // 無敵閃爍
    private boolean eatFood; // 是否吃到食物的提示
    private int extraSpeed; // 給予額外速度
    private int downSpeed; // 減速

    // Player 攻擊
    private boolean canShot;
    private Delay playerShootDelay;

    //ID (用來確認模式)
    private int Id;

    //effect
    private Image imgInvincible;
    private Image imgSpeedUp;
    private Image imgSpeedDown;

    public Player(int x, int y) {
        this(PlayerAnimator.ActorType.PLAYER_TWO, x, y);
    }

    public Player(PlayerAnimator.ActorType actorType, int x, int y) {
        super(x, y, ACTOR_WIDTH, ACTOR_HEIGHT);
        dir = Direction.RIGHT;
        animator = new PlayerAnimator(PlayerAnimator.State.JUMP, actorType);
        this.Speed = new Vector(0, 0);
        this.isJumping = true;
        isSquating = false;
        this.ySpeed = 0;
        this.rebornPoint = new int[2];
        this.rebornPoint[0] = x;
        this.rebornPoint[1] = y;
        this.deathDelay = new Delay(45);
        this.deathDelay.stop();
        this.heartCount = PLAYER_REBORN_LIVES;
        this.canBeEffect = true;
        this.effectDelay = new Delay(PLAYER_EFFECT_CD);
        this.effectDelay.stop();
        this.extraSpeed = 0;
        this.invincible = false;
        this.eatFood = false;
        this.paintDelay = new Delay(3); // 無敵狀態閃爍
        this.paint = true;
        this.canShot = true;
        this.playerShootDelay = new Delay(PLAYER_SKILL_CD);
        this.imgInvincible = SceneController.instance().irc().tryGetImage(new Path().image().effects().invincible());
        this.imgSpeedUp = SceneController.instance().irc().tryGetImage(new Path().image().effects().speedUp());
        this.imgSpeedDown = SceneController.instance().irc().tryGetImage(new Path().image().effects().speedDown());
    }

    public int getYSpeed() {
        return this.ySpeed;
    }

    public void setYSpeed(int y) {
        this.ySpeed = y;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public int getId() {
        return this.Id;
    }

    public void setJumping(boolean jumping) {
        this.isJumping = jumping;
    }

    public void setCanGetEffect(boolean canGetSkill) {
        this.canBeEffect = canGetSkill;
    }

    public boolean invincible() {
        return this.invincible;
    }

    public boolean isSquating() {
        return this.isSquating;
    }

    public Delay getSkillDelay() {
        return this.effectDelay;
    }

    public Delay getPlayerShootDelay() {
        return this.playerShootDelay;
    }

    public int getHeartCount() {
        return this.heartCount;
    }

    public PlayerAnimator playerAnimator() {
        return this.animator;
    }

    public Vector getSpeed() {
        return this.Speed;
    }

    public int getMonsterCount() {
        return this.monsterCount;
    }

    public void setMonsterCount(int monsterCount) {
        this.monsterCount = monsterCount;
    }

    public final void setSpeed(Vector vector) {
        this.Speed = vector;
    }

    public void setState(PlayerAnimator.State state) {
        this.animator.setState(state);
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public void setBrickSpeed(int brickSpeed) {
        this.brickSpeed = brickSpeed;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public void setCanShot(boolean canShot) {
        this.canShot = canShot;
    }

    public int getEndingPoint() {
        return endingPoint;
    }

    public void setEndingPoint(int endingPoint) {
        this.endingPoint = endingPoint;
    }

    public void addEndingPoint(int number) {
        this.endingPoint += number;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public void addEventCount(int number) {
        this.eventCount += number;
    }

    public int getDeathAmount() {
        return deathCount;
    }

    public void setDeathAmount(int deathCount) {
        this.deathCount = deathCount;
    }

    public void addDeathAmountOnce() {
        this.deathCount += 1;
    }

    public void setRebornPoint(int x, int y) { // 設定重生點
        this.rebornPoint[0] = x;
        this.rebornPoint[1] = y;
    }

    public void jump(int jump) { // 跳躍
        ySpeed = -jump;
        isJumping = true;
        animator.setState(PlayerAnimator.State.JUMP);
    }

    public void move() { // 移動
        if (!outOfScreen() && animator.State() != PlayerAnimator.State.DEAD) {
            translateX((int) Speed.vx());
            translateY((int) Speed.vy());
        }
    }

    public void fall() { // 重力往下掉
        if (animator.State() != PlayerAnimator.State.DEAD) {
            translateY(this.ySpeed);
        }
    }

    public void effect(int effect) {
        switch (effect) {
            case 1: // 加速
                this.extraSpeed = PLAYER_SPEED / 2;
                break;
            case 2: // 減速
                this.downSpeed = EFFECT_SPEED_DOWN;
                break;
            case 3: // 無敵
                this.invincible = true;
                this.paintDelay.loop();
                break;
            case 4: // 吃到食物
                this.eatFood = true;
                this.effectDelay.loop();
                break;
        }
    }

    private void squat() {
        Rect r = Rect.genWithCenter(collider().centerX(), collider().centerY() - 2, ACTOR_WIDTH, ACTOR_HEIGHT / 2);
        resetRect(r);
        isSquating = true;
    }

    private void setVector() {
        this.Speed = this.Speed.zero();
        if (pressed[0]) {
            this.Speed = this.Speed.add(new Vector(-PLAYER_SPEED - this.extraSpeed + this.brickSpeed + this.downSpeed, 0));
        }
        if (pressed[1]) {
            this.Speed = this.Speed.add(new Vector(PLAYER_SPEED + this.extraSpeed + this.brickSpeed - this.downSpeed, 0));
        }
    }

    public boolean getCanMove() {
        return this.canMove;
    }

    public boolean canShot() {
        return this.canShot;
    }

    public Direction getDirection() {
        return this.dir;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    private void resetEffect() {
        canBeEffect = true;
        extraSpeed = 0;
        invincible = false;
        eatFood = false;
        effectDelay.stop();
        paintDelay.stop();
        paint = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (invincible) {
            g.drawImage(imgInvincible, painter().centerX() - 64, painter().centerY() - 64, UNIT_X * 4, UNIT_Y * 4, null);
        }
        if (eatFood) {
            g.setColor(Color.green);
            g.setFont(FontLoader.Font03(FONT_SIZE_CONTENT));
            g.drawString("HP++", painter().centerX() - 50, painter().centerY() - 50);
        }
        if (paint) {
            animator.paint(dir, painter().left(), painter().top(), painter().right(), painter().bottom(), g);
        }
        if (extraSpeed != 0) {
            g.drawImage(imgSpeedUp, painter().centerX() - 64, painter().top() - 32, UNIT_X * 4, UNIT_Y * 4, null);
        }
        if (downSpeed != 0) {
            g.drawImage(imgSpeedDown, painter().centerX() - 64, painter().top() - 64, UNIT_X * 4, UNIT_Y * 4, null);
        }
    }

    @Override
    public void update() {
        animator.update();
        setVector();
        if (touchLeft()) {
            changeLeft(MapInformation.mapInfo().left());
        }
        if (touchRight()) {
            changeRight(MapInformation.mapInfo().right());
        }
        if (touchTop()) {
            changeTop(MapInformation.mapInfo().top());
        }
        if (touchBottom()) {
            changeBottom(MAP_HEIGHT);
        }

        // 生命/愛心小於0
        if (this.heartCount <= 0) {
            if (!IS_DEBUG) {
                if (animator.State() != PlayerAnimator.State.DEAD) {
                    AudioResourceController.getInstance().play(new Path().sound().effects().effectPlayerDie());
                    animator.setState(PlayerAnimator.State.DEAD);
                    deathDelay.play();
                }
            }
            this.heartCount = PLAYER_REBORN_LIVES;
        }
        if (animator.State() == PlayerAnimator.State.DEAD) {
            deathDelay.play();//啟動死亡DELAY
            Rect r = Rect.genWithCenter(collider().centerX(), collider().centerY(), ACTOR_WIDTH, ACTOR_HEIGHT);
            resetRect(r);
            haveBeenDead = true;
        }
        if (deathDelay.count()) { // 若是死亡DELAY計時到了，設定角色狀態回到重生點
            animator.setState(PlayerAnimator.State.JUMP);
            dir = Direction.RIGHT;
            isJumping = true;
            ySpeed = 0;
            Speed = new Vector(0, 0);
            Rect r = Rect.genWithCenter(collider().centerX(), collider().centerY(), ACTOR_WIDTH, ACTOR_HEIGHT);
            resetRect(r);
            this.downSpeed = 0;
            resetEffect();
            // 角色死亡後的位置
            setRect(new Rect(rebornPoint[0] - UNIT_X, rebornPoint[1], rebornPoint[0] + ACTOR_WIDTH - UNIT_X, rebornPoint[1] + ACTOR_HEIGHT));
        }
        if (effectDelay.count()) { // 若是技能delay計時到了，重置為可以獲得技能
            resetEffect();
        }
        if (paintDelay.count()) {
            if (paint) {
                paint = false;
            } else {
                paint = true;
            }
        }
        if (playerShootDelay.count()) {
            canShot = true;
            playerShootDelay.stop();
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        Direction dir = Direction.getDirection(commandCode);
        if (animator.State() != PlayerAnimator.State.DEAD) {
            switch (dir) {
                case LEFT:
                    if (!isSquating) {
                        pressed[0] = true;
                        setDir(Direction.LEFT);
                        if (animator.State() != PlayerAnimator.State.WALK
                                && !isJumping) {
                            setState(PlayerAnimator.State.WALK);
                        }
                    }
                    break;
                case RIGHT:
                    if (!isSquating) {
                        pressed[1] = true;
                        setDir(Direction.RIGHT);
                        if (animator.State() != PlayerAnimator.State.WALK
                                && !isJumping) {
                            setState(PlayerAnimator.State.WALK);
                        }
                    }
                    break;
                case DOWN:
                    if (!isJumping) {
                        if (!isSquating && animator.State() != PlayerAnimator.State.WALK) {
                            squat();
                        }
                        if (animator.State() != PlayerAnimator.State.SQUAT) {
                            setState(PlayerAnimator.State.SQUAT);
                        }
                    }
                    break;
                case JUMP:
                    if (IS_DEBUG) {
                        jump(PLAYER_JUMP);
                        setState(PlayerAnimator.State.JUMP);
                        break;
                    }
                    if (!isJumping && !isSquating) {
                        jump(PLAYER_JUMP);
                        if (playerAnimator().State() != PlayerAnimator.State.JUMP) {
                            setState(PlayerAnimator.State.JUMP);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        Direction dir = Direction.getDirection(commandCode);
        switch (dir) {
            case LEFT:
                pressed[0] = false;
                if (animator.State() != PlayerAnimator.State.JUMP && animator.State() != PlayerAnimator.State.DEAD && !isSquating) {
                    setState(PlayerAnimator.State.STOP);
                }
                break;
            case RIGHT:
                pressed[1] = false;
                if (animator.State() != PlayerAnimator.State.JUMP && animator.State() != PlayerAnimator.State.DEAD && !isSquating) {
                    setState(PlayerAnimator.State.STOP);
                }

                break;
            case JUMP:
                if (animator.State() != PlayerAnimator.State.JUMP && animator.State() != PlayerAnimator.State.DEAD && !isSquating) {
                    setState(PlayerAnimator.State.STOP);
                }
                break;
            case DOWN:
                resetRect(Rect.genWithCenter(collider().centerX(), collider().bottom() - 18, ACTOR_WIDTH, ACTOR_HEIGHT));
                setState(PlayerAnimator.State.STOP);
                isSquating = false;
                break;
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
    }

}
