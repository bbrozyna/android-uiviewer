package com.bbrozyna.caseweek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class ADBExecutor {
	private static String ADB_PATH = Paths.get(System.getProperty("user.dir"), "adb", "adb.exe").toString();
	
	private static Runtime getRuntime() {
		return Runtime.getRuntime();
	}
	
    private static Process executeCommand(String command) throws IOException {
        return getRuntime().exec(command);
    }

    public static BufferedReader executeCommandInADB(String command) throws IOException {
        String fullCommand = ADB_PATH + " " + command;

        Process pr = executeCommand(fullCommand);
        InputStream inputStream = pr.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static void executeCommandInADBAndWait(String command) throws IOException {
        String fullCommand = ADB_PATH + " " + command;
        Process pr = executeCommand(fullCommand);
        try {
            pr.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
