package com.dtstep.lighthouse.insights.config;
/*
 * Copyright (C) 2022-2025 XueLing.雪灵
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
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class RepeatableRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RepeatableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = getRequestBodyBytes(request);
    }

    private byte[] getRequestBodyBytes(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try (InputStream inputStream = request.getInputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
        }
        return result.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStreamWrapper(body);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new BufferedReader(new InputStreamReader(inputStream, getCharacterEncoding()));
    }

    private static class ServletInputStreamWrapper extends ServletInputStream {
        private final ByteArrayInputStream inputStream;

        public ServletInputStreamWrapper(byte[] data) {
            this.inputStream = new ByteArrayInputStream(data);
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() {
            return inputStream.read();
        }

        @Override
        public int available() throws IOException {
            return inputStream.available();
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }

        @Override
        public synchronized void reset() {
            inputStream.reset();
        }
    }
}