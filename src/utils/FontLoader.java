package utils;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

public class FontLoader {

    private static HashMap<String, Font> map = new HashMap<>();

    public static Font loadFont(String fontFileName, float fontSize) {
        String key = fontFileName + fontSize;
        if (map.containsKey(key)) {
            return map.get(key);
        }
        try {

            File file = new File(fontFileName);

            FileInputStream aixing = new FileInputStream(file);

            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, aixing);

            Font dynamicFontPt = dynamicFont.deriveFont(fontSize);

            aixing.close();

            map.put(key, dynamicFontPt);

            return dynamicFontPt;

        } catch (Exception e) {
            e.printStackTrace();
            return new java.awt.Font("宋體", Font.PLAIN, 14);

        }

    }

    public static java.awt.Font Font01(float size) {
        String root = System.getProperty("user.dir");
        Font font = FontLoader.loadFont(root + "/src/resources/Font/Font01.ttf", size);
        return font;
    }

    public static java.awt.Font Font02(float size) {
        String root = System.getProperty("user.dir");
        Font font = FontLoader.loadFont(root + "/src/resources/Font/Font02.ttf", size);
        return font;
    }

    public static java.awt.Font Font03(float size) {
        String root = System.getProperty("user.dir");
        Font font = FontLoader.loadFont(root + "/src/resources/Font/Font03.ttf", size);
        return font;
    }

    public static java.awt.Font Retro4(float size) {
        String root = System.getProperty("user.dir");
        Font font = FontLoader.loadFont(root + "/src/resources/Font/Retro4.ttf", size);
        return font;
    }

    public static java.awt.Font Retro5(float size) {
        String root = System.getProperty("user.dir");
        Font font = FontLoader.loadFont(root + "/src/resources/Font/Retro5.ttf", size);
        return font;
    }

    public static java.awt.Font Retro6(float size) {
        String root = System.getProperty("user.dir");
        Font font = FontLoader.loadFont(root + "/src/resources/Font/Retro6.ttf", size);
        return font;
    }

    public static java.awt.Font Retro7(float size) {
        String root = System.getProperty("user.dir");
        Font font = FontLoader.loadFont(root + "/src/resources/Font/Retro7.ttf", size);
        return font;
    }
}
