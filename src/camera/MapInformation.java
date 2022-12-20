package camera;


import gameObjects.Rect;

/**
 *
 * @author user
 */
public class MapInformation {
    
    private static Rect mapInfo;
    
    public static Rect mapInfo(){
        return mapInfo;
    }
    
    public static void setMapInfo(int left, int top, int right, int bottom){
        mapInfo = new Rect(left, top, right, bottom);
    }
    
}
