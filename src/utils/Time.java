package utils;

import java.awt.Color;
import java.awt.Graphics;

public class Time implements GameKernel.GameInterface {
    private int x;
    private int y;
    private Delay delay;
    private int minute;
    private int second;
    private int millisecond;

    public Time() {
        delay = new Delay(5);
        delay.loop();
        x = 0;
        y = 0;
        minute = 0;
        second = 0;
        millisecond = 0;
    }

    @Override
    public void paint(Graphics g) {
        g.setFont(FontLoader.Font03(60));
        g.setColor(Color.yellow);
        g.drawString(toString(), x, y);
        g.setColor(Color.black);
    }

    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        if (delay.count()) {
            millisecond++;
        }
        if (millisecond == 10) {
            millisecond = 0;
            second++;
        }
        if (second == 60) {
            second = 0;
            minute++;
        }
    }

    @Override
    public String toString() {
        return ((minute < 10) ? "0" + minute : minute + "") + ":" + ((second < 10) ? "0" + second : second + "") + ":" + millisecond;
    }

}
