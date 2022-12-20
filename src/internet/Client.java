package Internet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    private boolean isStop;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private String data;

    public static class Command {
        private int serialNum;
        private int commandCode;
        private ArrayList<String> strs;
        private String IP;

        public Command(int serialNum, int commandCode, String IP ,ArrayList<String> strs) {
            this.commandCode = commandCode;
            this.serialNum = serialNum;
            this.strs = strs;
            this.IP = IP;
        }

        public int getSerialNum() {
            return serialNum;
        }

        public int getCommandCode() {
            return commandCode;
        }

        public ArrayList<String> getStrs() {
            return strs;
        }
    }

    private static Client client;

    private Client() {
        this.isStop = false;
        this.data = "";
    }

    public static Client getInstance() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public String getIP() {
         return socket.getLocalAddress().toString();
    }

    public static String getLocalAddress() {
        return "127.0.0.1";
    }

    public void start(int port, String serverIP) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //建立連線指定Ip和埠的socket
                    socket = new Socket(serverIP, port);
                    //獲取系統標準輸入流
                    out = new PrintWriter(socket.getOutputStream());
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void connect() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isStop) {
                        String str = "";
                        str = in.readLine();// 逗號分隔字串
                        data = str;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void closeConnect() throws IOException {
        isStop = true;
        out.close();
        in.close();
        socket.close();

    }

    public void sendCommand(Command command) {
        StringBuilder input = new StringBuilder();
        input.append(command.serialNum);
        input.append(",");
        input.append(command.commandCode);
        input.append(",");
        input.append(command.IP);
        for (String string : command.strs) {
            input.append(",");
            input.append(string);
        }
        String msg = input.toString();
        out.println(msg);
        out.flush();
    }

    public Command getCommand() {
        String data = this.data;
        synchronized (data) {
            if (data.isEmpty()) {
                return new Command(9999, 9999, "",new ArrayList<>());  //若沒有資料，則傳出一個預設值
            }
            ArrayList<String> parsedData = parse(data);
            int serialNum = Integer.parseInt(parsedData.get(0));
            int commandCode = Integer.parseInt(parsedData.get(1));
            String IP = parsedData.get(2);
            parsedData.remove(0);
            parsedData.remove(0);
            parsedData.remove(0);
            return new Command(serialNum, commandCode, IP,parsedData);
        }
    }

    public static ArrayList<String> parse(String data) {
        String[] str = data.split(",");
        ArrayList<String> strList = new ArrayList<String>(Arrays.asList(str));
        return strList;
    }
}