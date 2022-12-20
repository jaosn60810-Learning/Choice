package maploader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadBmp {

    public  ArrayList<int[][]> readBmp(String path) {
        ArrayList<int[][]> rgbArr = new ArrayList();
        BufferedImage bi = null;

        try {
            bi = ImageIO.read(getClass().getResource(path));
        } catch (IOException ex) {
            Logger.getLogger(ReadBmp.class.getName()).log(Level.SEVERE, null, ex);
        }

        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int[][] rgbContent = new int[2][];
                int pixel = bi.getRGB(i, j);
                rgbContent[0] = new int[]{i, j};
                rgbContent[1] = new int[]{pixel};
                rgbArr.add(rgbContent);
            }
        }
        return rgbArr;
    }
}
