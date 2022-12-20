package controllers;

import utils.Global;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

// 單例模式 Singleton
public class ImageResourceController {
    private static class KeyPair {
        private String path;
        private Image img;

        public KeyPair(String path, Image img) {
            this.path = path;
            this.img = img;
        }
    }

    // 單例
    private static ImageResourceController irc;

    // 內容
    private ArrayList<KeyPair> imgPairs;

    public ImageResourceController() {
        imgPairs = new ArrayList<>();
    }

    public static ImageResourceController instance() {
        if (irc == null) {
            irc = new ImageResourceController();
        }
        return irc;
    }

    public Image tryGetImage(String path) {
        KeyPair pair = findKeyPair(path);
        if (pair == null) {
            // 找不到, 創建並回傳
            return addImage(path);
        }
        // 找到, 直接回傳
        return pair.img;
    }

    private Image addImage(String path) {
        try {
            if (Global.IS_DEBUG) {
                System.out.println("load img from: " + path);
            }
            Image img = ImageIO.read(getClass().getResource(path));
            imgPairs.add(new KeyPair(path, img));
            return img;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyPair findKeyPair(String path) {
        for (int i = 0; i < imgPairs.size(); i++) {
            KeyPair pair = imgPairs.get(i);
            if (pair.path.equals(path)) {
                return pair;
            }
        }
        return null;
    }

    public void clear() {
        imgPairs.clear();
    }
}
