package com.dtstep.lighthouse.common.util;

import java.net.NetworkInterface;
import java.util.*;

public class SystemUtil {

    public static List<String> getMACAddress()throws Exception{
        List<String> macList = new ArrayList<>();
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            StringBuilder sb = new StringBuilder();
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            byte[] mac = netInterface.getHardwareAddress();
            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
            }
            if(sb.length() != 0){
                String temp = sb.toString().toLowerCase();
                if(!macList.contains(temp)){
                    macList.add(temp);
                }
            }
        }
        return macList;
    }
}
