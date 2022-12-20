package utils;

import controllers.SceneController;

import java.awt.*;


import static utils.PlayerAnimator.State.*;

public class PlayerAnimator {

    public enum ActorType {
        PLAYER_ONE(5),
        PLAYER_TWO(6);
        private int value;

        ActorType(int i) {
            this.value = i;
        }
    }

    public enum State {
        // 角色有幾張圖和切換速度
        WALK(new int[]{0, 1, 2, 3}, 2),
        STOP(new int[]{0, 1, 2, 3}, 8),
        JUMP(new int[]{0, 1, 1, 1, 2, 2}, 5),
        SQUAT(new int[]{0}, 2),
        DEAD(new int[]{0, 1, 1, 1, 2, 2}, 5),
        REBORN(new int[]{0}, 0);
        private int arr[];
        private int speed;

        State(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    private Image img;
    private Image imgDead;
    private Image imgRun;
    private Image imgJump;
    private Image imgStop;
    private Image imgSquat;
    private final Delay delay;
    private int count;
    private State state;
    private ActorType actorType;

    // 決定 img
    public PlayerAnimator(State state, ActorType actorType) {
        switch (actorType) {
            case PLAYER_ONE:
                imgRun = SceneController.instance().irc().tryGetImage(new Path().image().players().playerOneRun());
                imgJump = SceneController.instance().irc().tryGetImage(new Path().image().players().playerOneJump());
                imgStop = SceneController.instance().irc().tryGetImage(new Path().image().players().playerOneStop());
                imgSquat = SceneController.instance().irc().tryGetImage(new Path().image().players().playerOneSquat());
                imgDead = SceneController.instance().irc().tryGetImage(new Path().image().players().playerOneDead());
                break;
            case PLAYER_TWO:
                imgRun = SceneController.instance().irc().tryGetImage(new Path().image().players().playerTwoRun());
                imgJump = SceneController.instance().irc().tryGetImage(new Path().image().players().playerTwoJump());
                imgStop = SceneController.instance().irc().tryGetImage(new Path().image().players().playerTwoStop());
                imgDead = SceneController.instance().irc().tryGetImage(new Path().image().players().playerTwoDead());
                imgSquat = SceneController.instance().irc().tryGetImage(new Path().image().players().playerTwoSquat());
                break;
            default:
                img = null;
                imgDead = null;
        }
        delay = new Delay(0);
        delay.loop();
        count = 0;
        this.actorType = actorType;
        setState(state);
    }

    public void setState(State state) {
        this.state = state;
        this.delay.setLimit(state.speed);
        count = 0;
    }

    public State State() {
        return this.state;
    }

    public void paint(Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {
        switch (actorType) {
            case PLAYER_ONE:
                switch (dir) {
                    // 朝右邊
                    case RIGHT:
                        switch (state) {
                            case WALK:
                                g.drawImage(imgRun,
                                        left, top,
                                        right, bottom,
                                        1 + 64 * (state.arr[count]),
                                        1,
                                        1 + 64 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case JUMP:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                1,
                                                1,
                                                64,
                                                64, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                65,
                                                1,
                                                128,
                                                64, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                129,
                                                1,
                                                192,
                                                64, null);
                                        break;
                                }
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        1 + 64 * (state.arr[count]),
                                        1,
                                        1 + 64 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        1,
                                        25,
                                        64,
                                        64, null);
                                return;
                            case DEAD:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                64,
                                                1,
                                                1,
                                                64, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                128,
                                                1,
                                                65,
                                                64, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                192,
                                                1,
                                                129,
                                                64, null);
                                        break;
                                }
                                return;
                        }
                        // 朝左邊
                    case LEFT:
                        switch (state) {
                            case WALK:
                                g.drawImage(imgRun,
                                        left, top,
                                        right, bottom,
                                        1 + 64 + 64 * (state.arr[count]),
                                        1,
                                        1 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case JUMP:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                64,
                                                1,
                                                1,
                                                64, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                128,
                                                1,
                                                65,
                                                64, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgJump,
                                                left, top,
                                                right, bottom,
                                                192,
                                                1,
                                                129,
                                                64, null);
                                        break;
                                }
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        1 + 64 + 64 * (state.arr[count]),
                                        1,
                                        1 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        64,
                                        25,
                                        1,
                                        64, null);
                                return;
                            case DEAD:
                                switch (state.arr[count]) {
                                    case 0:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                1,
                                                1,
                                                64,
                                                64, null);
                                        break;
                                    case 1:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                65,
                                                1,
                                                128,
                                                64, null);
                                        break;
                                    case 2:
                                        g.drawImage(imgDead,
                                                left, top,
                                                right, bottom,
                                                129,
                                                1,
                                                192,
                                                64, null);
                                        break;
                                }
                                return;
                        }
                }
            case PLAYER_TWO:
                switch (dir) {
                    case RIGHT:
                        switch (state) {
                            case WALK:
                                g.drawImage(imgRun,
                                        left, top,
                                        right, bottom,
                                        0 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case JUMP:
                                g.drawImage(imgJump,
                                        left, top,
                                        right, bottom,
                                        0 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        0 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        64,
                                        32,
                                        1,
                                        64, null);
                                return;
                            case DEAD:
                                g.drawImage(imgDead,
                                        left, top,
                                        right, bottom,
                                        0 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                        }
                    case LEFT:
                        switch (state) {
                            case WALK:
                                if (state.arr[count] % 3 != 2) {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            0 + 64 + 64 * state.arr[count] - 5 * (state.arr[count] / 3),
                                            0,
                                            0 + 64 * state.arr[count] - 5 * (state.arr[count] / 3),
                                            64, null);
                                } else {
                                    g.drawImage(imgRun,
                                            left, top,
                                            right, bottom,
                                            0 + 64 * (state.arr[count]) - 65 * (state.arr[count] / 3) + 60 * ((state.arr[count] + 1) / 3),
                                            0,
                                            0 + 64 * (state.arr[count]) - 65 * (state.arr[count] / 3) + 60 * (state.arr[count] / 3),
                                            64, null);
                                }
                                return;
                            case JUMP:
                                g.drawImage(imgJump,
                                        left, top,
                                        right, bottom,
                                        0 + 64 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case STOP:
                                g.drawImage(imgStop,
                                        left, top,
                                        right, bottom,
                                        0 + 64 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                            case SQUAT:
                                g.drawImage(imgSquat,
                                        left, top,
                                        right, bottom,
                                        64,
                                        32,
                                        1,
                                        64, null);
                                return;
                            case DEAD:
                                g.drawImage(imgDead,
                                        left, top,
                                        right, bottom,
                                        0 + 64 + 64 * (state.arr[count]),
                                        0,
                                        0 + 64 * (state.arr[count]),
                                        64, null);
                                return;
                        }
                }
            default:
                break;
        }
    }


    public void update() {
        if (delay.count()) {
            count = ++count % state.arr.length;
        }
    }
}
