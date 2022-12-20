package menu;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BackgroundType {

    public abstract <T> T getBackground();

    public abstract void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height);

    //設定背景為顏色
    public static class BackgroundColor extends BackgroundType {

        private Color color;

        public BackgroundColor(Color color) {
            this.color = color;
        }

        @Override
        public Color getBackground() {
            return this.color;
        }

        @Override
        public void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height) {
            g.setColor(color);
            if (isOval) {
                if (isFill) {
                    g.fillOval(x, y, width, height);
                } else {
                    g.drawOval(x, y, width, height);
                }
            } else {
                if (isFill) {
                    g.fillRect(x, y, width, height);
                } else {
                    g.drawRect(x, y, width, height);
                }
            }
        }
    }

    //設定背景為圖片
    public static class BackgroundImage extends BackgroundType {

        private Image img;

        public BackgroundImage(Image image) {
            this.img = image;
        }

        @Override
        public Image getBackground() {
            return this.img;
        }

        @Override
        public void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height) {
            if (isOval) {
                BufferedImage imageSrcIn = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D ovalSrcIn = imageSrcIn.createGraphics();
                ovalSrcIn.fillOval(0, 0, width, height);
                ovalSrcIn.setComposite(AlphaComposite.SrcIn);
                ovalSrcIn.drawImage(this.img, 0, 0, width, width, null);
                g.drawImage(imageSrcIn, x, y, null);

            } else {
                g.drawImage(this.img, x, y, width, height, null);
            }
        }
    }
}
