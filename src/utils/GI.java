package utils;

import controllers.SceneController;
import scenes.*;
import sun.applet.Main;
import utils.CommandSolver.*;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GI implements GameKernel.GameInterface, MouseCommandListener, KeyListener {

    public GI(){
        SceneController.instance().change(new MainScene());
    }

    @Override
    public void paint(Graphics g) {
        SceneController.instance().paint(g);
    }

    @Override
    public void update() {
        SceneController.instance().update();
    }

    @Override
    public void mouseTrig(MouseEvent e, MouseState state, long trigTime) {
        MouseCommandListener ml = SceneController.instance().mouseListener();
        if (ml != null) {
            ml.mouseTrig(e, state, trigTime);
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyPressed(commandCode, trigTime);
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyReleased(commandCode, trigTime);
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyTyped(c, trigTime);
        }
    }
}
