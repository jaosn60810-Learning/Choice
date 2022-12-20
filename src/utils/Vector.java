package utils;

/**
 * @author LSYu
 */
public class Vector {

    private double x;
    private double y;

    public Vector() {
        this(0, 0);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector) {
        this(vector.x, vector.y);
    }

    public final double getRadian() {
        return Math.atan2(y, x);
    }

    public final double getAngle() {
        return getRadian() / Math.PI * 180;
    }

    public final double length() {
        return Math.sqrt(lengthSqaure());
    }

    public final double lengthSqaure() {
        return x * x + y * y;
    }

    public void setVx(double x) {
        this.x = x;
    }

    public void setVy(double y) {
        this.y = y;
    }

    public final Vector zero() {
        this.x = this.y = 0;
        return this;
    }

    public final boolean isZero() {
        return x == 0 && y == 0;
    }

    public final Vector setLength(double value) {
        double angle = getAngle();
        x = Math.cos(angle) * value;
        y = Math.sin(angle) * value;
        return this;
    }

    public final Vector normalize() {
        double length = length();
        x = x / length;
        y = y / length;
        return this;
    }

    public final boolean isNormalized() {
        return length() == 1.0;
    }

    public Vector reverse() {
        x = -x;
        y = -y;
        return this;
    }

    // 求兩向量的dot product
    public double dotP(Vector v) {
        return x * v.x + y * v.y;
    }

    // 求兩向量的cross product
    public double crossP(Vector v) {
        return x * v.y - y * v.x;
    }

    // 求兩向量間夾角
    public static double radianBetween(Vector v1, Vector v2) {
        if (!v1.isNormalized()) {
            v1 = new Vector(v1).normalize(); // |v1| = 1
        }
        if (!v2.isNormalized()) {
            v2 = new Vector(v2).normalize(); // |v2| = 1
        }
        return Math.acos(v1.dotP(v2));
    }

    public double vx() {
        return x;
    }

    public double vy() {
        return y;
    }

    public void changeY(double vy) {
        this.y = y + vy;
    }

    public void changeX(double vx) {
        this.y = y + vx;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector multiply(double value) {
        return new Vector(x * value, y * value);
    }

    public Vector divide(double value) {
        return new Vector(x / value, y / value);
    }
}
