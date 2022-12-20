
package gameObjects;

import utils.RebornPointAnimator;

import java.awt.*;

public class RebornPoint extends GameObject{
    
    private RebornPointAnimator rebornPointAnimator;
    
    public RebornPoint(int x, int y, int width, int height) {
        super(x, y, width, height);
        rebornPointAnimator = new RebornPointAnimator();
    }
    public RebornPointAnimator getRebornPointAnimator(){
        return this.rebornPointAnimator;
    }
    @Override
    public void paintComponent(Graphics g) {
        rebornPointAnimator.paint(painter().left(), painter().top(),
                painter().right(), painter().bottom(), g);
    }

    @Override
    public void update() {
        rebornPointAnimator.update();
    }
    
}
