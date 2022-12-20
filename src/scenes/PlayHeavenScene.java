package scenes;

import camera.Camera;
import camera.MapInformation;
import controllers.AudioResourceController;
import controllers.ImageResourceController;
import controllers.SceneController;
import gameObjects.*;
import maploader.MapInfo;
import maploader.MapLoader;
import utils.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.Global.*;

public class PlayHeavenScene extends Scene {

    private ArrayList<Player> players; // 玩家(在這個場景裡New一個新的玩家陣列)
    private ArrayList<Brick> bricks; // 磚塊
    private int gravity; // 地圖的重力
    private ArrayList<Heart> foods; // 食物
    private ArrayList<Skill> skills; // 技能
    private ArrayList<Monster> monsters; // 怪物

    // 背景
    private Image img;

    // 計分
    private HeartAnimator HeartCount; // 畫出右上角的計算生命/愛心

    // 時間
    private double beginTime; // 每進入一次本場景，就用System.nanoTime()抓時間起始點。離開本場景(PlayScene)則釋放掉
    private boolean isFinish;
    private Delay finishDelay;
    private Image imgFinish;
    private double currentTime; // 每進入一次本場景的update，就用System.nanoTime()重抓一次時間起始點
    private static double passedTime; // update持續時不斷更新
    private double deadTimeCode; // 玩家死亡時的System.nanoTime()
    private double liveTime; // 玩家每次復活都重算

    // 鏡頭
    private Camera camera;

    // 重生點
    private ArrayList<RebornPoint> rebornPoints;
    private int maxRebornX; // 最遠重生點 X
    private int maxRebornY; // 最遠重生點 Y

    public PlayHeavenScene(ArrayList<Player> inputPlayers) {
        players = new ArrayList<>();
        this.players.add(new Player(PlayerAnimator.ActorType.PLAYER_ONE, MAP_WIDTH - 400, 720 * 32 - 32 * 10));
        players.get(0).setHeartCount(inputPlayers.get(0).getHeartCount());
        players.get(0).setMonsterCount(inputPlayers.get(0).getMonsterCount());
        players.get(0).setEventCount(inputPlayers.get(0).getEventCount());
        players.get(0).setEndingPoint(inputPlayers.get(0).getEndingPoint());
        this.maxRebornY = MAP_HEIGHT;
    }

    @Override
    public void sceneBegin() {
        // 音樂播放
        AudioResourceController.getInstance().loop(new Path().sound().bgm().bgmPlayHeaven(), 99);

        // 時間
        beginTime = System.nanoTime();
        // 切換場景的 delay 時間
        finishDelay = new Delay(30);
        finishDelay.stop();
        deadTimeCode = beginTime;

        // 地圖
        MapInformation.setMapInfo(0, 0, MAP_WIDTH, MAP_HEIGHT); // left, top, right, bottom

        // 背景
        img = SceneController.instance().irc().tryGetImage(new Path().image().backgrounds().backgroundHeaven());

        // 重力
        gravity = 2;

        // 建構復活點
        rebornPoints = new ArrayList<>();

        // 所有磚塊陣列
        bricks = new ArrayList<>();

        // 建構生命/愛心
        foods = new ArrayList<>();

        // 建構計算生命/愛心的圖
        HeartCount = new HeartAnimator(HeartAnimator.FoodType.HeartCount);

        // 建構怪物
        monsters = new ArrayList<>();

        // 建構技能發射物
        skills = new ArrayList<>();

        // 鏡頭
        camera = new Camera(MAP_WIDTH, MAP_HEIGHT, monsters, players);
        camera.setTarget(players.get(0));
        camera.setChaseY(10);

        // 地圖初始化
        mapInitialize();

    }

    @Override
    public void sceneEnd() {
        this.bricks = null;
        this.HeartCount = null;
        this.monsters = null;
        this.rebornPoints = null;
        this.skills = null;
        ImageResourceController.instance().clear();
        AudioResourceController.getInstance().stop(new Path().sound().bgm().bgmPlayHeaven());
    }

