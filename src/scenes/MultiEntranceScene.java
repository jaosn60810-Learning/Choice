package scenes;

import camera.MapInformation;
import controllers.SceneController;
import gameObjects.Brick;
import gameObjects.Player;
import maploader.MapInfo;
import maploader.MapLoader;
import menu.*;
import menu.Button;
import menu.Label;
import utils.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Global.*;
import static utils.Global.FONT_SIZE_CONTENT;

public class MultiEntranceScene extends Scene {

    private ArrayList<Button> buttons;
    private ArrayList<Brick> bricks;

    @Override
    public void sceneBegin() {

        MapInformation.setMapInfo(0, 0, SCREEN_X, SCREEN_Y);

        // 按鈕
        buttons = new ArrayList<>();
        buttons.add(new Button(screenCenterX(BUTTON_SIZE_INSIDE) - 340, SCREEN_Y - 100, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("START")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white)));
        buttons.add(new Button(screenCenterX(BUTTON_SIZE_INSIDE) + 340, SCREEN_Y - 100, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("BACK")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white)));
        buttons.get(0).setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("START")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        buttons.get(1).setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("BACK")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        buttons.get(0).setClickedActionPerformed((x, y) -> SceneController.instance().change(new MultiPlayScene()));
        buttons.get(1).setClickedActionPerformed((x, y) -> SceneController.instance().change(new MainScene()));

        // 所有磚塊陣列
        bricks = new ArrayList<>();

        // 地圖初始化
        mapInitialize();
    }

    @Override
    public void sceneEnd() {
        this.buttons = null;
        this.bricks = null;
    }

    @Override
    public void paint(Graphics g) {
        // 繪製按鈕
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).paint(g);
        }
        // 繪製磚塊
        for (int j = 0; j < bricks.size(); j++) {
            bricks.get(j).paint(g);
        }
        // 文字敘述
        g.setFont(FontLoader.Font01(FONT_SIZE_CONTENT));
        g.setColor(Color.white);
        // 標題故事
        g.drawString("在奇異的空間", screenCenterX(32 * 6), 100 + LINE_SPACING_CONTENT);
        g.drawString("兩個存在體莫名的交會", screenCenterX(32 * 10), 100 + LINE_SPACING_CONTENT * 2);
        g.drawString("在異界迴廊中追尋存在的意義吧", screenCenterX(32 * 14), 100 + LINE_SPACING_CONTENT * 3);
        g.drawString("冒險結束後會有什麼呢", screenCenterX(32 * 10), 100 + LINE_SPACING_CONTENT * 4);
        // P1
        g.drawString("P2", 290, 550);
        g.drawString("操作方式 :", 100, 550 + LINE_SPACING_CONTENT * 2);
        g.drawString("W , A , S , D", 100, 550 + LINE_SPACING_CONTENT * 3);
        g.drawString("攻擊方式 :", 400, 550 + LINE_SPACING_CONTENT * 2);
        g.drawString("Z", 400, 550 + LINE_SPACING_CONTENT * 3);
        // P2
        g.drawString("P1", 960, 550);
        g.drawString("操作方式 :", 750, 550 + LINE_SPACING_CONTENT * 2);
        g.drawString("上 , 下 , 左 , 右", 750, 550 + LINE_SPACING_CONTENT * 3);
        g.drawString("攻擊方式 :", 1050, 550 + LINE_SPACING_CONTENT * 2);
        g.drawString("SPACE", 1050, 550 + LINE_SPACING_CONTENT * 3);

    }

    @Override
    public void update() {
        //磚塊更新
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            brick.update();
        }
    }

    private void mapInitialize() {
        try {
            ArrayList<MapInfo> mapInfo = new MapLoader("genMapMultiEntrance.bmp", "genMapMultiEntrance.txt").combineInfo();
            for (MapInfo tmp : mapInfo) {  // 地圖產生器內物件的 {左, 上, 寬, 高}
                switch (tmp.getName()) {
                    // 角色
                    case "playerOneDemo":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X * 4, UNIT_Y * 4,
                                BrickAnimator.BrickType.PLAYER_ONE));
                        break;
                    case "playerTwoDemo":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X * 4, UNIT_Y * 4,
                                BrickAnimator.BrickType.PLAYER_TWO));
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MainScene.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
