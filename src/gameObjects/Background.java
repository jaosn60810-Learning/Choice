package gameObjects;

import controllers.SceneController;
import utils.Path;

import java.awt.*;

public class Background extends GameObject {

    private Image img;
    private int paintRight;
    private int paintLeft;
    private int paintTop;
    private int paintBottom;

    public Background(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.paintLeft =painter().left();
        this.paintRight = painter().right();
        this.paintTop = painter().top();
        this.paintBottom = painter().bottom();
    }

    public void setPaintRight(int right) {
        this.paintRight = right;
    }

    public void setPaintLeft(int left) {
        this.paintLeft = left;
    }
    public void setPaintTop(int top){
        this.paintTop = top;
    }
    public void setPaintBottom(int bottom){
        this.paintBottom = bottom;
    }

    @Override
    public void paintComponent(Graphics g) {
                g.drawImage(img, paintLeft, paintTop, paintRight, paintBottom,
                        paintLeft-painter().left(), paintTop, paintRight-painter().left(), paintBottom, null);
    }

    @Override
    public void update() {
    }

}
