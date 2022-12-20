package scenes;

import camera.MapInformation;
import controllers.AudioResourceController;
import controllers.ImageResourceController;
import controllers.SceneController;
import gameObjects.Brick;
import gameObjects.Player;
import menu.BackgroundType;
import menu.Button;
import menu.Label;
import menu.MouseTriggerImpl;
import menu.Style;
import utils.*;
import maploader.MapInfo;
import maploader.MapLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import static utils.Global.*;
import static utils.PlayerAnimator.ActorType.PLAYER_ONE;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EventScene extends Scene {

    private Delay delay;
    private Image background;
    private Image billboard;
    private Image eventItem;
    private ArrayList<Button> buttons;
    private ArrayList<Brick> bricks;
    private boolean isChoose = false;
    private boolean goHeaven = false;
    private boolean isEnding = false;
    private ArrayList<Player> players;
    private int randomEvent = random(1, 10);

    public EventScene(ArrayList<Player> inputPlayers) {
        players = new ArrayList<>();
        players.add(new Player(PlayerAnimator.ActorType.PLAYER_ONE, 100, 0));
        players.get(0).setHeartCount(inputPlayers.get(0).getHeartCount());
        players.get(0).setMonsterCount(inputPlayers.get(0).getMonsterCount());
        players.get(0).setEventCount(inputPlayers.get(0).getEventCount());
        players.get(0).setEndingPoint(inputPlayers.get(0).getEndingPoint());
    }

    @Override
    public void sceneBegin() {
        // 音樂播放
        AudioResourceController.getInstance().play(new Path().sound().bgm().bgmEvent());

        // 時間
        delay = new Delay(90);

        // 地圖
        MapInformation.setMapInfo(0, 0, SCREEN_X, SCREEN_Y);

        // 背景
        background = SceneController.instance().irc().tryGetImage(new Path().image().backgrounds().backgroundEvent());
        billboard = SceneController.instance().irc().tryGetImage(new Path().image().backgrounds().backgroundBillboard());

        // 所有按鈕陣列
        buttons = new ArrayList<>();
        buttons.add(new Button(screenCenterX(SCREEN_X / 3 * 2) + 100, 460, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("Yes")
                .setTextFont(FontLoader.Font02(FONT_SIZE_CONTENT))
                .setTextColor(Color.white)));
        buttons.add(new Button(screenCenterX(SCREEN_X / 3 * 2) + 452, 460, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("No")
                .setTextFont(FontLoader.Font02(FONT_SIZE_CONTENT))
                .setTextColor(Color.white)));
        buttons.add(new Button(screenCenterX(640) + 350, SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("Continue")
                .setTextFont(FontLoader.Font02(FONT_SIZE_CONTENT))
                .setTextColor(Color.white)));
        buttons.get(0).setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("Yes")
                .setTextFont(FontLoader.Font02(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        buttons.get(1).setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("No")
                .setTextFont(FontLoader.Font02(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        buttons.get(2).setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("Continues")
                .setTextFont(FontLoader.Font02(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        buttons.get(0).setClickedActionPerformed(new Label.ClickedAction() {
            @Override
            public void clickedActionPerformed(int x, int y) {
                isChoose = true;
                players.get(0).addEndingPoint(-1);
                players.get(0).addEventCount(1);
                goHeaven = false;
                if (players.get(0).getEventCount() == 2) {
                    isEnding = true;
                }
            }
        });
        buttons.get(1).setClickedActionPerformed(new Label.ClickedAction() {
            @Override
            public void clickedActionPerformed(int x, int y) {
                isChoose = true;
                players.get(0).addEndingPoint(1);
                players.get(0).addEventCount(1);
                goHeaven = true;
                if (players.get(0).getEventCount() == 2) {
                    isEnding = true;
                }
            }
        });
        buttons.get(2).setClickedActionPerformed(new Label.ClickedAction() {
            @Override
            public void clickedActionPerformed(int x, int y) {
                if (isEnding) {
                    SceneController.instance().change(new EndingScene(players));
                } else if (!isEnding && goHeaven == true) {
                    SceneController.instance().change(new PlayHeavenScene(players));
                } else if (!isEnding && goHeaven == false) {
                    SceneController.instance().change(new PlayHellScene(players));
                }
            }
        });

        // 玩家
        players.add(new Player(PlayerAnimator.ActorType.PLAYER_ONE, 14 * 32, (int) (144 * 5 * 32 - 32 * 10) * 0));

        // 所有磚塊陣列
        bricks = new ArrayList<>();

        // 隨機生成事件物件圖片
        switch (randomEvent) {
            case 1:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem01());
                break;
            case 2:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem02());
                break;
            case 3:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem03());
                break;
            case 4:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem04());
                break;
            case 5:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem05());
                break;
            case 6:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem06());
                break;
            case 7:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem07());
                break;
            case 8:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem08());
                break;
            case 9:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem09());
                break;
            case 10:
                eventItem = ImageResourceController.instance().tryGetImage(new Path().image().events().eventItem10());
                break;
        }

        // 地圖初始化
        mapInitialize();
    }

    @Override
    public void sceneEnd() {
        this.background = null;
        this.billboard = null;
        this.eventItem = null;
        this.buttons = null;
        this.bricks = null;
        ImageResourceController.instance().clear();
        AudioResourceController.getInstance().stop(new Path().sound().bgm().bgmEvent());
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        g.drawImage(billboard, screenCenterX(SCREEN_X / 3 * 2), SCREEN_Y / 8, SCREEN_X / 3 * 2, 500, null);
        g.setFont(FontLoader.Font01(FONT_SIZE_CONTENT));
        g.setColor(Color.white);

        // 繪製角色
        players.get(0).paint(g);

        // 隨機故事
        switch (randomEvent) {
            case 1:
                g.drawString("一個不良於行又瞎眼的老人倒在地上喃喃自語", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「我的神奇拐杖跑到哪裡去了...?」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("耳邊有股聲音讓你把枴杖據為己有", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要聽從耳邊的聲音嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 2:
                g.drawString("一個看起來染疫的怪物倒在地上對你說", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「我快死了, 最後想把一個祕密傳下去...」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("你覺得怪物看起來有些傳染病", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要過去聽怪物說話嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 3:
                g.drawString("你發現一個寫了警語的寶箱", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「凡不是你的就不該打開」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("你看著厚重的寶箱", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要打開嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 4:
                g.drawString("一個看起來快熄滅的火把焦急的對你說", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「快過來幫我別人的煤油搶過來 !」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("你覺得溫度有點高", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要過去幫忙添加煤油嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 5:
                g.drawString("你發現一具剛死不久冒著詛咒臭味的屍體", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("屍體旁邊用血寫著", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("「如果我死了, 請幫我禱告」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要幫忙禱告嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 6:
                g.drawString("真身未明的生命體向你傳遞了訊息", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「να σου δώσω μια ευλογία」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("你猜測生命體在詢問你是否接受提議", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("接受嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 7:
                g.drawString("一面古老的黑鏡散發出不詳的氣息", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「迷途的旅人, 過來, 給你好東西...」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("你隱約覺得不太妥當, 但又充滿好奇", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("想靠近看看嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 8:
                g.drawString("一個可愛的小雞害怕的對你說", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「好心人阿不要吃我我不好吃阿 !!!」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("你覺得這雞越看越美味", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要把雞吃掉嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 9:
                g.drawString("為什麼塔裡會有船 ?", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("你看著華麗的船", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("雖然沒人邀請你, 但你覺得應該上去搜刮一番", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要上船快樂一下嗎 ?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;
            case 10:
                g.drawString("一個看不出容貌的士兵對你說", screenCenterX(640), SCREEN_Y / 8 + 150); // 一個字 32 pixel
                g.drawString("「前方禁止進入, 想通過就先殺了我」", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT);
                g.drawString("你覺得很困擾", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 2);
                g.drawString("要殺了士兵嗎?", screenCenterX(640), SCREEN_Y / 8 + 150 + LINE_SPACING_CONTENT * 3);
                break;

        }

        // 繪製按鈕
        buttons.get(0).paint(g);
        buttons.get(1).paint(g);

        // 繪製磚塊
        for (int j = 0; j < bricks.size(); j++) {
            bricks.get(j).paint(g);
        }

        // 繪製事件
        g.drawImage(eventItem, screenCenterX(UNIT_X * 3), 815, UNIT_X * 3, UNIT_Y * 3, null);

        // 顯示選擇後的增益
        if (isChoose == true) {
            g.drawImage(billboard, screenCenterX(SCREEN_X / 3 * 2), SCREEN_Y / 8, SCREEN_X / 3 * 2, 500, null);
            buttons.get(2).paint(g);
            g.setFont(FontLoader.Font01(FONT_SIZE_CONTENT));
            g.setColor(Color.white);
            g.drawString("附近似乎傳來了什麼崩壞的聲音", screenCenterX(640), SCREEN_Y / 8 + 120 + LINE_SPACING_CONTENT);
            if (goHeaven) {
                g.drawString("你感覺身上有什麼力量消失了【速度降低】", screenCenterX(640), SCREEN_Y / 8 + 120 + LINE_SPACING_CONTENT * 2);
            } else {
                g.drawString("你感受到一股強大的增幅【速度提升】", screenCenterX(640), SCREEN_Y / 8 + 120 + LINE_SPACING_CONTENT * 2);
            }
            g.drawString("有股強大的力量將你拉扯到另一個空間", screenCenterX(640), SCREEN_Y / 8 + 120 + LINE_SPACING_CONTENT * 3);
        }
    }

    @Override
    public void update() {

        // 磚塊更新
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            brick.update();
        }

        // 按鈕更新
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            button.update();
        }

        // 判斷角色動作
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);


            //更新角色
            player.update();
            if (player.playerAnimator().State() != PlayerAnimator.State.DEAD) {
                //改變角色 Y speed
                playerFallSpeed(player);
                player.setCanMove(true);
                //用預判物件判斷角色站在磚塊上
                playerTouchBrickY(bricks, player);
                if (player.getCanMove()) {
                    //角色向下掉
                    player.fall();
                    if (player.playerAnimator().State() != PlayerAnimator.State.JUMP) {
                        player.setState(PlayerAnimator.State.JUMP);
                    }
                    player.setJumping(true);
                }
                player.setCanMove(true);
                if (player.getCanMove()) {
                    player.move();
                }
             }
        }
    }

    // 改變角色 y 方向速度
    private void playerFallSpeed(Player player) {
        // 當往上的時候
        if (player.getYSpeed() < 0) {
            // 上升速度越慢
            player.setYSpeed((int) (player.getYSpeed() * 0.9));
            // 正在跳的狀態
            player.setJumping(true);
        }
        // 當往下的時候
        if (player.getYSpeed() >= 0) {
            // 重力加速度
            player.setYSpeed(player.getYSpeed() + 2);
        }
    }

    private void playerTouchBrickY(ArrayList<Brick> bricks, Player player) {
        // 建立預判物件
        Player tmp = new Player(player.collider().centerX(), player.collider().centerY());
        // 用預判物件判斷 y 方向的移動會不會碰到障礙
        tmp.translateY(player.getYSpeed());
        // 判斷不同種的磚塊與角色的互動關係
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            // 如果碰到磚塊
            if (tmp.isCollision(brick)) {
                // 從上往下撞
                if (tmp.collider().bottom() > brick.collider().top()
                        && tmp.collider().top() < brick.collider().top()) {
                    // 角色的底是磚塊得的上方
                    player.changeBottom(brick.collider().top());
                    // 碰到地板將 isjumping 設為 false
                    player.setJumping(false);
                    // y 方向速度設為 0
                    player.setYSpeed(0);
                    //因為預判物件碰撞到磚塊，將本體可移動設為 false
                    player.setCanMove(false);
                    // 設定方塊加速度
                    player.setBrickSpeed(brick.getBrickSpeed());

                    // 設定踩到磚塊時的狀態
                    if (player.playerAnimator().State() != PlayerAnimator.State.WALK
                            && player.playerAnimator().State() != PlayerAnimator.State.STOP
                            && player.playerAnimator().State() != PlayerAnimator.State.SQUAT
                            && player.playerAnimator().State() != PlayerAnimator.State.DEAD) {
                        // 沒按方向的時候 STOP
                        if (player.getSpeed().vx() == 0 && !player.isSquating()) {
                            player.setState(PlayerAnimator.State.STOP);
                        } else {
                            player.setState(PlayerAnimator.State.WALK);
                        }
                    }
                    break;
                }
            }
        }
    }


    private void mapInitialize() {
        try {
            ArrayList<MapInfo> mapInfo = new MapLoader("genMapEvent.bmp", "genMapEvent.txt").combineInfo();
            for (MapInfo tmp : mapInfo) {  // 地圖產生器內物件的 {左, 上, 寬, 高}
                switch (tmp.getName()) {
                    // 地上的磚塊
                    case "objBrickEventFixed":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BRICK_ENDING));
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