    @Override
    public void paint(Graphics g) {
        // 鏡頭開始
        camera.startCamera(g);

        // 繪製背景
        int backgroundWidth = 1280;
        int backgroundHeight = 1471; // 1280
        for (int i = -1; i < (MAP_HEIGHT / backgroundHeight) + 1; i++) {
            g.drawImage(img, 0, backgroundHeight * i, backgroundWidth, backgroundHeight, null);
        }

        // 畫鏡頭
        camera.paint(g);

        // 繪製食物
        for (int i = 0; i < foods.size(); i++) {
            if (camera.isCollision(foods.get(i))) {
                // 在鏡頭範圍中才進行繪製食物
                foods.get(i).paint(g);
            }
        }

        // 繪製重生點
        for (int j = 0; j < rebornPoints.size(); j++) {
            if (camera.isCollision(rebornPoints.get(j))) {
                //在鏡頭範圍中繪製重生點
                rebornPoints.get(j).paint(g);
            }
        }

        // 繪製磚塊
        for (int i = 0; i < bricks.size(); i++) {
            if (camera.isCollision(bricks.get(i))) {
                // 在鏡頭範圍中才進行繪製磚塊
                bricks.get(i).paint(g);
            }
        }

        // 繪製角色
        for (int i = 0; i < players.size(); i++) {
            if (camera.isCollision(players.get(i))) {
                // 在鏡頭範圍中才繪製角色
                players.get(i).paint(g);
            }
        }

        // 繪製技能
        for (int j = 0; j < skills.size(); j++) {
            if (camera.isCollision(skills.get(j))) {
                // 在鏡頭範圍中繪製技能
                skills.get(j).paint(g);
            }
        }

        // 繪製怪獸
        for (int j = 0; j < monsters.size(); j++) {
            if (camera.isCollision(monsters.get(j))) {
                // 在鏡頭範圍中才進行繪製怪物
                monsters.get(j).paint(g);
            }
        }

        // 鏡頭結束。要放在繪製生命/愛心和怪物擊殺數量前面 不然死掉後位置會跑掉
        camera.endCamera(g);

        //繪製生命/愛心獲得數量圖片
        for (int i = 0; i < players.get(0).getHeartCount(); i++) {
            HeartCount.paint(SCREEN_X - 48 - i * 48, HEARTCOUNT_Y, SCREEN_X - i * 48, HEARTCOUNT_Y + 48, g);
        }
    }

    @Override
    public void update() {

        /**時間更新*/
        currentTime = System.nanoTime();
        passedTime = ((double) (int) (((currentTime - beginTime) / 1000000000) * 10) / 10);


        if (players.get(0).playerAnimator().State() == PlayerAnimator.State.DEAD) {
            deadTimeCode = System.nanoTime();//玩家死亡時的時間點
        }
        if (players.get(0).isHaveBeenDead()) {
            liveTime = ((double) (int) (((currentTime - deadTimeCode) / 1000000000) * 10) / 10);//玩家死亡後過了多久
            players.get(0).setPlayerLiveTime(liveTime);
        }
        if (finishDelay.count()) {
            SceneController.instance().change(new EventScene(players));
        }

        camera.update();

        //磚塊更新
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            brick.update();
            //如果磚塊的vanish是ture就讓它消失
            if (brick.getVanish()) {
                bricks.remove(i--);
            }
        }

        //怪物更新
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            monster.update();

            // 如果玩家死掉重生位置 在地水之下
            if (monster.getMonsterType() == MonsterAnimator.MonsterType.WATERLINE) {
                // 如果重生點離下方地水 大於 玩家的高度 * 2
                if (monster.collider().centerY() <= maxRebornY) {
                    // 復活位置改成 X 隨機 , Y 是 地水的 Y
                    players.get(0).setRebornPoint(random(0, SCREEN_X), monster.collider().centerY() - ACTOR_HEIGHT * 8);
                }
            }

