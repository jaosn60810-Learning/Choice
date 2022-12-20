package camera;

import gameObjects.Monster;
import gameObjects.Player;
import utils.Global;
import gameObjects.GameObject;
import utils.CommandSolver;
import utils.MonsterAnimator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Camera extends GameObject implements CommandSolver.MouseCommandListener, CommandSolver.KeyCommandListener {

    private enum State {
        TARGET_MODE, // 鏡頭追焦物件
        MOUSE_MODE // 滑鼠控制鏡頭縮放
    }

    /**
     * 主要屬性
     */

    private final int imgWidth; // 背景圖寬度(最大的那張底圖)
    private final int imgHeight; // 背景圖高度(最大的那張底圖)

    //    private double chaseX; // 追蹤除數(x軸)
    private double chaseY; // 追蹤除數(y軸)

    private State cameraState; // 鏡頭控制狀態

    //    private GameObject target; // 主要追蹤對象
    private Player target; // 主要追蹤對象
    private ArrayList<Monster> targetDeadlines; // 次要追蹤對象(天火或地水)的陣列
    Monster monsterDeadLine; // 次要追蹤對象(天火或地水)


    /**
     * 次要屬性
     */
    private double multiple; // 放大倍率

    /**
     * 設定螢幕移動距離 與判定範圍
     */
    private int screenRange;
    private int screenStep;

    private int mouseX;
    private int mouseY;

//    /**
//     * 抓PlayScene的怪物來用
//     */
//    private ArrayList<Monster> monsters;
//    private Monster monsterFireLine;

    // 抓玩家
    private ArrayList<Player> players;

    /**
     * 預設鏡頭為全景
     */
    public Camera(int inputImageWidth, int inputImageHeight, ArrayList<Monster> monsters, ArrayList<Player> players) {
        super(0, 0, Global.SCREEN_X, Global.SCREEN_Y);
//        this.chaseX = 1; //追焦初始值
        this.chaseY = 1; //追焦初始值
        this.imgWidth = inputImageWidth;
        this.imgHeight = inputImageHeight;
        this.multiple = 1d;//預設初始倍率
        this.cameraState = State.TARGET_MODE;//預設初始狀態為鏡頭追焦物件模式
        this.mouseX = 0;
        this.mouseY = 0;
        this.targetDeadlines = monsters;// 抓怪獸
        this.players = players;// 抓玩家
    }

    /**
     * 可設定鏡頭大小
     */
    public Camera(int inputImageWidth, int inputImageHeight, int x, int y, int cameraWidth, int cameraHeight) {
        //需要兩個以上的鏡頭，可以設定該鏡頭的座標與寬高
        super(x, y, cameraWidth, cameraHeight);
//        this.chaseX = 1; //追焦初始值
        this.chaseY = 1; //追焦初始值
        this.imgWidth = inputImageWidth;
        this.imgHeight = inputImageHeight;
        this.cameraState = State.MOUSE_MODE;//預設初始狀態為滑鼠控制
        //放大功能
        this.multiple = 1d;//預設初始倍率
        //捲軸功能
        this.screenRange = 100;
        this.screenStep = 10;
        this.mouseX = 0;
        this.mouseY = 0;
    }

    /**
     * 主要追蹤角色
     */
    public void setTarget(Player inputTrackTarget) {
        this.target = inputTrackTarget;
        this.cameraState = State.TARGET_MODE;
    }

    /**
     * 次要追蹤角色
     */
    public void setTargetDeadline(ArrayList<Monster> inputTargetDeadlines) {
        this.targetDeadlines = inputTargetDeadlines;
//        this.cameraState = State.TARGET_MODE;
    }

    /**
     * 追蹤X&Y軸的速度除數
     */ //數字越大移動越慢
    public void setChase(int inputChaseX, int inputChaseY) {
//        this.chaseX = inputChaseX;
        this.chaseY = inputChaseY;
    }

    /** 追蹤X軸的速度除數 */ //數字越大移動越慢
//    public void setChaseX(int chaseX){
//        this.chaseX = chaseX;
//    }

    /**
     * 追蹤Y軸的速度除數
     */ //數字越大移動越慢
    public void setChaseY(int chaseY) {
        this.chaseY = chaseY;
    }


    private MonsterWho monsterWho;

    /**
     * 現在的怪獸是天火還是地水？
     */
    private enum MonsterWho {
        MONSTER_FIRELINE,
        MONSTER_WATERLINE
    }


    /**
     * 抓出天火或地水
     */
    public void findDeadLine() {
        for (int i = 0; i < targetDeadlines.size(); i++) {
            Monster monster = targetDeadlines.get(i);
            monster.update(); // 這行刪掉的話，畫面會晃
            if (monster.getMonsterType() == MonsterAnimator.MonsterType.FIRELINE) {
                monsterDeadLine = this.targetDeadlines.get(i);
                this.monsterWho = MonsterWho.MONSTER_FIRELINE;
            } else if (monster.getMonsterType() == MonsterAnimator.MonsterType.WATERLINE) {
                monsterDeadLine = this.targetDeadlines.get(i);
                this.monsterWho = MonsterWho.MONSTER_WATERLINE;
            }
        }
    }

    /**
     * 開始追焦目標物件
     */
    // 就會移動到鏡頭
    public void trackTarget() {

//        int dX = (int)((target.collider().centerX() - collider().centerX()) / chaseX);//鏡頭X軸移動距離
        int dY = (int) ((target.collider().centerY() - collider().centerY()) / chaseY);//鏡頭Ｙ軸移動距離

        //鏡頭初始時超出範圍修正
        if (collider().left() < 0) {
            translateX(-collider().left());
        }
        if (collider().right() > imgWidth) {
            translateX(imgWidth - collider().right());
        }
        if (collider().top() < 0) {
            translateY(-collider().top());
        }
        if (collider().bottom() > imgHeight) {
            translateY(imgHeight - collider().bottom());
        }

//        //鏡頭還沒碰到最左邊＆最右邊
//        if(collider().left() + dX >= 0 && collider().right() + dX <= imgWidth) {
//            translateX(dX);
//        }

        /**修改處*/
        //抓出天火或地水
        findDeadLine();

        // 雙人模式中 鏡頭只會追天火
        if (monsterWho == MonsterWho.MONSTER_FIRELINE && players.get(0).getId() == 99) {
            // 天火與鏡頭的距離
            int distanceFireLineToCamera = (monsterDeadLine.collider().top() - collider().top());
            // 玩家與鏡頭的距離
            int distancePlayerToCamera = collider().bottom() - target.collider().bottom();
            translateY(distanceFireLineToCamera);

        // 單人模式的鏡頭模式 (天火)
        } else if (monsterWho == MonsterWho.MONSTER_FIRELINE) {
            // 天火與鏡頭的距離
            int distanceFireLineToCamera = (monsterDeadLine.collider().top() - collider().top());
            // 玩家與鏡頭的距離
            int distancePlayerToCamera = collider().bottom() - target.collider().bottom();

            // 若天火(向)下得比鏡頭快(深)時(==看得到天火)，鏡頭只能隨著天火向下移動
            if (collider().top() <= monsterDeadLine.collider().top()) {
                translateY(distanceFireLineToCamera);//鏡頭必須跟隨天火
                // 但如果角色已經下去接近底部了，就要追焦回角色身上，且鏡頭只能往下移動
                if (distancePlayerToCamera < Global.ACTOR_HEIGHT * 2) {
                    translateY(Math.abs(dY));
                }
                //若是鏡頭(向)下得比天火快(深)時(==看不到天火)，鏡頭可以(隨著角色)任意上下改變
            } else if (collider().top() > monsterDeadLine.collider().top()) { //判斷式裡面不要有 =，畫面就不會晃(but why? XD)
                translateY(dY);
            }

        // 單人模式的鏡頭模式 (地水)
        } else if (monsterWho == MonsterWho.MONSTER_WATERLINE) {
            //地水與鏡頭的距離
            int distanceWaterLineToCamera;
            //玩家與鏡頭的距離
            int distancePlayerToCamera = target.collider().top() - collider().top();

            //鏡頭的底是地水的底
            changeBottom(monsterDeadLine.collider().bottom());
            distanceWaterLineToCamera = (collider().bottom() - monsterDeadLine.collider().bottom()); // 加正數鏡頭會下移 (+ 132)
            translateY(distanceWaterLineToCamera);
//            //若地水(向)上得比鏡頭快(高)時(==看得到地水)，鏡頭只能隨著地水向上移動
//            if (collider().bottom() > monsterDeadLine.collider().top()) {
//                translateY(distanceWaterLineToCamera);//鏡頭必須跟隨地水
//                //但如果角色已經上去接近天花板了，就要追焦回角色身上，且鏡頭只能往上移動
//                if (distancePlayerToCamera < Global.ACTOR_HEIGHT * 2) {
//                    translateY(Math.abs(dY) * -1);
//                }
//                //若是鏡頭(向)上得比地水快(高)時(==看不到地水)，鏡頭可以(隨著角色)任意上下改變
//            } else if (collider().bottom() < monsterDeadLine.collider().top()) { // 判斷式裡面不要有 =，畫面就不會晃(but why? XD)
//                translateY(dY);
//            }
        }
    }

    /**
     * 開始使用鏡頭
     */
    public void startCamera(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(multiple, multiple);
        g2d.translate(-painter().left(), -painter().top());
    }

    /**
     * 結束鏡頭
     */ //畫布回歸原始位置
    public void endCamera(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(painter().left(), painter().top());
    }

    /**
     * 每次移動鏡頭邏輯更新
     */
    @Override
    public void update() {
        if (cameraState == State.TARGET_MODE) {
            trackTarget(); // 追焦功能
        } else {
            moveScreen(mouseX, mouseY);
        }
    }

    @Override
    public void mouseTrig(MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (this.cameraState == State.MOUSE_MODE) {
            if (state == CommandSolver.MouseState.MOVED) {
                // 在MOUSE_MODE中，預設滑鼠在地圖四角移動，鏡頭朝該方向移動 -> 設定偵測範圍&移動距離
//                moveScreen(e);
                mouseX = e.getX();
                mouseY = e.getY();
            }
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        if (commandCode == KeyEvent.VK_Z) {
            //按下Z放大畫面
            multiple += 0.1;
        }
        if (commandCode == KeyEvent.VK_X) {
            //按下X縮小畫面
            multiple -= 0.1;
        }
        if (commandCode == KeyEvent.VK_A) {
            //按下A切換鏡頭追焦畫面
            if (cameraState == State.MOUSE_MODE) {
                this.cameraState = State.TARGET_MODE;
            } else {
                this.cameraState = State.MOUSE_MODE;
            }
        }
    }

    /**
     * 在Debug模式時，畫出鏡頭外框
     */
    @Override
    public void paintComponent(Graphics g) {
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(painter().left(), painter().top(), painter().width(), painter().height());
        }
    }

    /**
     * 設定螢幕移動距離 與判定範圍
     */
    public void setScreenMovingRange(int screenRange, int screenStep) {
        this.screenRange = screenRange;
        this.screenStep = screenStep;
    }

    /**
     * 移動螢幕
     */
    private void moveScreen(int inputX, int inputY) {
        int dX = 0;
        int dY = 0;
        if (inputX > Global.SCREEN_X - screenRange) {
            dX = screenStep;
        }
        if (inputX < screenRange) {
            dX = -screenStep;
        }
        if (inputY > Global.SCREEN_Y - screenRange) {
            dY = screenStep;
        }
        if (inputY < screenRange) {
            dY = -screenStep;
        }
        translate(dX, dY);

        if (collider().left() <= 0) {
            translateX(-dX);
        }
        if (collider().right() >= imgWidth) {
            translateX(-dX);
        }
        if (collider().top() <= 0) {
            translateY(-dY);
        }
        if (collider().bottom() >= imgHeight) {
            translateY(-dY);
        }
    }
}
