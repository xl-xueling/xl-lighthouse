package com.dtstep.lighthouse.common.util;

import java.io.IOException;
import java.net.*;

public class IpUtils {

    public static boolean checkIpPort(String ip, int port) throws Exception {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip,port),3000);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPortUsing(int port) {
        boolean flag = false;
        InetSocketAddress d = new InetSocketAddress(port);
        try(Socket socket = new Socket()) {
            socket.connect(d,3000);
            flag = true;
        } catch (IOException ignored) {}
        return flag;
    }
}