            // 會射東西的怪物
            if (monster.getMonsterType() == MonsterAnimator.MonsterType.DRAGON_LR
                    || monster.getMonsterType() == MonsterAnimator.MonsterType.PINKELF_LR
                    || monster.getMonsterType() == MonsterAnimator.MonsterType.DEMON_LR) {
                // 射擊的頻率
                if (Global.random(1, 100) >= 99) {
                    // 如果怪物朝左邊的射擊方式
                    if (monster.getDirection() == Direction.LEFT) {
                        // 新增怪物攻擊(往左平行飛)
                        monsters.add(new Monster(monster.collider().left(), monster.collider().top(),
                                UNIT_X, UNIT_Y,
                                MonsterAnimator.MonsterType.MONSTER_SHOOT, new Vector(-7, 0), new Delay(360), Direction.LEFT));
                        // 新增怪物攻擊(往左下飛)
                        monsters.add(new Monster(monster.collider().left(), monster.collider().top(),
                                UNIT_X, UNIT_Y,
                                MonsterAnimator.MonsterType.MONSTER_SHOOT, new Vector(-7, 1), new Delay(360), Direction.LEFT));
                        // 新增怪物攻擊(往左上飛)
                        monsters.add(new Monster(monster.collider().left(), monster.collider().top(),
                                UNIT_X, UNIT_Y,
                                MonsterAnimator.MonsterType.MONSTER_SHOOT, new Vector(-7, -1), new Delay(360), Direction.LEFT));
                    }
                    // 如果怪物朝右邊的射擊方式
                    else {
                        // 新增怪物攻擊(往右平行飛)
                        monsters.add(new Monster(monster.collider().right(), monster.collider().top(),
                                UNIT_X, UNIT_Y,
                                MonsterAnimator.MonsterType.MONSTER_SHOOT, new Vector(5, 0), new Delay(360), Direction.RIGHT));
                        // 新增怪物攻擊(往右下飛)
                        monsters.add(new Monster(monster.collider().right(), monster.collider().top(),
                                UNIT_X, UNIT_Y,
                                MonsterAnimator.MonsterType.MONSTER_SHOOT, new Vector(5, 1), new Delay(360), Direction.RIGHT));
                        // 新增怪物攻擊(往右上飛)
                        monsters.add(new Monster(monster.collider().right(), monster.collider().top(),
                                UNIT_X, UNIT_Y,
                                MonsterAnimator.MonsterType.MONSTER_SHOOT, new Vector(5, -1), new Delay(360), Direction.RIGHT));
                    }
                }
            }

            // 在地上行走的怪物
            if (monster.getMonsterType() == MonsterAnimator.MonsterType.SLIME_LR || monster.getMonsterType() == MonsterAnimator.MonsterType.GOBLIN_LR) {
                //改變怪物 Y speed
                monsterFallSpeed(monster);
                monster.setCanMove(true);
                //用預判物件判斷角色站在磚塊上
                monsterTouchBrickY(bricks, monster);
                // 怪物狀態是可以移動
                if (monster.getCanMove()) {
                    // 怪物向下掉
                    monster.fall();
                }
            }

