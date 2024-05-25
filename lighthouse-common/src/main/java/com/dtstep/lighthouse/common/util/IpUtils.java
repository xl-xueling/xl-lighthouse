package com.dtstep.lighthouse.common.util;
/*
 * Copyright (C) 2022-2024 XueLing.雪灵
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
