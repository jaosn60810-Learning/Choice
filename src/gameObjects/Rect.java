package gameObjects;

public class Rect {
    private int left;
    private int top;
    private int right;
    private int bottom;

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public Rect(Rect rect){
        this.left = rect.left;
        this.top = rect.top;
        this.right = rect.right;
        this.bottom = rect.bottom;
    }

    public static Rect genWithCenter(int x, int y, int width, int height){
        int left = x - width / 2;
        int right = left + width;
        int top = y - height / 2;
        int bottom = top + height;
        return new Rect(left, top, right, bottom);
    }

    public final boolean overlap(int left, int top, int right, int bottom){
        if (this.left() > right) {
            return false;
        }
        if (this.right() < left) {
            return false;
        }
        if (this.top() > bottom) {
            return false;
        }
        if (this.bottom() < top) {
            return false;
        }
        return true;
    }

    public final boolean overlap(Rect b){
        return overlap(b.left, b.top, b.right, b.bottom);
    }

    public int centerX(){
        return (left + right) / 2;
    }
    public int centerY(){
        return (top + bottom) / 2;
    }
    public float exactCenterX(){
        return (left + right) / 2f;
    }
    public float exactCenterY(){
        return (top + bottom) / 2f;
    }

    public final Rect translate(int dx, int dy){
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
        return this;
    }
    public final Rect translateX(int dx){
        this.left += dx;
        this.right += dx;
        return this;
    }
    public final Rect translateY(int dy){
        this.top += dy;
        this.bottom += dy;
        return this;
    }

    public int left() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int top() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int right() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int bottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int width(){
        return this.right - this.left;
    }

    public int height(){
        return this.bottom - this.top;
    }

    public final void setCenter(int x, int y){
        translate(x - centerX(), y - centerY());
    }
}
