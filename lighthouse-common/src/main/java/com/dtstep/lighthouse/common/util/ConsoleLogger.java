package com.dtstep.lighthouse.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ConsoleLogger {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleLogger.class);

    public static void init() throws Exception {
        LoggingOutputStream infoOutputStream = new LoggingOutputStream(logger, LoggingOutputStream.LogLevel.INFO);
        PrintStream infoPrintStream = new PrintStream(infoOutputStream,true);
        System.setOut(infoPrintStream);
        LoggingOutputStream errorOutputStream = new LoggingOutputStream(logger, LoggingOutputStream.LogLevel.ERROR);
        PrintStream errorPrintStream = new PrintStream(errorOutputStream,true);
        System.setErr(errorPrintStream);
    }
}
