package utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Global {
    public enum Direction {
        DOWN(0),
        LEFT(1),
        RIGHT(2),
        JUMP(3),
        SHOT(4);

        private int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Direction getDirection(int value) {
            switch (value) {
                case 0:
                case 5:
                    return DOWN;
                case 1:
                case 6:
                    return LEFT;
                case 2:
                case 7:
                    return RIGHT;
                case 3:
                case 8:
                    return JUMP;
                case 4:
                case 9:
                    return SHOT;
            }
            return RIGHT;
        }
    }

    public static final boolean IS_DEBUG = false;

    public static void log(String str) {
        System.out.println(str);
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }

    // 視窗大小
    public static final int WINDOW_WIDTH = 1280 + 16;
    public static final int WINDOW_HEIGHT = 960 + 39;
    public static final int SCREEN_X = WINDOW_WIDTH - 8 - 8;
    public static final int SCREEN_Y = WINDOW_HEIGHT - 31 - 8;

    // 字體大小
    public static final int FONT_SIZE_TITLE = 80;
    public static final int FONT_SIZE_CONTENT = 50; // 一個字 32 pixel

    // 行距
    public static final int LINE_SPACING_CONTENT = 48;

    // 按鈕大小
    public static final int BUTTON_SIZE_MENU = 500;
    public static final int BUTTON_SIZE_INSIDE = 300;

    // 地圖大小
    public static final int MAP_WIDTH = WINDOW_WIDTH;
    public static final int MAP_HEIGHT = 720 * 32;

    // 資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的奈秒數

    // 畫面更新時間
    public static final int FRAME_LIMIT = 60;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;

    // 物件單位大小
    public static final int UNIT_X = 32;
    public static final int UNIT_Y = 32;
    public static final int ACTOR_WIDTH = 64;
    public static final int ACTOR_HEIGHT = 64;

    // 持有生命/愛心座標
    public static final int HEARTCOUNT_X = SCREEN_X - 500;
    public static final int HEARTCOUNT_Y = 10;

    // 剩下時間座標
    public static final int TIME_X = 0;
    public static final int TIME_Y = 10;

    // 玩家操控指令
    public static final int P1_DOWN = 0;
    public static final int P1_LEFT = 1;
    public static final int P1_RIGHT = 2;
    public static final int P1_JUMP = 3;
    public static final int P1_SHOT = 4;
    public static final int P2_DOWN = 5;
    public static final int P2_LEFT = 6;
    public static final int P2_RIGHT = 7;
    public static final int P2_JUMP = 8;
    public static final int P2_SHOT = 9;

    // 磚塊相關
    public static final int BRICKSPEED_LEFT = -6;
    public static final int BRICKSPEED_RIGHT = 6;

    // 角色數值
    public static final int PLAYER_EFFECT_CD = 60; // 加速 無敵 的持續時間
    public static final int PLAYER_SKILL_CD = 10; // 技能CD
    public static final int PLAYER_SPEED = 8;
    public static final int PLAYER_JUMP = 35 + 10;
    public static final int PLAYER_REBORN_LIVES = 3; // 玩家死掉復活血量

    // effect
    public static final int EFFECT_DELAY = 300; // 效果持續時間
    public static final int EFFECT_SPEED_DOWN = 1;

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }

    public static double length(double x, double y) {
        return Math.sqrt((x * x) + (y * y));
    }

    public static double twoPointLength(double x, double y, double targetx, double targety) {
        return length(targetx - x, targety - y);
    }

    // 回傳物件置中全螢幕的座標位置
    public static int screenCenterX(double objectLength) {
        int x = (int) (SCREEN_X / 2 - objectLength / 2);
        return x;
    }

    public static int screenCenterY(double objectLength) {
        int y = (int) (SCREEN_Y / 2 - objectLength / 2);
        return y;
    }
}

