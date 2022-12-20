package utils;

import controllers.SceneController;
import utils.Global.Direction;

import java.awt.*;

public class MonsterAnimator {

    public enum MonsterType {

        MONSTER_SHOOT(new int[]{0, 1, 2, 3, 4}, 10),   // 怪物的攻擊
        BAT_UD(new int[]{0, 1, 2}, 10),
        BAT_LR(new int[]{0, 1, 2}, 10),
        DEMON_UD(new int[]{0, 1, 2}, 10),
        DEMON_LR(new int[]{0, 1, 2}, 10),
        DRAGON_UD(new int[]{0, 1, 2}, 10),
        DRAGON_LR(new int[]{0, 1, 2}, 10),
        GHOST_UD(new int[]{0, 1, 2}, 10),
        GHOST_LR(new int[]{0, 1, 2}, 10),
        GOBLIN_UD(new int[]{0, 1, 2}, 10),
        GOBLIN_LR(new int[]{0, 1, 2}, 10),
        GREENELF_UD(new int[]{0, 1, 2}, 10),
        GREENELF_LR(new int[]{0, 1, 2}, 10),
        MINOTAUR_UD(new int[]{0, 1, 2}, 10),
        MINOTAUR_LR(new int[]{0, 1, 2}, 10),
        PINKELF_UD(new int[]{0, 1, 2}, 10),
        PINKELF_LR(new int[]{0, 1, 2}, 10),
        SKELETON_UD(new int[]{0, 1, 2}, 10),
        SKELETON_LR(new int[]{0, 1, 2}, 10),
        SLIME_UD(new int[]{0, 1, 2}, 10),
        SLIME_LR(new int[]{0, 1, 2}, 10),
        FIRELINE(new int[]{0, 1}, 10),  // 天火
        WATERLINE(new int[]{0, 1}, 10),  // 地水

        // 尖刺
        SPIKES_FIXED;

        private int[] arr;
        private int speed;

        MonsterType(int[] arr, int speed) {
            this.arr = arr;
            this.speed = speed;
        }

        MonsterType() {  // 給刺用的建構子

        }
    }

    private Image img;
    private final Delay delay;
    private int count;
    private MonsterType monsterType;
    private int state;

    public MonsterAnimator(MonsterType monsterType) {
        this.monsterType = monsterType;

        // 新的版本
        switch (monsterType) {
            case MONSTER_SHOOT:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterShoot());
                break;
            case BAT_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterBatUD());
                break;
            case BAT_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterBatLR());
                break;
            case DEMON_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterDemonUD());
                break;
            case DEMON_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterDemonLR());
                break;
            case DRAGON_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterDragonUD());
                break;
            case DRAGON_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterDragonLR());
                break;
            case GHOST_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterGhostUD());
                break;
            case GHOST_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterGhostLR());
                break;
            case GOBLIN_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterGoblinUD());
                break;
            case GOBLIN_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterGoblinLR());
                break;
            case GREENELF_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterGreenElfUD());
                break;
            case GREENELF_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterGreenElfLR());
                break;
            case MINOTAUR_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterMinotaurUD());
                break;
            case MINOTAUR_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterMinotaurLR());
                break;
            case PINKELF_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterPinkElfUD());
                break;
            case PINKELF_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterPinkElfLR());
                break;
            case SKELETON_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterSkeletonUD());
                break;
            case SKELETON_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterSkeletonLR());
                break;
            case SLIME_UD:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterSlimeUD());
                break;
            case SLIME_LR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterSlimeLR());
                break;
            case FIRELINE:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterFireLine());
                break;
            case WATERLINE:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterWaterLine());
                break;
            case SPIKES_FIXED:
                img = SceneController.instance().irc().tryGetImage(new Path().image().monsters().monsterSpikes());
                break;
        }

        this.delay = new Delay(monsterType.speed);
        this.delay.loop();
        this.count = 0;
    }

    // 畫出    小怪
    public void paint(MonsterType monsterType, Direction dir, int left, int top, int right, int bottom, Graphics g) {
        switch (monsterType) {

            case BAT_UD:
            case BAT_LR:
            case DEMON_UD:
            case DEMON_LR:
            case DRAGON_UD:
            case DRAGON_LR:
            case GHOST_UD:
            case GHOST_LR:
            case GOBLIN_UD:
            case GOBLIN_LR:
            case GREENELF_UD:
            case GREENELF_LR:
            case MINOTAUR_UD:
            case MINOTAUR_LR:
            case PINKELF_UD:
            case PINKELF_LR:
            case SKELETON_UD:
            case SKELETON_LR:
            case SLIME_UD:
            case SLIME_LR:
                if (dir == Direction.RIGHT) {
                    if (monsterType.arr[count] <= 2) {
                        g.drawImage(img,
                                left, top, right, bottom,
                                0 + 48 * (monsterType.arr[count] + 3),
                                0,
                                48 + 48 * (monsterType.arr[count] + 3),
                                48, null);
                    } else {
                        g.drawImage(img,
                                left, top, right, bottom,
                                0 + 48 * (monsterType.arr[count]),
                                0,
                                48 + 48 * (monsterType.arr[count]),
                                48, null);
                    }
                } else {
                    if (monsterType.arr[count] > 2) {
                        g.drawImage(img,
                                left, top, right, bottom,
                                0 + 48 * (monsterType.arr[count] - 3),
                                0,
                                48 + 48 * (monsterType.arr[count] - 3),
                                48, null);
                    } else {
                        g.drawImage(img,
                                left, top, right, bottom,
                                0 + 48 * (monsterType.arr[count]),
                                0,
                                48 + 48 * (monsterType.arr[count]),
                                48, null);
                    }
                }
                break;
            /**天火*/
            /**地水*/
            case WATERLINE:
            case FIRELINE:
                g.drawImage(img,
                        left, top, right, bottom,
                        0,
                        32 * monsterType.arr[count],
                        96,
                        32 + 32 * monsterType.arr[count], null);
                break;
            case MONSTER_SHOOT:
                g.drawImage(img,
                        left, top, right, bottom,
                        0 + 192 * (monsterType.arr[count]),
                        0,
                        192 + 192 * (monsterType.arr[count]),
                        192, null);
                break;
            case SPIKES_FIXED:
                g.drawImage(img, left, top, right, bottom, 0, 0, 48, 48, null);
                break;
        }

    }

    public void update() {
        if (delay.count()) {   // 用delay來跑count
            count = ++count % monsterType.arr.length;
        }
    }
}
