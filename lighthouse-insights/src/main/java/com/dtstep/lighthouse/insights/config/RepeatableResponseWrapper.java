package com.dtstep.lighthouse.insights.config;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class RepeatableResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedOutputStream = new ByteArrayOutputStream();
    private PrintWriter cachedWriter;
    private ServletOutputStream cachedServletOutputStream;

    public RepeatableResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (cachedServletOutputStream == null) {
            cachedServletOutputStream = new CachedServletOutputStream(cachedOutputStream);
        }
        return cachedServletOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (cachedWriter == null) {
            cachedWriter = new PrintWriter(cachedOutputStream);
        }
        return cachedWriter;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (cachedWriter != null) {
            cachedWriter.flush();
        }
        if (cachedServletOutputStream != null) {
            cachedServletOutputStream.flush();
        }
        super.flushBuffer();
    }

    public byte[] getContentAsByteArray() throws IOException {
        flushBuffer();
        return cachedOutputStream.toByteArray();
    }

    private static class CachedServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream buffer;

        public CachedServletOutputStream(ByteArrayOutputStream buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(int b) throws IOException {
            buffer.write(b);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(javax.servlet.WriteListener listener) {
        }
    }
}

