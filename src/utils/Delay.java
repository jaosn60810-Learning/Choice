package utils;

public class Delay {
    private int count;
    private int countLimit;
    private boolean isPause;
    private boolean isLoop;

    public Delay(int countLimit) {
        this.countLimit = countLimit;
        count = 0;
        isPause = true;
        isLoop = false;
    }

    public void setLimit(int limit) {
        this.countLimit = limit;
    }

    public void stop() {
        isPause = true;
        count = 0;
        isLoop = false;
    }

    public void pause() {
        isPause = true;
    }

    public void play() {
        isPause = false;
        isLoop = false;
    }

    public void loop() {
        isPause = false;
        isLoop = true;
    }

    public boolean count() {
        if (isPause) {
            return false;
        }
        if (count >= countLimit) {
            if (isLoop) {
                count = 0;
            } else {
                stop();
            }
            return true;
        }
        count++;
        return false;
    }
}
