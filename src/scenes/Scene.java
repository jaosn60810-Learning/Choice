package scenes;

import utils.CommandSolver.KeyListener;
import utils.CommandSolver.MouseCommandListener;
import java.awt.Graphics;

public abstract class Scene {

    public abstract void sceneBegin();
    
    public abstract void sceneEnd();
    
    public abstract void paint(Graphics g);
    
    public abstract void update();
    
    public abstract MouseCommandListener mouseListener();
    
    public abstract KeyListener  keyListener();
}
