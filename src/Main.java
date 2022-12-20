import static utils.Global.*;

import utils.GI;
import utils.GameKernel;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        final JFrame jf = new JFrame();
        jf.setTitle("Your Choices");
        jf.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int[][] command = {
                {KeyEvent.VK_LEFT, P1_LEFT},
                {KeyEvent.VK_RIGHT, P1_RIGHT},
                {KeyEvent.VK_DOWN, P1_DOWN},
                {KeyEvent.VK_UP, P1_JUMP},
                {KeyEvent.VK_SPACE, P1_SHOT},
                {KeyEvent.VK_A, P2_LEFT},
                {KeyEvent.VK_D, P2_RIGHT},
                {KeyEvent.VK_S, P2_DOWN},
                {KeyEvent.VK_W, P2_JUMP},
                {KeyEvent.VK_Z, P2_SHOT},
        };

        GI gi = new GI();
        GameKernel gk;
        gk = new GameKernel.Builder(gi, LIMIT_DELTA_TIME, NANOSECOND_PER_UPDATE)
                .initListener(command)
                .enableKeyboardTrack(gi)
                .mouseForceRelease()
                .trackChar()
                .keyCleanMode()
                .enableMouseTrack(gi)
                .gen();

        jf.add(gk);
        jf.setVisible(true);
        gk.run(IS_DEBUG);
    }
}
