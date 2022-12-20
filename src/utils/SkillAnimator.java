package utils;

import controllers.SceneController;

import java.awt.*;


public class SkillAnimator {

    public enum SkillType {
        FIRE(new int[]{0, 0, 1, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 5, 5, 5, 6, 6, 6, 5, 5, 5, 6, 6, 6}, 3),
        PLAYER_ONE_SHOOT(new int[]{0, 1, 2, 3, 4}, 2),
        PLAYER_TWO_SHOOT(new int[]{0, 1, 2, 3, 4}, 2);
        private int arr[];
        private int speed;

        SkillType(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    private Image img;
    private final Delay delay;
    private int count;
    private SkillType skillType;

    public SkillAnimator(SkillType skillType) {
        switch (skillType) {
//            case FIRE:
//                img = SceneController.instance().irc().tryGetImage(new Path().image().players().fireball());
//                break;
            case PLAYER_ONE_SHOOT:
                img = SceneController.instance().irc().tryGetImage(new Path().image().players().playerOneShoot());
                break;
            case PLAYER_TWO_SHOOT:
                img = SceneController.instance().irc().tryGetImage(new Path().image().players().playerTwoShoot());
                break;
        }
        this.skillType = skillType;
        this.delay = new Delay(skillType.speed);
        this.delay.loop();
        this.count = 0;
    }

    public SkillType getSkillType() {
        return this.skillType;
    }

    public void paint(Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {
        g.drawImage(img,
                left, top,
                right, bottom,
                skillType.arr[count] * 192, 1,
                (skillType.arr[count]) * 192 + 191, 192, null);
    }

    public void update() {
        if (delay.count()) {
            count = ++count % skillType.arr.length;
        }
    }
}
