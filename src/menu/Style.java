package menu;

import java.awt.*;

public abstract class Style {

    //text
    protected String text;
    protected Color textColor;
    protected Font textFont;

    //border
    protected boolean isHaveBorder; //是否有外框
    protected Color borderColor; //外框顏色
    protected int borderThickness; //外框厚度
    protected BackgroundType objectBackground; //框底類別(圖片、顏色)
    protected int height;
    protected int width;

    public static class StyleRect extends Style {

        //長方形填滿
        protected boolean isFill;

        //建構子
        public StyleRect(int width, int height, boolean isFill, BackgroundType background) {
            this.isFill = isFill;
            this.height = height;
            this.width = width;
            this.objectBackground = background;
        }

        public StyleRect(int width, int height, BackgroundType background) {
            this(width, height, true, background);
        }

        @Override
        public void paintComponent(Graphics g, int x, int y) {
            if (super.isHaveBorder) {
                g.setColor(super.borderColor);
                g.fillRect(x - super.borderThickness, y - super.borderThickness, width + super.borderThickness * 2, height + super.borderThickness * 2);
            }
            objectBackground.paintBackground(g, false, isFill, x, y, width, height);
            if (super.text != null) {
                g.setColor(super.textColor);
                g.setFont(super.textFont);
                int stringWidth = g.getFontMetrics().stringWidth(super.text);
                g.drawString(super.text, x + (width - stringWidth) / 2,
                        y + height / 2 + g.getFontMetrics().getDescent()+5);
            }
        }

        @Override
        public boolean isHaveBorder() {
            return super.isHaveBorder;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public Color getBorderColor() {
            return super.borderColor;
        }

        public Color getObjectColor() {
            return objectBackground.getBackground();
        }

        public int getBorderThickness() {
            return super.borderThickness;
        }

    }

    public static class StyleOval extends StyleRect {

        //建構子
        public StyleOval(int width, int height, boolean isFill, BackgroundType backgroundType) {
            super(width, height, isFill, backgroundType);
        }

        public StyleOval(int width, int height, BackgroundType backgroundType) {
            super(width, height, true, backgroundType);
        }

        @Override
        public void paintComponent(Graphics g, int x, int y) {
            if (super.isHaveBorder()) {
                g.setColor(super.getBorderColor());
                g.fillOval(x - super.getBorderThickness(), y - super.getBorderThickness(),
                        super.getWidth() + super.getBorderThickness() * 2, super.getHeight() + super.getBorderThickness() * 2);
            }
            objectBackground.paintBackground(g, true, isFill, x, y, width, height);
            g.setFont(textFont);
            g.setColor(super.getTextColor());
            int stringWidth = g.getFontMetrics().stringWidth(super.text);
            g.drawString(super.text, x + (width - stringWidth) / 2, y + height / 2 + g.getFontMetrics().getDescent());

        }

    }

    public abstract void paintComponent(Graphics g, int x, int y);

    //建構子
    public Style() {
        isHaveBorder = false;
        text = "";
        textColor = Color.white;
        borderColor = Color.black;
        borderThickness = 1;
        textFont = new Font("TimesRoman", Font.ITALIC, 50);
    }

    public Style setText(String text) {
        this.text = text;
        return this;
    }

    public int getStyleWidth() {
        return this.width;
    }

    public int getStyleHeight() {
        return this.height;
    }

    public Style setTextColor(Color textColor) {
        this.textColor = textColor;
        return this;
    }

    public Style setTextFont(Font textFont) {
        this.textFont = textFont;
        return this;
    }

    public Style setHaveBorder(boolean isHaveBorder) {
        this.isHaveBorder = isHaveBorder;
        return this;
    }

    public Style setBorderColor(Color BorderColor) {
        this.borderColor = BorderColor;
        return this;
    }

    public Style setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        return this;
    }

    public String getText() {
        return text;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Font getTextFont() {
        return textFont;
    }

    public boolean isHaveBorder() {
        return isHaveBorder;
    }

}