            // 如果是射出物
            if (monster.getMonsterType() == MonsterAnimator.MonsterType.MONSTER_SHOOT) {
                // 如果射出物狀態是爆炸 或 是可以被移除的狀態 或 超出螢幕
                if (monsters.get(i).getState() != Monster.State.NORMAL || monster.getRemove() || monster.outOfScreen()) {
                    // 移除射出務
                    monsters.remove(i--);
                }
                // 若射出物碰到磚塊
                for (int n = 0; n < bricks.size(); n++) {
                    // 如果射出物碰到磚塊
                    if (monster.isCollision(bricks.get(n)) && monster.getMonsterType() == MonsterAnimator.MonsterType.MONSTER_SHOOT) {
                        // 將射出物狀態改為爆炸
                        monster.setState(Monster.State.ATTACK);
                        break;
                    }
                }
            }
        }

        // 生命/愛心更新
        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).update();
        }

        //重生點更新
        for (int i = 0; i < rebornPoints.size(); i++) {
            rebornPoints.get(i).update();
        }

        // 技能更新
        for (int i = 0; i < skills.size(); i++) {
            skills.get(i).update();
            skillHitMonster(monsters, skills.get(i));
            if (skills.get(i).isRemove()) {
                skills.remove(i--);
            }
        }

        // 判斷角色動作
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            if (Global.IS_DEBUG) {
                System.out.println("player" + i + " " + player.playerAnimator().State() + " " + player.collider().centerX() + " " + player.collider().centerY());
            }
            //更新角色
            player.update();
            if (player.playerAnimator().State() != PlayerAnimator.State.DEAD) {
                //改變角色 Y speed
                playerFallSpeed(player);
                player.setCanMove(true);
                //用預判物件判斷角色站在磚塊上
                playerTouchBrickY(bricks, player);
                if (player.getCanMove()) {
                    //角色向下掉
                    player.fall();
                    if (player.playerAnimator().State() != PlayerAnimator.State.JUMP) {
                        player.setState(PlayerAnimator.State.JUMP);
                    }
                    player.setJumping(true);
                }
                player.setCanMove(true);
                //判斷角色是否撞牆
                playerTouchBrickX(bricks, player);
                if (player.getCanMove()) {
                    //角色移動
                    player.move();
                }
                //判斷角色是否碰到怪物
                playerHitMonster(monsters, player);
                //判斷吃生命/愛心
                playerTouchFood(player, foods);
                //判斷重置重生點
                playerSetRebornPoint(rebornPoints, player);
                // 判斷玩家是否在地水之下
                killPlayerIfBeyondWaterLine(monsters, player);

                // 判斷玩家是否碰到最上端
                if (player.collider().top() <= 0 + ACTOR_HEIGHT) {
                    isFinish = true;
                    finishDelay.play();
                    break;
                }

                // 是否加速
                if (players.get(0).getEndingPoint() > 0) {
                    player.effect(2);
                } else {
                    player.effect(1);
                }
            }
        }

        // 生命/愛心閃爍
        HeartCount.update();
    }

    //建構地圖
    private void mapInitialize() {
        try {
            ArrayList<MapInfo> mapInfo = new MapLoader("genMapHeaven.bmp", "genMapHeaven.txt").combineInfo();
            for (MapInfo tmp : mapInfo) {  // 地圖產生器內物件的 {左, 上, 寬, 高}
                switch (tmp.getName()) {
                    // 磚塊 Type 記得依照不同 Scene 來調整
                    // 會動 brick 左右動
                    case "brickMoveLR":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X,
                                BrickAnimator.BrickType.BRICK_HEAVEN_MOVE_LR, new Vector(3, 0), new Delay(150)));
                        break;
                    // 會動 brick 上下動
                    case "brickMoveUD":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X,
                                BrickAnimator.BrickType.BRICK_HEAVEN_MOVE_UD, new Vector(0, 2), new Delay(200)));
                        break;
                    // 會動 brick 斜動
                    case "brickMoveSlash":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X,
                                BrickAnimator.BrickType.BRICK_HEAVEN_MOVE_SLASH, new Vector(2, 2), new Delay(200)));
                        break;
                    // 踩過會消失
                    case "brickVanish":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                BrickAnimator.BrickType.BRICK_HEAVEN_VANISH, new Delay(15)));
                        break;
                    // 固定的磚塊
                    case "brickFix":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BRICK_HEAVEN_FIX));
                        break;
                    // 水柱
                    case "brickSpring":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + 20,
                                UNIT_X * 2, UNIT_Y * 2,
                                BrickAnimator.BrickType.BRICK_SPRING));
                        break;
                    // 終點線
                    case "brickEnding":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BRICK_ENDING));
                        break;
                    // 向左箭頭
                    case "brickSpeedUpLeft":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BRICK_SPEED_UP_LEFT));
                        break;
                    // 加速箭頭
                    case "brickSpeedUpRight":
                        bricks.add(new Brick(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2,
                                UNIT_X, UNIT_Y,
                                BrickAnimator.BrickType.BRICK_SPEED_UP_RIGHT));
                        break;
                    case "foodHpPlusOne":
                        foods.add(new Heart(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2 + 5,
                                UNIT_X, UNIT_Y, HeartAnimator.FoodType.FOOD_HP_PLUS_ONE));
                        break;
                    case "foodHpPlusTwo":
                        foods.add(new Heart(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2 + 2,
                                UNIT_X, UNIT_Y, HeartAnimator.FoodType.FOOD_HP_PLUS_TWO));
                        break;
                    case "foodHpPlusThree":
                        foods.add(new Heart(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2 + 2,
                                UNIT_X, UNIT_Y, HeartAnimator.FoodType.FOOD_HP_PLUS_THREE));
                        break;
                    case "foodHpPlusFour":
                        foods.add(new Heart(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2 + 2,
                                UNIT_X, UNIT_Y, HeartAnimator.FoodType.FOOD_HP_PLUS_FOUR));
                        break;
                    case "foodHpPlusFive":
                        foods.add(new Heart(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2 + 2,
                                UNIT_X, UNIT_Y, HeartAnimator.FoodType.FOOD_HP_PLUS_FIVE));
                        break;
                    // 往上飛(地水)
                    case "monsterWaterLine":
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 2, UNIT_Y * 2,
                                MonsterAnimator.MonsterType.WATERLINE, new Vector(0, 1), new Delay(100), players));
                        break;
                    // 在磚上左右短距離移動 ( 7 個單位), 怪物的種類圖片顯示請自行調整
                    case "monsterMoveShort":
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 2, UNIT_Y * 2,
                                MonsterAnimator.MonsterType.SLIME_LR, new Vector(-2, 0), new Delay(100)));
                        break;
                    // 在磚上左右長距離移動 ( 19 個單位), 怪物的種類圖片顯示請自行調整
                    case "monsterMoveLong":
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 2, UNIT_Y * 2,
                                MonsterAnimator.MonsterType.GOBLIN_LR, new Vector(-4, 0), new Delay(50)));
                        break;
                    // 在空中左右移動 ( 30 個單位), 怪物的種類圖片顯示請自行調整
                    case "monsterFlyLR":
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 2, UNIT_Y * 2,
                                MonsterAnimator.MonsterType.GREENELF_LR, new Vector(-3, 0), new Delay(300)));
                        break;
                    // 在空中上下移動 ( 19 個單位), 怪物的種類圖片顯示請自行調整
                    case "monsterFlyUD":
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 4,
                                UNIT_X * 2, UNIT_Y * 2,
                                MonsterAnimator.MonsterType.BAT_UD, new Vector(0, 3), new Delay(100)));
                        break;
                    // 在空中左右移動 9 個單位並會射擊的怪物
                    case "monsterFlyLRShoot":
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() * 3 / 2,
                                UNIT_X * tmp.getSizeX() * 3, UNIT_Y * tmp.getSizeY() * 3,
                                MonsterAnimator.MonsterType.PINKELF_LR, new Vector(2, 0), new Delay(120)));
                        break;
                    // 會向上的尖刺
                    case "monsterSpikes":
                        monsters.add(new Monster(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2 + 10,
                                UNIT_X * tmp.getSizeX(), UNIT_Y * tmp.getSizeY(),
                                MonsterAnimator.MonsterType.SPIKES_FIXED, new Vector(0, 0), new Delay(120)));
                        break;
                    // 重生點
                    case "rebornPoint":
                        rebornPoints.add(new RebornPoint(UNIT_X * tmp.getX() + UNIT_X * tmp.getSizeX() / 2,
                                UNIT_Y * tmp.getY() + UNIT_Y * tmp.getSizeY() / 2 - UNIT_Y,
                                UNIT_X * tmp.getSizeX() * 2, UNIT_Y * tmp.getSizeY() * 4));
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MainScene.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // 改變角色 y 方向速度
    private void playerFallSpeed(Player player) {
        // 當往上的時候
        if (player.getYSpeed() < 0) {
            // 上升速度越慢
            player.setYSpeed((int) (player.getYSpeed() * 0.9));
            // 正在跳的狀態
            player.setJumping(true);
        }
        // 當往下的時候
        if (player.getYSpeed() >= 0) {
            // 重力加速度
            player.setYSpeed(player.getYSpeed() + gravity);
        }
    }

    // 判斷 Y 軸碰撞
    private void playerTouchBrickY(ArrayList<Brick> bricks, Player player) {
        // 建立預判物件
        Player tmp = new Player(player.collider().centerX(), player.collider().centerY());
        // 用預判物件判斷 y 方向的移動會不會碰到障礙
        tmp.translateY(player.getYSpeed());
        // 判斷不同種的磚塊與角色的互動關係
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            // 如果碰到磚塊
            if (tmp.isCollision(brick)) {
                // 從上往下撞
                if (tmp.collider().bottom() > brick.collider().top()
                        && tmp.collider().top() < brick.collider().top()) {
                    // 角色的底是磚塊得的上方
                    player.changeBottom(brick.collider().top());
                    // 碰到地板將 isjumping 設為 false
                    player.setJumping(false);
                    // y 方向速度設為 0
                    player.setYSpeed(0);
                    //因為預判物件碰撞到磚塊，將本體可移動設為 false
                    player.setCanMove(false);
                    // 設定方塊加速度
                    player.setBrickSpeed(brick.getBrickSpeed());

                    // 如果是會移動磚塊
                    if (brick.getBrickType() == BrickAnimator.BrickType.BRICK_HEAVEN_MOVE_LR
                            || brick.getBrickType() == BrickAnimator.BrickType.BRICK_HEAVEN_MOVE_UD
                            || brick.getBrickType() == BrickAnimator.BrickType.BRICK_HEAVEN_MOVE_SLASH) {
                        // 站在會動的平台上速度會跟平台一樣
                        if (player.playerAnimator().State() != PlayerAnimator.State.WALK
                                && player.playerAnimator().State() != PlayerAnimator.State.DEAD) {
                            // 沒有按方向鍵時給速度
                            if (player.getSpeed().vx() == 0) {
                                // 設定玩家速度
                                player.setSpeed(brick.getVector());

                                // 如果玩家狀態是蹲下
                                if (player.isSquating()) {
                                    // 設成蹲下圖片
                                    player.playerAnimator().setState(PlayerAnimator.State.SQUAT);
                                } else {
                                    // 設成停止圖片
                                    player.playerAnimator().setState(PlayerAnimator.State.STOP);
                                }
                            }
                        }
                        break;
                    }

                    // 如果是踩到會消失的磚塊會讓磚塊的消失 delay 開始跑
                    if (brick.getBrickType() == BrickAnimator.BrickType.BRICK_HEAVEN_VANISH) {
                        // 還沒被踩過為 false
                        if (!brick.getVanish()) {
                            //用 delay 算時間時間到就會將 vanish 設為true
                            brick.getDelay().loop();
                        }
                        break;
                    }

                    // 切換場景
                    if (brick.getBrickType() == BrickAnimator.BrickType.BRICK_ENDING) {
                        isFinish = true;
                        finishDelay.play();
                        break;
                    }

                    // 如果是踩到彈簧往上跳 40
                    if (brick.getBrickType() == BrickAnimator.BrickType.BRICK_SPRING) {
                        // 彈簧聲音
                        AudioResourceController.getInstance().shot(new Path().sound().effects().effectBrickSpring());
                        // 玩家跳躍速度
                        player.jump(40);
                        break;
                    }

                    // 設定踩到磚塊時的狀態
                    if (player.playerAnimator().State() != PlayerAnimator.State.WALK
                            && player.playerAnimator().State() != PlayerAnimator.State.STOP
                            && player.playerAnimator().State() != PlayerAnimator.State.SQUAT
                            && player.playerAnimator().State() != PlayerAnimator.State.DEAD) {
                        // 沒按方向的時候 STOP
                        if (player.getSpeed().vx() == 0 && !player.isSquating()) {
                            player.setState(PlayerAnimator.State.STOP);
                        } else {
                            player.setState(PlayerAnimator.State.WALK);
                        }
                    }
                    break;
                }

                // 從下往上撞到磚塊
                else if (tmp.collider().top() < brick.collider().bottom()
                        && tmp.collider().bottom() > brick.collider().bottom()) {
                    //設定 top 為磚塊 bottom + 1
                    player.changeTop(brick.collider().bottom() + 1);
                    //角色往下移動 10
                    player.translateY(10);
                    // y 方向設為 0
                    player.setYSpeed(0);
                    // 因為預判物件碰撞到磚塊，將本體可移動設為 false
                    player.setCanMove(false);
                    break;
                }
            }
        }
    }

    private void playerTouchMonsterY(ArrayList<Monster> monsters, Player player) {
        // 建立預判物件
        Player tmp = new Player(player.collider().centerX(), player.collider().centerY());
        // 用預判物件判斷 y 方向的移動會不會碰到障礙
        tmp.translateY(player.getYSpeed());//YSpeed = 重力
        // 判斷不同種的磚塊與角色的互動關係
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            // 如果碰到地水
            if (tmp.isCollision(monster) && monster.getMonsterType() == MonsterAnimator.MonsterType.WATERLINE) {
                if (tmp.collider().bottom() > monster.collider().top()
                        && tmp.collider().top() > monster.collider().top()) {
                    //設定 top 為磚塊 bottom + 1
                    player.changeBottom(monster.collider().top() - 1);
                    //角色往上移動 10
                    player.translateY(10 * -1);
                    // y 方向設為 0 >>>應該是設重力為0吧???避免加速度向下???
                    player.setYSpeed(0);
                    // 因為預判物件碰撞到磚塊，將本體可移動設為 false >> 不能操控的意思???
                    player.setCanMove(false);
                    break;
                }
            }
        }
    }

    // 判斷x軸碰撞
    private void playerTouchBrickX(ArrayList<Brick> bricks, Player player) {
        // 創建預判物件
        Player tmp = new Player(player.collider().centerX(), player.collider().centerY());
        // 預判物件移動x方向速度
        tmp.translateX((int) player.getSpeed().vx());

        // 判斷不同種的磚塊與角色的互動關係
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            // 如果不是角色下面撞到磚塊上面或角色上面撞到磚塊下面
            if (tmp.isCollision(brick)
                    && tmp.collider().bottom() != brick.collider().top()
                    && tmp.collider().top() != brick.collider().bottom()) {
                // 撞到磚塊左邊
                if (tmp.collider().right() > brick.collider().left()
                        && tmp.collider().left() < brick.collider().left()
                        && !player.isSquating()
                        && player.playerAnimator().State() != PlayerAnimator.State.SQUAT) {
                    // 改變本體 right 為磚塊 left - 1
                    player.changeRight(brick.collider().left() - 1);
                    // 因為預判物件碰到磚塊，將本體可移動設為 false
                    player.setCanMove(false);
                    break;
                }
                // 撞到磚塊右邊
                else if (tmp.collider().left() < brick.collider().right()
                        && tmp.collider().right() > brick.collider().right()
                        && !player.isSquating()
                        && player.playerAnimator().State() != PlayerAnimator.State.SQUAT) {
                    // 改變本體 left 為磚塊 right + 1
                    player.changeLeft(brick.collider().right() + 1);
                    // 因為預判物件碰到磚塊，將本體可移動設為 false
                    player.setCanMove(false);
                }
                break;
            }
        }
    }

    // 玩家碰到食物會補血
    private void playerTouchFood(Player player, ArrayList<Heart> foods) {
        for (int i = 0; i < foods.size(); i++) {
            Heart food = foods.get(i);

            if (player.isCollision(foods.get(i))) {
                AudioResourceController.getInstance().shot(new Path().sound().effects().effectPlayerEat());
                player.effect(4);
                foods.remove(i--);
                // 如果超過 10 滴血 不能再補
                if (player.getHeartCount() + food.getHeartNumber() >= 10) {
                    player.setHeartCount(10);
                } else {
                    player.setHeartCount(player.getHeartCount() + food.getHeartNumber());
                }
                break;
            }
        }
    }

    // 玩家技能攻擊怪物
    private void playerShotFire(Player player, SkillAnimator.SkillType skillType) {
        if (player.getDirection() == Direction.RIGHT) {
            skills.add(new Skill(player.collider().centerX() + 20,
                    player.collider().centerY(),
                    64, 64, skillType, player.getDirection(), new Delay(120)));
        } else {
            skills.add(new Skill(player.collider().centerX() - 20,
                    player.collider().centerY(),
                    64, 64, skillType, player.getDirection(), new Delay(120)));
        }
        player.setCanShot(false);
        player.getPlayerShootDelay().play();
    }

    //改變怪物 y 方向速度
    private void monsterFallSpeed(Monster monster) {
        if (monster.getYSpeed() < 0) {
            monster.setYSpeed((int) (monster.getYSpeed() * 0.9));
        }
        if (monster.getYSpeed() >= 0) {
            monster.setYSpeed(monster.getYSpeed() + gravity);
        }
    }

    // 判斷怪物 Y 軸碰撞
    private void monsterTouchBrickY(ArrayList<Brick> bricks, Monster monster) {
        // 判斷 y 方向的移動會不會碰到障礙
        monster.translateY(monster.getYSpeed());
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            if (monster.isCollision(brick)) {
                // 如果從上往下撞
                if (monster.collider().bottom() > brick.collider().top()
                        && monster.collider().top() < brick.collider().top()) {
                    // 怪物的下面是磚塊的上面
                    monster.changeBottom(brick.collider().top());
                    // y 方向速度設為 0
                    monster.setYSpeed(0);
                    // 因為預判物件碰撞到磚塊，將本體可移動設為 false
                    monster.setCanMove(false);
                }
                break;
            }
        }
    }

    // 玩家撞到怪物
    private void playerHitMonster(ArrayList<Monster> monsters, Player player) {//碰到怪
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            if (player.isCollision(monster)) {
                if (!IS_DEBUG && !player.invincible()) {
                    AudioResourceController.getInstance().play(new Path().sound().effects().effectPlayerHurt());
                    // 碰到怪物扣血
                    player.setHeartCount(player.getHeartCount() - 1);
                    // 玩家可以有效果在身上
                    player.setCanGetEffect(true);
                    // 被怪物碰到會有一段無敵時間
                    player.effect(3);
                    // 玩家不可以有效果在身上
                    player.setCanGetEffect(false);
                    // 效果持續時間
                    player.getSkillDelay().loop();
                    // 如果由上往下碰到地水 會被推回上面
                    if (monster.getMonsterType() == MonsterAnimator.MonsterType.WATERLINE) {
                        playerTouchMonsterY(monsters, player);
                    }

                    break;

                } else if (monster.getMonsterType() == MonsterAnimator.MonsterType.WATERLINE) {
                    playerTouchMonsterY(monsters, player);
                    break;
                }
            }
        }
    }

    // 玩家在地水之下
    private void killPlayerIfBeyondWaterLine(ArrayList<Monster> monsters, Player player) {//碰到怪
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            if (monster.getMonsterType() == MonsterAnimator.MonsterType.WATERLINE) {
                if (monster.collider().bottom() < player.collider().top())
                    // 在地水之下直接死
                    player.setHeartCount(0);
            }
        }
    }

    // 技能射出物打到怪獸
    private void skillHitMonster(ArrayList<Monster> monsters, Skill skill) { // 火球攻擊對象
        for (int i = 0; i < this.monsters.size(); i++) {
            if (monsters.get(i).isCollision(skill)
                    && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.SPIKES_FIXED // 刺不能被招式打掉
                    && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.WATERLINE) { // 地水不能被招式打掉
                if (monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.MONSTER_SHOOT) {
                    skill.setRemove(true);
                }
                AudioResourceController.getInstance().play(new Path().sound().effects().effectMonsterDie());
                if (skill.getSkillAnimator().getSkillType() == SkillAnimator.SkillType.PLAYER_ONE_SHOOT
                        && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.MONSTER_SHOOT) {
                    players.get(0).setMonsterCount(players.get(0).getMonsterCount() + 1);
                }
                if (skill.getSkillAnimator().getSkillType() == SkillAnimator.SkillType.FIRE
                        && monsters.get(i).getMonsterType() != MonsterAnimator.MonsterType.MONSTER_SHOOT) {
                    players.get(1).setMonsterCount(players.get(1).getMonsterCount() + 1);
                }
                monsters.remove(i--);
                break;
            }
        }
    }

    // 設定重生點
    private void playerSetRebornPoint(ArrayList<RebornPoint> rebornPoints, Player player) {
        for (int i = 0; i < rebornPoints.size(); i++) {
            // 超過重生點就設定重生點
            if (player.collider().top() < rebornPoints.get(i).collider().top()) {
                // 設定重生點
                player.setRebornPoint(maxRebornX, maxRebornY + 100);
                // 改變重生點
                rebornPoints.get(i).getRebornPointAnimator().setType(RebornPointAnimator.RebornPointType.TOUCHED);
                // 將經過的重生點中 最上面的 記錄起來
                if (rebornPoints.get(i).collider().centerY() <= maxRebornY) {
                    maxRebornY = rebornPoints.get(i).collider().centerY();
                    maxRebornX = rebornPoints.get(i).collider().centerX();
                }
            }
        }
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return null;
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (commandCode / 5 == 0) {
                    if (commandCode == P1_SHOT
                            && players.get(0).canShot()
                            && !players.get(0).isSquating()
                            && players.get(0).playerAnimator().State() != PlayerAnimator.State.DEAD) {
                        playerShotFire(players.get(0), SkillAnimator.SkillType.PLAYER_ONE_SHOOT);
                        AudioResourceController.getInstance().play(new Path().sound().effects().effectPlayerShoot());
                    } else {
                        players.get(0).keyPressed(commandCode, trigTime);
                    }
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (commandCode / 5 == 0) {
                    players.get(0).keyReleased(commandCode, trigTime);
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {

            }
        };
    }
}
