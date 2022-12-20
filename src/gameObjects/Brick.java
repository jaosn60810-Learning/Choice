package gameObjects;

import utils.BrickAnimator;
import utils.BrickAnimator.BrickType;
import utils.Delay;
import utils.Vector;

import java.awt.*;

import static utils.Global.*;

public class Brick extends GameObject {

    BrickAnimator brickAnimator;
    private int x;
    private int y;
    private BrickType brickType;
    // 會不見的
    private Delay delay;
    private boolean vanish;
    // 移動的磚塊要用到的
    private Vector vector;
    private boolean moveReverse;
    // 加速方塊用
    private int brickSpeed;

    // 不會動磚塊
    public Brick(int x, int y, int width, int height, BrickType brickType) {
        super(x, y, width, height);
        this.brickAnimator = new BrickAnimator(brickType);
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.brickType = brickType;
        this.delay = new Delay(1);
        this.delay.stop();
        this.moveReverse = false;
        // 向左加速磚
        if (brickType == BrickType.BRICK_SPEED_UP_LEFT) {
            this.brickSpeed = BRICKSPEED_LEFT;
        }
        // 向右加速磚
        else if (brickType == BrickType.BRICK_SPEED_UP_RIGHT) {
            this.brickSpeed = BRICKSPEED_RIGHT;
        } else {
            this.brickSpeed = 0;
        }
    }

    // 不會動磚塊
    public Brick(int x, int y, int width, int height, BrickType brickType, Delay delay) {
        super(x, y, width, height);
        this.brickAnimator = new BrickAnimator(brickType);
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.brickType = brickType;
        this.delay = delay;
        this.delay.loop();
        this.moveReverse = false;
        if (brickType == BrickType.BRICK_SPEED_UP_LEFT) {
            this.brickSpeed = BRICKSPEED_LEFT;
        } else if (brickType == BrickType.BRICK_SPEED_UP_RIGHT) {
            this.brickSpeed = BRICKSPEED_RIGHT;
        } else {
            this.brickSpeed = 0;
        }
    }


    // 會動磚塊
    public Brick(int x, int y, int width, BrickType brickType, Vector vector, Delay delay) {
        super(x, y, width, UNIT_Y);
        brickAnimator = new BrickAnimator(brickType);
        this.x = x - width / 2;
        this.y = y - UNIT_Y / 2;
        this.brickType = brickType;
        this.vector = vector;
        this.moveReverse = false;
        this.delay = delay;
        this.delay.loop();

        // 是否是加速磚
        if (brickType == BrickType.BRICK_SPEED_UP_LEFT) {
            this.brickSpeed = BRICKSPEED_LEFT;
        } else if (brickType == BrickType.BRICK_SPEED_UP_RIGHT) {
            this.brickSpeed = BRICKSPEED_RIGHT;
        } else {
            this.brickSpeed = 0;
        }
    }

    // 會消失磚塊
    public Brick(int x, int y, BrickType brickType, Delay delay) {
        super(x, y, UNIT_X, UNIT_Y);
        brickAnimator = new BrickAnimator(brickType);
        this.x = x - UNIT_X / 2;
        this.y = y - UNIT_Y / 2;
        this.brickType = brickType;
        this.vector = new Vector(0, 0);
        this.moveReverse = false;
        this.vanish = false;
        this.delay = delay;
        this.delay.loop();

        // 會消失
        if (brickType == BrickType.BRICK_VANISH
                || brickType == BrickType.BRICK_HEAVEN_VANISH
                || brickType == BrickType.BRICK_HELL_VANISH) {
            this.delay.stop();
        }
        // 是否是加速磚
        if (brickType == BrickType.BRICK_SPEED_UP_LEFT) {
            this.brickSpeed = BRICKSPEED_LEFT;
        } else if (brickType == BrickType.BRICK_SPEED_UP_RIGHT) {
            this.brickSpeed = BRICKSPEED_RIGHT;
        } else {
            this.brickSpeed = 0;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getBrickSpeed() {
        return this.brickSpeed;
    }

    public Vector getVector() {
        return this.vector;
    }

    public BrickType getBrickType() {
        return this.brickType;
    }

    public Delay getDelay() {
        return this.delay;
    }

    public boolean getVanish() {
        return this.vanish;
    }

    @Override
    public void paintComponent(Graphics g) {
        brickAnimator.paint(painter().left(), painter().top(), painter().right(), painter().bottom(), g);
    }

    @Override
    public void update() {
        if (brickType == BrickType.BRICK_MOVE_LR
                || brickType == BrickType.BRICK_MOVE_UD
                || brickType == BrickType.BRICK_MOVE_SLASH
                || brickType == BrickType.BRICK_HEAVEN_MOVE_LR
                || brickType == BrickType.BRICK_HEAVEN_MOVE_UD
                || brickType == BrickType.BRICK_HEAVEN_MOVE_SLASH
                || brickType == BrickType.BRICK_HELL_MOVE_LR
                || brickType == BrickType.BRICK_HELL_MOVE_UD
                || brickType == BrickType.BRICK_HELL_MOVE_SLASH
        ) {
            if (moveReverse) {
                this.vector = new Vector(-Math.abs(this.vector.vx()), Math.abs(this.vector.vy()));
            } else {
                this.vector = new Vector(Math.abs(this.vector.vx()), -Math.abs(this.vector.vy()));
            }
            if (delay.count()) {
                if (moveReverse) {
                    moveReverse = false;
                } else {
                    moveReverse = true;
                }
            }
            translateX((int) this.vector.vx());
            translateY((int) this.vector.vy());
        }
        if (brickType == BrickType.BRICK_VANISH
                || brickType == BrickType.BRICK_HEAVEN_VANISH
                || brickType == BrickType.BRICK_HELL_VANISH) {
            if (delay.count()) {
                vanish = true;
            }
        }
        if (brickType == BrickType.BRICK_SPRING
        || brickType == BrickType.PLAYER_ONE
        || brickType == BrickType.PLAYER_TWO) {
            brickAnimator.update();
        }
    }
}
