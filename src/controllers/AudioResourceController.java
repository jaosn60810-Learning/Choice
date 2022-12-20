package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author LSYu
 */
public class AudioResourceController {

    private static AudioResourceController audioCenter;
    private Map<String, ClipThread> soundMap;
    private final ClipThread.FinishHandler finishHandler = (String fileName, Clip clip) -> {
        if (this.soundMap.containsKey(fileName)) {
            if (this.soundMap.get(fileName).framePos == -1) {
                this.soundMap.remove(fileName);
                clip.close();
            }
        } else {
            clip.close();
        }
    };

    private AudioResourceController() {
        this.soundMap = new HashMap<>();
    }

    public static AudioResourceController getInstance() {
        if (audioCenter == null) {
            audioCenter = new AudioResourceController();
        }
        return audioCenter;
    }

    public void play(final String fileName) {
        if (this.soundMap.containsKey(fileName)) {
            final ClipThread ct = this.soundMap.get(fileName);
            if (!ct.isDead()) {
                ct.playSound();
                return;
            }
        }
        final ClipThread ct = new ClipThread(fileName, 1, this.finishHandler);
        this.soundMap.put(fileName, ct);
        ct.start();
    }

    public void shot(final String fileName) {
        new ClipThread(fileName, 1, this.finishHandler).start();
    }

    public void loop(final String fileName, final int count) {
        final ClipThread ct = new ClipThread(fileName, count, this.finishHandler);
        this.soundMap.put(fileName, ct);
        ct.start();
    }

    public void pause(final String fileName) {
        if (this.soundMap.containsKey(fileName)) {
            final ClipThread ct = this.soundMap.get(fileName);
            ct.framePos = ct.clip.getFramePosition();
            ct.clip.stop();
        }
    }

    // 同樣音效連續撥放時只能停止最後一次
    public void stop(final String fileName) {
        if (!this.soundMap.containsKey(fileName)) {
            return;
        }
        final ClipThread ct = this.soundMap.get(fileName);
        ct.stopSound();
        this.soundMap.remove(fileName);
    }

    // 單例
    private static class ClipThread extends Thread {

        private final String fileName;
        private final int count;
        private final FinishHandler finishHandler;
        private Clip clip;
        private int framePos;

        public ClipThread(final String fileName, final int count, final FinishHandler finishHandler) {
            this.fileName = fileName;
            this.count = count;
            this.finishHandler = finishHandler;
            this.framePos = -1;
        }

        @Override
        public void run() {
            final AudioInputStream audioInputStream;
            try {
                audioInputStream = AudioSystem.getAudioInputStream(new File(this.getClass().getResource(this.fileName).toURI()));
                this.clip = AudioSystem.getClip();
                this.clip.open(audioInputStream);
                this.clip.setFramePosition(0);
                // values have min/max values, for now don't check for outOfBounds values
                final FloatControl gainControl = (FloatControl) this.clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(5f);
                playSound();
                this.clip.addLineListener((LineEvent event) -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        this.finishHandler.whenFinish(this.fileName, this.clip);
                    }
                });
            } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException ex) {
                Logger.getLogger(AudioResourceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void playSound() {
            if (this.framePos != -1) {
                this.clip.setFramePosition(this.framePos);
                this.framePos = -1;
            }
            if (this.count == 1) {
                this.clip.start();
            } else {
                this.clip.loop(this.count);
            }
        }

        public void stopSound() {
            if (this.clip != null && this.clip.isRunning()) {
                this.clip.stop();
                if (isAlive() || !isInterrupted()) {
                    interrupt();
                }
            }
        }

        public boolean isDead() {
            return this.clip == null;
        }

        public interface FinishHandler {
            public void whenFinish(String fileName, Clip clip);
        }
    }
}
