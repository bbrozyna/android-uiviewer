package com.bbrozyna.caseweek;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.bbrozyna.caseweek.client.ApkClient;

public class AndroidWrapperRuntime {
    private Thread instrumentationThread;

    AndroidWrapperRuntime() throws IOException {
    	if (getDevices().isEmpty()) {
    		throw new IOException("No Devices");
    	}
		ApkClient.installAPKs();
		forwardADBPort();
        startInstrumentationServer();
    }
    
    private void forwardADBPort() {
    	try {
			ADBExecutor.executeCommandInADBAndWait("forward tcp:65432 tcp:65432");
			System.out.println("Forwarded");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private void startInstrumentationServer() throws IOException {
    	String command = "shell am instrument -w -r   -e debug false -e class 'com.screenaccessibilityreader.ApplicationTest#testAutomation' com.screenaccessibilityreader.test/android.support.test.runner.AndroidJUnitRunner";
    	instrumentationThread = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("Starting instrumentation");
					ADBExecutor.executeCommandInADBAndWait(command);
				} catch (IOException e) {
					return;
				}
			}
		});
    	instrumentationThread.start();
    }
    
    ArrayList<Integer> getScreenSize(){
        String resolution;
        ArrayList<Integer> dimensions = new ArrayList<>();
        try {
           BufferedReader responseFromADB = ADBExecutor.executeCommandInADB("shell dumpsys window | grep mBounds");
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
            BufferedReader responseFromADB = ADBExecutor.executeCommandInADB("devices");

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
        return ADBExecutor.executeCommandInADB("version").readLine();
    }

    void replaceDumpFile() throws IOException {

		byte[] data = ApkClient.readData("dump");
		try (PrintWriter out = new PrintWriter("dump.xml")) {
		    out.println(new String(data));
		}
    }

    void replaceScreenshot() throws IOException{
		byte[] data = ApkClient.readData("screenshot");
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
        ImageIO.write(img, "jpg", new File("screen.jpg"));
    }

    void touch(long x, long y) throws IOException {
        String command  = String.format("%s %d %d","shell input tap", x, y);
        System.out.println(command);
        ADBExecutor.executeCommandInADBAndWait(command);
    }

    public String getOperatorName(){
        // todo 2. Using getprop, find the property and parse the values
        return "";
    }

    public void swipe(){
        // todo 3. Use swipe to go from home to app tray
    }

    public boolean installAPK(){
        // todo 4. Install apk using a method
        return false;
    }

    public static void main(String[] args){
        try {
        	AndroidWrapperRuntime awr = new AndroidWrapperRuntime();
            awr.getVersion();
            System.out.println(awr.getOperatorName());  // todo use getprop and parse the response
            awr.swipe();  // todo what arguments it needs?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   }
