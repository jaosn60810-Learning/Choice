package utils;

import controllers.SceneController;

import java.awt.*;

public class HeartAnimator {

    public enum FoodType {
        FOOD(new int[]{0, 0, 0, 0}, 8),
        HeartCount(new int[]{0, 1, 2, 3, 4}, 15),
        FOOD_HP_PLUS_ONE(new int[]{0}, 0),
        FOOD_HP_PLUS_TWO(new int[]{0}, 0 ),
        FOOD_HP_PLUS_THREE(new int[]{0}, 0),
        FOOD_HP_PLUS_FOUR(new int[]{0}, 0),
        FOOD_HP_PLUS_FIVE(new int[]{0}, 0);
        private int arr[];
        private int speed;

        FoodType(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    private Image img;
    private final Delay delay;
    private int count;
    private FoodType foodType;
    private int heardNumber;

    public HeartAnimator(FoodType foodType) {
        switch (foodType) {
            case FOOD:
                int a = Global.random(0, 4);

                switch (a) {
                    case 0:
                        img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusOne());
                        heardNumber = 1;
                        break;
                    case 1:
                        img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusTwo());
                        heardNumber = 2;
                        break;
                    case 2:
                        img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusThree());
                        heardNumber = 3;
                        break;
                    case 3:
                        img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusFour());
                        heardNumber = 4;
                        break;
                    case 4:
                        img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusFive());
                        heardNumber = 5;
                        break;
                }
                break;
            case HeartCount:
                img = SceneController.instance().irc().tryGetImage(new Path().image().objs().heartCount());
                break;
            case FOOD_HP_PLUS_ONE:
                img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusOne());
                heardNumber = 1;
                break;
            case FOOD_HP_PLUS_TWO:
                img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusTwo());
                heardNumber = 2;
                break;
            case FOOD_HP_PLUS_THREE:
                img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusThree());
                heardNumber = 3;
                break;
            case FOOD_HP_PLUS_FOUR:
                img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusFour());
                heardNumber = 4;
                break;
            case FOOD_HP_PLUS_FIVE:
                img = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusFive());
                heardNumber = 5;
                break;
        }
        this.foodType = foodType;
        this.delay = new Delay(foodType.speed);
        this.delay.loop();
        this.count = 0;
    }

    public int getHeartNumber() {
        return heardNumber;
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) {
        g.drawImage(img,
                left, top,
                right, bottom,
                0 + 48 * foodType.arr[count], 0,
                48 + 48 * foodType.arr[count], 48, null);
    }

    public void update() {
        if (delay.count()) {
            count = ++count % foodType.arr.length;
        }
    }
}
