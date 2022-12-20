package scenes;

import camera.MapInformation;
import controllers.AudioResourceController;
import controllers.SceneController;
import controllers.ImageResourceController;
import gameObjects.Brick;
import gameObjects.Monster;
import maploader.MapInfo;
import maploader.MapLoader;
import menu.BackgroundType;
import menu.BackgroundType.BackgroundImage;
import menu.Label;
import menu.MouseTriggerImpl;
import menu.Button;
import menu.Style;
import utils.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Global.*;

public class SingleEntranceScene extends Scene{

    private ArrayList<Button> buttons;
    private ArrayList<Brick> bricks;
    private Monster monster;
    private Image foodImage;
    private Image brickImage;

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
        buttons.get(0).setClickedActionPerformed((x, y) -> SceneController.instance().change(new PlayScene()));
        buttons.get(1).setClickedActionPerformed((x, y) -> SceneController.instance().change(new MainScene()));

        monster = new Monster(screenCenterX(UNIT_X * 3) - 300, SCREEN_Y - 250,
                UNIT_X * 3, UNIT_Y * 3,
                MonsterAnimator.MonsterType.GHOST_LR, new Vector(0, 0), new Delay(0));

        foodImage = SceneController.instance().irc().tryGetImage(new Path().image().foods().foodHpPlusThree());

        brickImage = SceneController.instance().irc().tryGetImage(new Path().image().objs().objBrickLobbyFixed());

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

        // 繪製怪物和食物圖片
        monster.paint(g);
        g.drawImage(foodImage, screenCenterX(UNIT_X * 3) + 330, SCREEN_Y - 285, UNIT_X * 3, UNIT_Y * 3, null);
        g.drawImage(brickImage, screenCenterX(UNIT_X * 3), SCREEN_Y - 300, UNIT_X * 3, UNIT_Y * 3, null);

        // 文字敘述
        g.setFont(FontLoader.Font01(FONT_SIZE_CONTENT));
        g.setColor(Color.white);

        g.drawString("小心怪物", screenCenterX(UNIT_X * 3) - 350, SCREEN_Y - 350);
        g.drawString("踩踏磚塊", screenCenterX(UNIT_X * 3) - 10, SCREEN_Y - 350);
        g.drawString("補充生命", screenCenterX(UNIT_X * 3) + 320, SCREEN_Y - 350);

        // 標題故事
        g.drawString("你在不明的空間恢復意識", screenCenterX(32 * 11), 100 + LINE_SPACING_CONTENT);
        g.drawString("這裡是哪裡自我是何物", screenCenterX(32 * 10), 100 + LINE_SPACING_CONTENT * 2);
        g.drawString("看來只能透過探索才能知曉", screenCenterX(32 * 12), 100 + LINE_SPACING_CONTENT * 3);
        g.drawString("在異界迴廊中追尋存在的意義吧", screenCenterX(32 * 14), 100 + LINE_SPACING_CONTENT * 4);
        // P1
        g.drawString("操作方式 :", 475, 330 + LINE_SPACING_CONTENT * 2);
        g.drawString("上 , 下 , 左 , 右", 475, 330 + LINE_SPACING_CONTENT * 3);
        g.drawString("攻擊方式 :", 800, 330 + LINE_SPACING_CONTENT * 2);
        g.drawString("SPACE", 800, 330 + LINE_SPACING_CONTENT * 3);



    }

    @Override
    public void update() {
        // 磚塊更新
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            brick.update();
        }
    }

    private void mapInitialize() {
        try {
            ArrayList<MapInfo> mapInfo = new MapLoader("genMapSingleEntrance.bmp", "genMapSingleEntrance.txt").combineInfo();
            for (MapInfo tmp : mapInfo) {  // 地圖產生器內物件的 {左, 上, 寬, 高}
                switch (tmp.getName()) {
                    // 角色
                    case "playerOneDemo":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X * 4, UNIT_Y * 4,
                                BrickAnimator.BrickType.PLAYER_ONE));
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
        return new CommandSolver.MouseCommandListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                for (int i = 0; i < buttons.size(); i++) {
                    MouseTriggerImpl.mouseTrig(buttons.get(i), e, state);
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }
}
