package com.dtstep.lighthouse.common.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

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

}
