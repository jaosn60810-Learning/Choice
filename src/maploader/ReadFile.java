package maploader;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class ReadFile {

    public ArrayList<String[]> readFile(String path) throws IOException {
        ArrayList<String> tmp = new ArrayList();
        String pathname = MapLoader.class.getResource(path).getFile();
        pathname = java.net.URLDecoder.decode(pathname, "utf-8");
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while ((line = br.readLine()) != null) {
            tmp.add(line);
        }
        ArrayList<String[]> filterArr = new ArrayList();

        tmp.forEach(new Consumer() {
            @Override
            public void accept(Object a) {
                String[] tmp = new String[4];
                tmp[0] = ((String) a).split(",")[0];
                tmp[1] = ((String) a).split(",")[1];
                tmp[2] = ((String) a).split(",")[3];
                tmp[3] = ((String) a).split(",")[4];
                filterArr.add(tmp);
            }
        });
        return filterArr;
    }
}
