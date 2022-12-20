package scenes;

import camera.MapInformation;
import controllers.AudioResourceController;
import controllers.ImageResourceController;
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

public class MultiEndingScene extends Scene {

    private ArrayList<Player> players;
    private ArrayList<Button> buttons;
    private ArrayList<Brick> bricks;

    public MultiEndingScene(ArrayList<Player> inputPlayers) {
        players = new ArrayList<>();
        players.add(new Player(PlayerAnimator.ActorType.PLAYER_ONE, 0, 0));
        players.add(new Player(PlayerAnimator.ActorType.PLAYER_TWO, 0, 0));
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setHeartCount(inputPlayers.get(i).getHeartCount());
            players.get(i).setMonsterCount(inputPlayers.get(i).getMonsterCount());
            players.get(i).setDeathAmount(inputPlayers.get(i).getDeathAmount() / 45);
        }
    }

    // 測試用建構子
    public MultiEndingScene() {}

    @Override
    public void sceneBegin() {

        MapInformation.setMapInfo(0, 0, SCREEN_X, SCREEN_Y);

        // 按鈕
        buttons = new ArrayList<>();
        buttons.add(new Button(screenCenterX(BUTTON_SIZE_INSIDE) - 340, SCREEN_Y - 100, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("PLAY AGAIN")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white)));
        buttons.add(new Button(screenCenterX(BUTTON_SIZE_INSIDE) + 340, SCREEN_Y - 100, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("BACK")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white)));
        buttons.get(0).setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("PLAY AGAIN")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        buttons.get(1).setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("BACK")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        buttons.get(0).setClickedActionPerformed((x, y) -> SceneController.instance().change(new MultiEntranceScene()));
        buttons.get(1).setClickedActionPerformed((x, y) -> SceneController.instance().change(new MainScene()));

        // 所有磚塊陣列
        bricks = new ArrayList<>();

        // 地圖初始化
        mapInitialize();
    }

    @Override
    public void sceneEnd() {
        this.players = null;
        this.buttons = null;
        this.bricks = null;
        ImageResourceController.instance().clear();
        AudioResourceController.getInstance().stop(new Path().sound().bgm().bgmEvent());
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
        g.drawString("一場奇異的冒險結束了 ， 過程之中兩個存在體所激盪出的火花", screenCenterX(32 * 27), 50 + LINE_SPACING_CONTENT);
        g.drawString("或許可以在這未知的空間中留下些什麼．．．", screenCenterX(32 * 27), 50 + LINE_SPACING_CONTENT * 2);
        g.drawString("異界迴廊依照你的表現將你標記", screenCenterX(32 * 27), 50 + LINE_SPACING_CONTENT * 3);

        /**
         * 滿足條件之稱號
         */
        // P1
        if (players.get(0).getDeathAmount() == 0) {
            g.drawString("☆ 神乎其技 ☆", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getDeathAmount() > 30) {
            g.drawString("★ 好想回家 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getHeartCount() > 8) {
            g.drawString("★ 還有點餓 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getHeartCount() == 1) {
            g.drawString("★ 差點消失 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getMonsterCount() > 50) {
            g.drawString("★ 異界屠夫 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getMonsterCount() == 0) {
            g.drawString("★ 和平主義 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getDeathAmount() < 10 && players.get(0).getHeartCount() > 5) {
            g.drawString("★ 大快朵頤 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getDeathAmount() < 10 && players.get(0).getMonsterCount() > 30) {
            g.drawString("★ 玩得不錯 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(0).getMonsterCount() > 25 && players.get(0).getHeartCount() < 3) {
            g.drawString("★ 多吃一點 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        } else {
            g.drawString("★ 無名存在 ★", 870, 150 + LINE_SPACING_CONTENT * 4);
        }

        // P2
        if (players.get(1).getDeathAmount() == 0) {
            g.drawString("☆ 神乎其技 ☆", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getDeathAmount() > 30) {
            g.drawString("★ 好想回家 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getHeartCount() > 8) {
            g.drawString("★ 還有點餓 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getHeartCount() == 1) {
            g.drawString("★ 差點消失 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getMonsterCount() > 50) {
            g.drawString("★ 異界屠夫 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getMonsterCount() == 0) {
            g.drawString("★ 和平主義 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getDeathAmount() < 10 && players.get(1).getHeartCount() > 5) {
            g.drawString("★ 大快朵頤 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getDeathAmount() < 10 && players.get(1).getMonsterCount() > 30) {
            g.drawString("★ 玩得不錯 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else if (players.get(1).getMonsterCount() > 25 && players.get(1).getHeartCount() < 3) {
            g.drawString("★ 多吃一點 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        } else {
            g.drawString("★ 無名存在 ★", 195, 150 + LINE_SPACING_CONTENT * 4);
        }

        /**
         * 回合結束後數值顯示
         */
        // P1
        g.drawString("P1", 960, 550);
        g.drawString("【 擊殺怪物數量 】   " + players.get(0).getMonsterCount(), 800, 550 + LINE_SPACING_CONTENT * 2);
        g.drawString("【 結束時生命值 】   " + players.get(0).getHeartCount(), 800, 550 + LINE_SPACING_CONTENT * 3);
        g.drawString("【 累計死亡次數 】   " + players.get(0).getDeathAmount(), 800, 550 + LINE_SPACING_CONTENT * 4);
        // P2
        g.drawString("P2", 290, 550);
        g.drawString("【 擊殺怪物數量 】   " + players.get(1).getMonsterCount(), 125, 550 + LINE_SPACING_CONTENT * 2);
        g.drawString("【 結束時生命值 】   " + players.get(1).getHeartCount(), 125, 550 + LINE_SPACING_CONTENT * 3);
        g.drawString("【 累計死亡次數 】   " + players.get(1).getDeathAmount(), 125, 550 + LINE_SPACING_CONTENT * 4);

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
