package utils;

public class Path {

    public static abstract class Flow {

        private String path;

        public Flow(String path) {
            this.path = path;
        }

        public Flow(Flow flow, String path) {
            this.path = flow.path + path;
        }

        @Override
        public String toString() {
            return path;
        }
    }

    private static class Resources extends Flow {
        private Resources() {
            super("/resources");
        }
    }

    public static class Images extends Flow {
        private Images() {
            super(new Resources(), "/images");
        }

        public static class Backgrounds extends Flow {
            private Backgrounds(Flow flow) {
                super(flow, "/backgrounds");
            }

            public String backgroundOpening() {
                return this + "/backgroundOpening.png";
            }

            public String backgroundLobby() {
                return this + "/backgroundLobby.png";
            }

            public String backgroundHeaven() {
                return this + "/backgroundHeaven.png";
            }

            public String backgroundHell() {
                return this + "/backgroundHell.png";
            }

            public String backgroundUniverse() {
                return this + "/backgroundUniverse.png";
            }

            public String backgroundEvent() {
                return this + "/backgroundEvent.png";
            }

            public String backgroundBillboard() {
                return this + "/backgroundBillboard.png";
            }

            public String backgroundEndingDown() {
                return this + "/backgroundEndingDown.png";
            }

            public String backgroundEndingNeutral() {
                return this + "/backgroundEndingNeutral.png";
            }

            public String backgroundEndingUp() {
                return this + "/backgroundEndingUp.png";
            }
        }

        public static class Effects extends Flow {

            private Effects(Flow flow) {
                super(flow, "/effects");
            }

            public String invincible() {
                return this + "/invincible.png";
            }

            public String speedUp() {
                return this + "/speedUp.png";
            }

            public String speedDown() {
                return this + "/speedDown.png";
            }
        }

        public static class Events extends Flow {

            private Events(Flow flow) {
                super(flow, "/events");
            }

            public String eventItem01() {
                return this + "/eventItem01.png";
            }

            public String eventItem02() {
                return this + "/eventItem02.png";
            }

            public String eventItem03() {
                return this + "/eventItem03.png";
            }

            public String eventItem04() {
                return this + "/eventItem04.png";
            }

            public String eventItem05() {
                return this + "/eventItem05.png";
            }

            public String eventItem06() {
                return this + "/eventItem06.png";
            }

            public String eventItem07() {
                return this + "/eventItem07.png";
            }

            public String eventItem08() {
                return this + "/eventItem08.png";
            }

            public String eventItem09() {
                return this + "/eventItem09.png";
            }

            public String eventItem10() {
                return this + "/eventItem10.png";
            }
        }

        public static class Food extends Flow {
            private Food(Flow flow) {
                super(flow, "/food");
            }

            public String foodHpPlusOne() {
                return this + "/foodHpPlusOne.png";
            }

            public String foodHpPlusTwo() {
                return this + "/foodHpPlusTwo.png";
            }

            public String foodHpPlusThree() {
                return this + "/foodHpPlusThree.png";
            }

            public String foodHpPlusFour() {
                return this + "/foodHpPlusFour.png";
            }

            public String foodHpPlusFive() {
                return this + "/foodHpPlusFive.png";
            }

        }

        public static class Monsters extends Flow {
            private Monsters(Flow flow) {
                super(flow, "/monsters");
            }

            public String monsterBatUD() {
                return this + "/monsterBatUD.png";
            }

            public String monsterBatLR() {
                return this + "/monsterBatLR.png";
            }

            public String monsterDemonUD() {
                return this + "/monsterDemonUD.png";
            }

            public String monsterDemonLR() {
                return this + "/monsterDemonLR.png";
            }

            public String monsterDragonUD() {
                return this + "/monsterDragonUD.png";
            }

            public String monsterDragonLR() {
                return this + "/monsterDragonLR.png";
            }

            public String monsterFireLine() {
                return this + "/monsterFireLine.png";
            }

            public String monsterWaterLine() {
                return this + "/monsterWaterLine.png";
            }

