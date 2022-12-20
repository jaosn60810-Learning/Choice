package utils;

import controllers.SceneController;

import java.awt.*;

public class RebornPointAnimator {

    public enum RebornPointType {
        UNTOUCH(new int[]{0, 1, 2, 3}, 5),
        TOUCHED(new int[]{0, 1, 2, 3}, 5);
        private int arr[];
        private int speed;

        RebornPointType(int arr[], int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }
    private Image img;
    private Image imgTouch;
    private final Delay delay;
    private int count;
    private RebornPointType type;

    public RebornPointAnimator() {
        this.img = SceneController.instance().irc().tryGetImage(new Path().image().objs().rebornPointOne());
        this.imgTouch = SceneController.instance().irc().tryGetImage(new Path().image().objs().rebornPointTwo());
        this.type = RebornPointType.UNTOUCH;
        this.delay = new Delay(60);
        this.delay.loop();
        this.count = 0;
    }

    public void setType(RebornPointType type) {
        this.type = type;
    }

    public RebornPointType getType() {
        return type;
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) {
        switch (type) {
            case UNTOUCH:
                g.drawImage(img,
                        left, top,
                        right, bottom,
                        0, 96 * type.arr[count],
                        48, 96 + 96 * type.arr[count], null);
                break;
            case TOUCHED:
                g.drawImage(imgTouch,
                        left, top,
                        right, bottom,
                        0, 96 * type.arr[count],
                        48, 96 + 96 * type.arr[count], null);
                break;
        }
    }

    public void update() {
        if (delay.count()) {
            count = ++count % type.arr.length;
        }
    }
}
