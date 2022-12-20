package controllers;

import scenes.Scene;
import utils.CommandSolver.MouseCommandListener;
import utils.CommandSolver.KeyListener;

import java.awt.Graphics;

public class SceneController {

    private static SceneController sceneController;
    private Scene currentScene;
    private Scene lastScene;
    private ImageResourceController lastIrc;
    private ImageResourceController currentIrc;
    private MouseCommandListener ml;
    private KeyListener kl;

    private SceneController() {
        lastIrc = new ImageResourceController();
        currentIrc = new ImageResourceController();
    }

    public static SceneController instance() {
        if (sceneController == null) {
            sceneController = new SceneController();
        }
        return sceneController;
    }

    public ImageResourceController irc() {
        return currentIrc;
    }

    public void change(Scene scene) {
        lastScene = currentScene;
        if (scene != null) {
            scene.sceneBegin();
        }
        ml = scene.mouseListener();
        kl = scene.keyListener();
        currentScene = scene;
    }

    public void paint(Graphics g) {
        if (currentScene != null) {
            currentScene.paint(g);
        }
    }

    public void update() {
        if (lastScene != null) {
            lastScene.sceneEnd();
            lastScene = null;
        }
        if (currentScene != null) {
            currentScene.update();
        }
    }

    public MouseCommandListener mouseListener() {
        return ml;
    }

    public KeyListener keyListener() {
        return kl;
    }
}
