package scenes;

import controllers.SceneController;
import controllers.ImageResourceController;
import controllers.AudioResourceController;
import menu.BackgroundType;
import menu.MouseTriggerImpl;
import menu.Button;
import menu.Style;
import utils.CommandSolver;
import utils.FontLoader;
import utils.Path;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static utils.Global.*;

public class MainScene extends Scene {

    private Image background;
    private ArrayList<Button> buttons;

    @Override
    public void sceneBegin() {
        // 開場音樂播放
        AudioResourceController.getInstance().play(new Path().sound().bgm().bgmOpening());

        // 背景畫面
        background = SceneController.instance().irc().tryGetImage(new Path().image().backgrounds().backgroundOpening());

        // 按鈕
        buttons = new ArrayList<>();
        buttons.add(new Button(screenCenterX(BUTTON_SIZE_MENU), 400, new Style.StyleRect(BUTTON_SIZE_MENU, 100, true, new BackgroundType.BackgroundColor(Color.darkGray))
                .setText("Single Mode")
                .setTextFont(FontLoader.Font03(FONT_SIZE_TITLE))
                .setTextColor(Color.white)));
        buttons.add(new Button(screenCenterX(BUTTON_SIZE_MENU) , 550, new Style.StyleRect(BUTTON_SIZE_MENU, 100, true, new BackgroundType.BackgroundColor(Color.darkGray))
                .setText("Multiple Mode")
                .setTextFont(FontLoader.Font03(FONT_SIZE_TITLE))
                .setTextColor(Color.white)));
        buttons.add(new Button(screenCenterX(BUTTON_SIZE_MENU) , 700, new Style.StyleRect(BUTTON_SIZE_MENU, 100, true, new BackgroundType.BackgroundColor(Color.darkGray))
                .setText("Leave")
                .setTextFont(FontLoader.Font03(FONT_SIZE_TITLE))
                .setTextColor(Color.white)));
        buttons.get(0).setStyleHover(new Style.StyleRect(BUTTON_SIZE_MENU, 100, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("Single Mode")
                .setTextFont(FontLoader.Font03(FONT_SIZE_TITLE))
                .setTextColor(Color.white));
        buttons.get(1).setStyleHover(new Style.StyleRect(BUTTON_SIZE_MENU, 100, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("Multiple Mode")
                .setTextFont(FontLoader.Font03(FONT_SIZE_TITLE))
                .setTextColor(Color.white));
        buttons.get(2).setStyleHover(new Style.StyleRect(BUTTON_SIZE_MENU, 100, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("Leave")
                .setTextFont(FontLoader.Font03(FONT_SIZE_TITLE))
                .setTextColor(Color.white));
        buttons.get(0).setClickedActionPerformed((x, y) -> SceneController.instance().change(new SingleEntranceScene()));
        buttons.get(1).setClickedActionPerformed((x, y) -> SceneController.instance().change(new MultiEntranceScene()));
        buttons.get(2).setClickedActionPerformed((x, y) -> System.exit(0));
    }

    @Override
    public void sceneEnd() {
        this.background = null;
        this.buttons = null;
        ImageResourceController.instance().clear();
        AudioResourceController.getInstance().stop(new Path().sound().bgm().bgmOpening());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        g.setFont(FontLoader.Font01(FONT_SIZE_TITLE * 3));
        g.drawString("異界迴廊 CHOICE", WINDOW_WIDTH / 13, WINDOW_HEIGHT / 4);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).paint(g);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (e, state, trigTime) -> {
            for (int i = 0; i < buttons.size(); i++) {
                MouseTriggerImpl.mouseTrig(buttons.get(i), e, state);
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