            public String monsterGhostUD() {
                return this + "/monsterGhostUD.png";
            }

            public String monsterGhostLR() {
                return this + "/monsterGhostLR.png";
            }

            public String monsterGoblinUD() {
                return this + "/monsterGoblinUD.png";
            }

            public String monsterGoblinLR() {
                return this + "/monsterGoblinLR.png";
            }

            public String monsterGreenElfUD() {
                return this + "/monsterGreenElfUD.png";
            }

            public String monsterGreenElfLR() {
                return this + "/monsterGreenElfLR.png";
            }

            public String monsterMinotaurUD() {
                return this + "/monsterMinotaurUD.png";
            }

            public String monsterMinotaurLR() {
                return this + "/monsterMinotaurLR.png";
            }

            public String monsterPinkElfUD() {
                return this + "/monsterPinkElfUD.png";
            }

            public String monsterPinkElfLR() {
                return this + "/monsterPinkElfLR.png";
            }

            public String monsterSkeletonUD() {
                return this + "/monsterSkeletonUD.png";
            }

            public String monsterSkeletonLR() {
                return this + "/monsterSkeletonLR.png";
            }

            public String monsterSlimeUD() {
                return this + "/monsterSlimeUD.png";
            }

            public String monsterSlimeLR() {
                return this + "/monsterSlimeLR.png";
            }

            public String monsterSpikes() {
                return this + "/monsterSpikes.png";
            }

            public String monsterShoot() {
                return this + "/monsterShoot.png";
            }

            public String monsterShootExplode() {
                return this + "/monsterShootExplode.png";
            }
        }

        public static class Objs extends Flow {
            private Objs(Flow flow) {
                super(flow, "/objs");
            }

            // 遊戲內物件 obj
            // 三種地圖共通物件
            public String objBrickSpeedUpLeft() {
                return this + "/objBrickSpeedUpLeft.png";
            } // 向左加速

            public String objBrickSpeedUpRight() {
                return this + "/objBrickSpeedUpRight.png";
            } // 向右加速

            public String objBrickSpring() {
                return this + "/objBrickSpring.png";
            } // 彈跳物件

            // 初始大廳物件
            public String objBrickLobbyDrop() {
                return this + "/objBrickLobbyDrop.png";
            } // 會消失的磚塊

            public String objBrickLobbyFixed() {
                return this + "/objBrickLobbyFixed.png";
            } // 固定的磚塊

            public String objBrickLobbyLRMove() {
                return this + "/objBrickLobbyLRMove.png";
            } // 左右移動的磚塊

            public String objBrickLobbySlashMove() {
                return this + "/objBrickLobbySlashMove.png";
            } // 斜向移動的磚塊

            public String objBrickLobbyUDMove() {
                return this + "/objBrickLobbyUDMove.png";
            } // 上下移動的磚塊

            // 天堂物件
            public String objBrickHeavenDrop() {
                return this + "/objBrickHeavenDrop.png";
            }

            public String objBrickHeavenFixed() {
                return this + "/objBrickHeavenFixed.png";
            }

            public String objBrickHeavenLRMove() {
                return this + "/objBrickHeavenLRMove.png";
            }

            public String objBrickHeavenSlashMove() {
                return this + "/objBrickHeavenSlashMove.png";
            }

            public String objBrickHeavenUDMove() {
                return this + "/objBrickHeavenUDMove.png";
            }

            // 地獄物件
            public String objBrickHellDrop() {
                return this + "/objBrickHellDrop.png";
            }

            public String objBrickHellFixed() {
                return this + "/objBrickHellFixed.png";
            }

            public String objBrickHellLRMove() {
                return this + "/objBrickHellLRMove.png";
            }

            public String objBrickHellSlashMove() {
                return this + "/objBrickHellSlashMove.png";
            }

            public String objBrickHellUDMove() {
                return this + "/objBrickHellUDMove.png";
            }

