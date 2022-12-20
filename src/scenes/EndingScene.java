package scenes;

import controllers.AudioResourceController;
import controllers.ImageResourceController;
import controllers.SceneController;
import gameObjects.Player;
import menu.*;
import menu.Button;
import menu.Label;
import utils.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static utils.Global.*;
import static utils.Global.FONT_SIZE_TITLE;
import static utils.PlayerAnimator.ActorType.PLAYER_ONE;

public class EndingScene extends Scene {
    private Delay delay;
    private Image background;
    private ArrayList<Player> players;
    private Button button;

    public EndingScene(ArrayList<Player> inputPlayers) {
        players = new ArrayList<>();
        players.add(new Player(PlayerAnimator.ActorType.PLAYER_ONE, 0, 0));
        players.get(0).setHeartCount(inputPlayers.get(0).getHeartCount());
        players.get(0).setMonsterCount(inputPlayers.get(0).getMonsterCount());
        players.get(0).setEventCount(inputPlayers.get(0).getEventCount());
        players.get(0).setEndingPoint(inputPlayers.get(0).getEndingPoint());
    }

    @Override
    public void sceneBegin() {
        // 時間
        delay = new Delay(90);

        // 音樂播放
        AudioResourceController.getInstance().play(new Path().sound().bgm().bgmEnding());

        // 背景
        if (players.get(0).getEndingPoint() > 0) {
            background = SceneController.instance().irc().tryGetImage(new Path().image().backgrounds().backgroundEndingUp());
        } else if (players.get(0).getEndingPoint() == 0) {
            background = SceneController.instance().irc().tryGetImage(new Path().image().backgrounds().backgroundEndingNeutral());
        } else if (players.get(0).getEndingPoint() < 0){
            background = SceneController.instance().irc().tryGetImage(new Path().image().backgrounds().backgroundEndingDown());
        }

        // 按鈕
        button = new menu.Button(screenCenterX(BUTTON_SIZE_INSIDE) + 340, SCREEN_Y - 100, new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.gray))
                .setText("BACK")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        button.setStyleHover(new Style.StyleRect(BUTTON_SIZE_INSIDE, 50, true, new BackgroundType.BackgroundColor(Color.LIGHT_GRAY))
                .setText("BACK")
                .setTextFont(FontLoader.Font03(FONT_SIZE_CONTENT))
                .setTextColor(Color.white));
        button.setClickedActionPerformed((x, y) -> SceneController.instance().change(new MainScene()));
    }

    @Override
    public void sceneEnd() {
        this.delay = null;
        this.background = null;
        this.players = null;
        this.button = null;
        ImageResourceController.instance().clear();
        AudioResourceController.getInstance().stop(new Path().sound().bgm().bgmEvent());
    }

    @Override
    public void paint(Graphics g) {

        g.drawImage(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);

        // 依照結局不同來畫
        if (players.get(0).getEndingPoint() > 0) {
            g.setColor(Color.gray);
            g.setFont(FontLoader.Font01(FONT_SIZE_CONTENT));
            g.drawString("你漂浮在一個明亮開闊而舒適的空間", screenCenterX(32 * 16), WINDOW_HEIGHT / 4 + 50);
            g.drawString("在異界迴廊中實現善舉著實不易", screenCenterX(32 * 14), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT);
            g.drawString("存在體不滅的回顧是你的獎賞", screenCenterX(32 * 13), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 2);
            g.drawString("只是無人在此等候", screenCenterX(32 * 8), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 3);
            g.drawString("異界迴廊在下方閃爍著曖昧不明的光芒", screenCenterX(32 * 17), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 5);
            g.drawString("誘使你又往它的方向前進...", screenCenterX(32 * 12), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 6);
        } else if (players.get(0).getEndingPoint() == 0) {
            g.setColor(Color.black);
            g.setFont(FontLoader.Font01(FONT_SIZE_CONTENT));
            g.drawString("飄忽不定的選擇讓你終究是個凡人", SCREEN_X / 2, WINDOW_HEIGHT / 4 + 50 - LINE_SPACING_CONTENT);
            g.drawString("你從草地上驚醒", SCREEN_X / 2, WINDOW_HEIGHT / 4 + 50);
            g.drawString("懷疑剛剛經歷的冒險只是一場夢", SCREEN_X / 2, WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT);
            g.drawString("但不遠處的高塔提醒你一切皆為真實", SCREEN_X / 2, WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 2);
            g.drawString("異界迴廊閃爍著曖昧不明的光芒", SCREEN_X / 2, WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 4);
            g.drawString("誘使你又往它的方向前進...", SCREEN_X / 2, WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 5);
        } else {
            g.setColor(Color.white);
            g.setFont(FontLoader.Font01(FONT_SIZE_CONTENT));
            g.drawString("不知名的存在體將你放在手上", screenCenterX(32 * 13), WINDOW_HEIGHT / 4 + 50);
            g.drawString("「Πολύ νόστιμη επιλογή」", screenCenterX(32 * 16), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT);
            g.drawString("你感覺身上有些什麼在逐漸消失", screenCenterX(32 * 14), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 2);
            g.drawString("但並沒有被剝奪自由", screenCenterX(32 * 9), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 3);
            g.drawString("異界迴廊在上方閃爍著曖昧不明的光芒", screenCenterX(32 * 17), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 5);
            g.drawString("誘使你又往它的方向前進...", screenCenterX(32 * 12), WINDOW_HEIGHT / 4 + 50 + LINE_SPACING_CONTENT * 6);
        }
        button.paint(g);
    }

    @Override
    public void update() {}

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return new CommandSolver.MouseCommandListener() {
            @Override
            public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
                MouseTriggerImpl.mouseTrig(button, e, state);
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }
}
