package menu;

import utils.CommandSolver;
import java.awt.event.MouseEvent;

public class MouseTriggerImpl {

    private static boolean ovalOverlap(Label obj, int eX, int eY) {
        int r = (int) (Math.sqrt(Math.pow(obj.width() / 2, 2) - Math.pow(obj.height() / 2, 2)));
        int r1X = obj.getX() + obj.width() / 2 - r;
        int r2X = obj.getX() + obj.width() / 2 + r;
        int rY = obj.getY() + obj.height() / 2;
        int threePointDistance = (int) (Math.sqrt(Math.pow(r1X - eX, 2) + Math.pow(rY - eY, 2)) + Math.sqrt(Math.pow(r2X - eX, 2) + Math.pow(rY - eY, 2)));
        return threePointDistance <= obj.width();
    }

    private static boolean rectOverlap(Label obj, int eX, int eY) {
        return eX <= obj.right() && eX >= obj.left() && eY >= obj.top() && eY <= obj.bottom();
    }

    public static void mouseTrig(Label obj, MouseEvent e, CommandSolver.MouseState state) {
        boolean isOval = (obj.getPaintStyle() instanceof Style.StyleOval);
        if (state == CommandSolver.MouseState.RELEASED && (obj instanceof Button)) {
            obj.unFocus();
        }
        if (state == CommandSolver.MouseState.MOVED) {
            if (isOval) {
                if (ovalOverlap(obj, e.getX(), e.getY())) {
                    obj.isHover();
                } else {
                    obj.unHover();
                }
            } else {
                if (rectOverlap(obj, e.getX(), e.getY())) {
                    obj.isHover();
                } else {
                    obj.unHover();
                }
            }
        }

        if (state == CommandSolver.MouseState.PRESSED) {
            if (isOval) {
                if (ovalOverlap(obj, e.getX(), e.getY())) {
                    obj.isFocus();
                    if (obj.getClickedAction() != null) {
                        obj.clickedActionPerformed();
                    }
                } else {
                    obj.unFocus();
                }
            } else {
                if (rectOverlap(obj, e.getX(), e.getY())) {
                    obj.isFocus();
                    if (obj.getClickedAction() != null) {
                        obj.clickedActionPerformed();
                    }
                } else {
                    obj.unFocus();
                }
            }
        }
    }
}