            // 事件物件
            public String objBrickEventFixed() {
                return this + "/objBrickEventFixed.png";
            }

            // 血量
            public String heartCount() {
                return this + "/objHeart.png";
            }

            // 食物
            public String FOOD() {
                return this + "/FOOD.png";
            }

            // 重生點
            public String rebornPointOne() {
                return this + "/rebornPointOne.png";
            }

            public String rebornPointTwo() {
                return this + "/rebornPointTwo.png";
            }

        }

        public static class Players extends Flow {

            private Players(Flow flow) {
                super(flow, "/players");
            }

            public String playerOneRun() {
                return this + "/playerOneRun.png";
            }

            public String playerOneJump() {
                return this + "/playerOneJump.png";
            }

            public String playerOneStop() {
                return this + "/playerOneStop.png";
            }

            public String playerOneSquat() {
                return this + "/playerOneSquat.png";
            }

            public String playerOneDead() {
                return this + "/playerOneDead.png";
            }

            public String playerOneShoot() {
                return this + "/playerOneShoot.png";
            }

            public String playerOneDemo() {
                return this + "/playerOneDemo.png";
            }

            public String playerTwoRun() {
                return this + "/playerTwoRun.png";
            }

            public String playerTwoJump() {
                return this + "/playerTwoJump.png";
            }

            public String playerTwoStop() {
                return this + "/playerTwoStop.png";
            }

            public String playerTwoSquat() {
                return this + "/playerTwoSquat.png";
            }

            public String playerTwoDead() {
                return this + "/playerTwoDead.png";
            }

            public String playerTwoShoot() {
                return this + "/playerTwoShoot.png";
            }

            public String playerTwoDemo() {
                return this + "/playerTwoDemo.png";
            }

        }

        public Backgrounds backgrounds() {
            return new Backgrounds(this);
        }

        public Effects effects() {
            return new Effects(this);
        }

        public Events events() {
            return new Events(this);
        }

        public Food foods() {
            return new Food(this);
        }

        public Monsters monsters() {
            return new Monsters(this);
        }

        public Objs objs() {
            return new Objs(this);
        }

        public Players players() {
            return new Players(this);
        }
    }

    public static class Sounds extends Flow {
        private Sounds() {
            super(new Resources(), "/sounds");
        }

        public static class Bgm extends Flow {
            private Bgm(Flow flow) {
                super(flow, "/bgm");
            }

            public String bgmOpening() {
                return this + "/bgmOpening.wav";
            }

            public String bgmEvent() {
                return this + "/bgmEvent.wav";
            }

            public String bgmPlayLobby() {
                return this + "/bgmPlayLobby.wav";
            }

            public String bgmPlayHeaven() {
                return this + "/bgmPlayHeaven.wav";
            }

            public String bgmPlayHell() {
                return this + "/bgmPlayHell.wav";
            }

            public String bgmEnding() {
                return this + "/bgmEnding.wav";
            }

        }

        public static class Effects extends Flow {
            private Effects(Flow flow) {
                super(flow, "/effects");
            }

            public String effectPlayerShoot() {
                return this + "/effectPlayerShoot.wav";
            }

            public String effectPlayerDie() {
                return this + "/effectPlayerDie.wav";
            }

            public String effectMonsterShoot() {
                return this + "/effectMonsterShoot.wav";
            }

            public String effectMonsterDie() {
                return this + "/effectMonsterDie.wav";
            }

            public String effectBrickSpring() {
                return this + "/effectBrickSpring.wav";
            }

            public String effectPlayerEat() {
                return this + "/effectPlayerEat.wav";
            }

            public String effectPlayerHurt() {
                return this + "/effectPlayerHurt.wav";
            }

            public String effectPlayerJump() {
                return this + "/effectPlayerJump.wav";
            }
        }

        public Bgm bgm() {
            return new Bgm(this);
        }

        public Effects effects() {
            return new Effects(this);
        }
    }

    public Images image() {
        return new Images();
    }

    public Sounds sound() {
        return new Sounds();
    }
}
