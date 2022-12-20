package utils;

import controllers.SceneController;

import java.awt.*;

public class BrickAnimator {

    public enum BrickType {
        // 會消失
        FOOD, // 食物

        // 初始大廳
        // 會動
        BRICK_MOVE_LR, // 會左右動 
        BRICK_MOVE_UD, // 上下動
        BRICK_MOVE_SLASH, // 斜動

        // 會自己消失
        BRICK_VANISH, // 磚塊踩了會消失

        // 不會破
        BRICK_FIX, // 固定的磚塊

        // 天堂
        // 會動
        BRICK_HEAVEN_MOVE_LR, // 會左右動
        BRICK_HEAVEN_MOVE_UD, // 上下動
        BRICK_HEAVEN_MOVE_SLASH, // 斜動

        // 會自己消失
        BRICK_HEAVEN_VANISH, // 磚塊踩了會消失

        // 不會破
        BRICK_HEAVEN_FIX, // 固定的磚塊

        // 地獄
        // 會動
        BRICK_HELL_MOVE_LR, // 會左右動
        BRICK_HELL_MOVE_UD, // 上下動
        BRICK_HELL_MOVE_SLASH, // 斜動

        // 會自己消失
        BRICK_HELL_VANISH, // 磚塊踩了會消失

        // 不會破
        BRICK_HELL_FIX, // 固定的磚塊

        // 功能型磚塊
        BRICK_SPRING(new int[]{0, 1, 2, 1}, 10), // 水柱把角色彈起
        BRICK_ENDING, // 終點線
        BRICK_SPEED_UP_LEFT,  // 向左加速
        BRICK_SPEED_UP_RIGHT, // 向右加速

        // 展示用
        PLAYER_ONE(new int[]{0, 1, 2, 1}, 10),
        PLAYER_TWO(new int[]{0, 1, 2, 1}, 10);

        private int[] state;
        private int speed;

        BrickType() {

        }

        BrickType(int[] state, int speed) {
            this.state = state;
            this.speed = speed;
        }
    }

    // 用 enum 決定印出哪種 brick
    private Image img;
    private BrickType brickType;
    private final Delay delay;
    private int count;

    public BrickAnimator(BrickType brickType) {
        this.brickType = brickType;
        switch (brickType) {
            case FOOD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().FOOD());
                break;
            case BRICK_MOVE_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickLobbyLRMove());
                break;
            case BRICK_HEAVEN_MOVE_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHeavenLRMove());
                break;
            case BRICK_HELL_MOVE_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHellLRMove());
                break;
            case BRICK_MOVE_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickLobbyUDMove());
                break;
            case BRICK_HEAVEN_MOVE_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHeavenUDMove());
                break;
            case BRICK_HELL_MOVE_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHellUDMove());
                break;
            case BRICK_MOVE_SLASH:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickLobbySlashMove());
                break;
            case BRICK_HEAVEN_MOVE_SLASH:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHeavenSlashMove());
                break;
            case BRICK_HELL_MOVE_SLASH:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHellSlashMove());
                break;
            case BRICK_VANISH:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickLobbyDrop());
                break;
            case BRICK_HEAVEN_VANISH:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHeavenDrop());
                break;
            case BRICK_HELL_VANISH:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHellDrop());
                break;
            case BRICK_FIX:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickLobbyFixed());
                break;
            case BRICK_HEAVEN_FIX:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHeavenFixed());
                break;
            case BRICK_HELL_FIX:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickHellFixed());
                break;
            case BRICK_SPRING:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickSpring());
                break;
            case BRICK_ENDING:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickEventFixed());
                break;
            case BRICK_SPEED_UP_LEFT:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickSpeedUpLeft());
                break;
            case BRICK_SPEED_UP_RIGHT:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickSpeedUpRight());
                break;
            case PLAYER_ONE:
                img = SceneController.instance().irc().tryGetImage(new Path().image().players().playerOneDemo());
                break;
            case PLAYER_TWO:
                img = SceneController.instance().irc().tryGetImage(new Path().image().players().playerTwoDemo());
                break;
        }
        this.delay = new Delay(brickType.speed);
        this.delay.loop();
        this.count = 0;
    }

    public BrickType getBrickType() {
        return this.brickType;
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) { // 加入 enum 從外面帶入狀態來決定印哪個磚塊
        if (brickType == brickType.BRICK_SPRING) { // 水柱
            g.drawImage(img,
                    left, top, right, bottom,
                    0,
                    0 + 96 * brickType.state[count],
                    96,
                    96 + 96 * brickType.state[count], null);
        }
        else if (brickType == brickType.PLAYER_ONE || brickType == brickType.PLAYER_TWO) {
            g.drawImage(img,
                    left, top, right, bottom,
                    0 + 48 * (brickType.state[count]),
                    0,
                    48 + 48 * (brickType.state[count]),
                    48, null);
        }
        else {
            g.drawImage(img, left, top, right, bottom, 0, 0, 48, 48, null);
        }
    }

    public void update() {
        if (delay.count()) { // 用 delay 來跑 count
            count = ++count % brickType.state.length;
        }
    }
}
