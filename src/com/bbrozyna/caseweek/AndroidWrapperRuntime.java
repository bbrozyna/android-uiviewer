package com.bbrozyna.caseweek;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AndroidWrapperRuntime {
    //  todo JUnit tests?
    private Runtime runtime;
    private String ADB_PATH = Paths.get(System.getProperty("user.dir"), "adb", "adb.exe").toString();


    AndroidWrapperRuntime(){
        this.runtime = Runtime.getRuntime();
    }

    private BufferedReader executeCommandInADB(String command) throws IOException {
        String fullCommand = ADB_PATH + " " + command;

        Process pr = runtime.exec(fullCommand);
        return new BufferedReader(new InputStreamReader(pr.getInputStream()));
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

    public void replaceDumpFile() throws IOException {
        executeCommandInADB("shell uiautomator dump /sdcard/dump.xml");
        executeCommandInADB("pull /sdcard/dump.xml");
    }

    void touch(long x, long y) throws IOException {
        String command  = String.format("%s %d %d","shell input tap", x, y);
        executeCommandInADB(command);
    }

    public static void main(String[] args){
        AndroidWrapperRuntime awr = new AndroidWrapperRuntime();
        try {
            awr.replaceDumpFile();
            System.out.println(awr.getDevices().get(0));
            for (int i : awr.getScreenSize()){
                System.out.println(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }
