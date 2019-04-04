package com.bbrozyna.caseweek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AndroidWrapperRuntime {
    private Runtime runtime;
    private String ADB_PATH = Paths.get(System.getProperty("user.dir"), "adb", "adb.exe").toString();


    AndroidWrapperRuntime(){
        this.runtime = Runtime.getRuntime();
    }

    private Process executeCommand(String command) throws IOException {
        return runtime.exec(command);
    }

    private BufferedReader executeCommandInADB(String command) throws IOException {
        String fullCommand = ADB_PATH + " " + command;

        Process pr = executeCommand(fullCommand);
        InputStream inputStream = pr.getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private void executeCommandInADBAndWait(String command) throws IOException {
        String fullCommand = ADB_PATH + " " + command;
        Process pr = executeCommand(fullCommand);
        try {
            pr.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    ArrayList<Integer> getScreenSize(){
        String resolution;
        ArrayList<Integer> dimensions = new ArrayList<>();
        try {
           BufferedReader responseFromADB = executeCommandInADB("shell dumpsys window | grep mBounds");
           if ((resolution=responseFromADB.readLine()) != null){
               Pattern p = Pattern.compile("\\[.*\\]\\s*\\[(.*)\\]");
               Matcher m = p.matcher(resolution);
               if(m.find()){
                   String[] result = m.group(1).split(",");
                   for(String dimension : result){
                       dimensions.add(Integer.valueOf(dimension
                       ));
                   }
                   return dimensions;
               }


           }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    ArrayList<String> getDevices(){
        ArrayList<String> devices = new ArrayList<>();
        String deviceInfo;

        try {
            BufferedReader responseFromADB = executeCommandInADB("devices");

            while((deviceInfo=responseFromADB.readLine()) != null) {
                if(!(deviceInfo.contains("List") || deviceInfo.isEmpty()))
            devices.add(deviceInfo);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return devices;
    }

    String getVersion() throws IOException{
        return executeCommandInADB("version").readLine();
    }

    void replaceDumpFile() throws IOException {
        executeCommandInADB("shell uiautomator dump /sdcard/dump.xml");
        executeCommandInADB("pull /sdcard/dump.xml");
    }

    void replaceScreenshot() throws IOException{
        executeCommandInADBAndWait("shell screencap /sdcard/screen.png");
        executeCommandInADBAndWait("pull /sdcard/screen.png");
    }


    void touch(long x, long y) throws IOException {
        String command  = String.format("%s %d %d","shell input tap", x, y);
        System.out.println(command);
        executeCommandInADBAndWait(command);
    }

    public static void main(String[] args){
        AndroidWrapperRuntime awr = new AndroidWrapperRuntime();
        try {
            awr.replaceScreenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("dupa");
    }

    }
