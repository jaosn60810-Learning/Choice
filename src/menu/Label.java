package menu;

import utils.GameKernel;
import java.awt.Graphics;

public class Label implements GameKernel.GameInterface {

    public interface ClickedAction {

        void clickedActionPerformed(int x, int y);
    }

    private ClickedAction clickAction;

    public void setClickedActionPerformed(ClickedAction a) {
        clickAction = a;
    }

    public void clickedActionPerformed() {
        clickAction.clickedActionPerformed(getX(), getY());
    }

    public ClickedAction getClickedAction() {
        return clickAction;
    }

    private int x;
    private int y;
    private Style styleNormal;
    private Style styleHover;
    private Style styleFocus;
    private boolean isFocus;
    private boolean isHover;

    public Label(int x, int y, Style styleNormal) {
        this.x = x;
        this.y = y;
        this.styleFocus = this.styleHover = this.styleNormal = styleNormal;
        isFocus = false;
    }

    public Label(int x, int y, Theme theme) {
        this.x = x;
        this.y = y;
        this.styleFocus = theme.focus();
        this.styleHover = theme.hover();
        this.styleNormal = theme.normal();
        isFocus = false;
    }

    public Label(int x, int y) {
        this.x = x;
        this.y = y;
        this.styleFocus = Theme.DEFAULT_THEME.focus();
        this.styleHover = Theme.DEFAULT_THEME.hover();
        this.styleNormal = Theme.DEFAULT_THEME.normal();
        isFocus = false;
    }

    //設定focus狀態樣式
    public void setStyleFocus(Style styleFocus) {
        this.styleFocus = styleFocus;
    }

    //設定hover狀態樣式
    public void setStyleHover(Style styleHover) {
        this.styleHover = styleHover;
    }

    //設定Normal狀態樣式
    public void setStyleNormal(Style styleNormal) {
        this.styleNormal = styleNormal;
    }

    //設定物件為focus狀態
    public void isFocus() {
        this.isFocus = true;
    }

    //解除物件focus狀態
    public void unFocus() {
        this.isFocus = false;
    }

    //取得物件當前focus狀態
    public boolean getIsFocus() {
        return this.isFocus;
    }

    //設定物件為hover狀態
    public void isHover() {
        this.isHover = true;
    }

    //解除物件hover狀態
    public void unHover() {
        this.isHover = false;
    }

    public Style getPaintStyle() {
        if (isFocus && this.styleFocus != null) {
            return this.styleFocus;
        } else if (isHover && this.styleHover != null) {
            return this.styleHover;
        } else {
            return this.styleNormal;
        }
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int width() {
        return getPaintStyle().getStyleWidth();
    }

    public int height() {
        return getPaintStyle().getStyleHeight();
    }

    public int left() {
        return getX();
    }

    public int right() {
        return getX() + width();
    }

    public int top() {
        return getY();
    }

    public int bottom() {
        return getY() + height();
    }

    public boolean getIsHover() {
        return this.isHover;
    }

    @Override
    public void paint(Graphics g) {
        if (getPaintStyle() != null) {
            getPaintStyle().paintComponent(g, x, y);
        }
    }

    @Override
    public void update() {

    }

}
