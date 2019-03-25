package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;

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

    String getUI() throws IOException {
        executeCommandInADB("shell uiautomator dump /sdcard/dump.xml");
        executeCommandInADB("cat /sdcard/dump.xml");
        return "";
    }

    public static void main(String[] args){
        AndroidWrapperRuntime awr = new AndroidWrapperRuntime();
        try {
            System.out.println(awr.getVersion());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO decide where to trycatch

    //TODO get the proper values and types

}
