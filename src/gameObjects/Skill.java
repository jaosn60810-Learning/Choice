package gameObjects;

import utils.Delay;
import utils.Global.Direction;
import utils.SkillAnimator;
import utils.SkillAnimator.SkillType;
import utils.Vector;

import java.awt.*;

public class Skill extends gameObjects.GameObject {

    private SkillAnimator skillAnimator;
    private Vector vector;
    private Delay delay;
    private boolean remove;
    private Direction dir;

    public Skill(int x, int y, int width, int height, SkillType fireBallType, Direction dir, Delay delay) {
        super(x, y, width, height);
        skillAnimator = new SkillAnimator(fireBallType);
        this.dir = dir;
        if (dir == Direction.RIGHT) {
            this.vector = new Vector(20, 0);
        } else if (dir == Direction.LEFT) {
            this.vector = new Vector(-20, 0);
        }
        this.delay = delay;
        this.delay.loop();
        this.remove = false;
    }

    public boolean isRemove() {
        return this.remove;
    }
    public void setRemove(boolean remove){
        this.remove = remove;
    }
    
    public SkillAnimator getSkillAnimator(){
        return this.skillAnimator;
    }
    @Override
    public void paintComponent(Graphics g) {
        skillAnimator.paint(dir, painter().left(), painter().top(), painter().right(), painter().bottom(), g);
    }

    @Override
    public void update() {
        skillAnimator.update();
        translateX((int)this.vector.vx());
        if (delay.count()) {
            remove = true;
        }
    }
}
