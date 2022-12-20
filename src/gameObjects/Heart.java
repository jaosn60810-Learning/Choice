package gameObjects;

import utils.HeartAnimator;
import utils.HeartAnimator.FoodType;

import java.awt.*;

public class Heart extends GameObject {

    private HeartAnimator heartAnimator;
    private int heardNumber;

    public Heart(int x, int y, int width, int height, FoodType foodType) {
        super(x, y, width, height);
        heartAnimator = new HeartAnimator(foodType);
        heardNumber = heartAnimator.getHeartNumber();
    }

    public int getHeartNumber() {
        return heardNumber;
    }

    @Override
    public void paintComponent(Graphics g) {
        heartAnimator.paint(painter().left(), painter().top(), painter().right(), painter().bottom(), g);
    }

    @Override
    public void update() {
        heartAnimator.update();
    }

}
