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

class AndroidWrapperRuntime {
    private Runtime runtime;
    private String ADB_PATH = Paths.get(System.getProperty("user.dir"), "adb", "adb.exe").toString();
    private Thread instrumentationThread;


    AndroidWrapperRuntime(){
        this.runtime = Runtime.getRuntime();
        forwardADBPort();
        startInstrumentationServer();
    }

    private void forwardADBPort() {
    	try {
			executeCommandInADBAndWait("forward tcp:65432 tcp:65432");
			System.out.println("Forwarded");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void startInstrumentationServer() {
    	String command = "shell am instrument -w -r   -e debug false -e class 'com.screenaccessibilityreader.ApplicationTest#testAutomation' com.screenaccessibilityreader.test/android.support.test.runner.AndroidJUnitRunner";
    	instrumentationThread = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println("Starting instrumentation");
					executeCommandInADBAndWait(command);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    	instrumentationThread.start();
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

	private static byte[] readData(String command) throws IOException {
		Socket socket = new Socket("localhost", 65432);
		System.out.println("Connected to socket");
		
		DataInputStream input = new DataInputStream(socket.getInputStream());
		BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
        output.write(command + "\n");
        output.flush();
        System.out.println("Sent command: " + command);
        
        int length = input.readInt();
        System.out.println("Got length: " + length);
        
        byte[] message = new byte[length];
        input.readFully(message, 0, message.length);
        System.out.println("Read message");
        
        output.write("OK\n");
        socket.close();
        return message;
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
		byte[] data = readData("dump");
		try (PrintWriter out = new PrintWriter("dump.xml")) {
		    out.println(new String(data));
		}
    }

    void replaceScreenshot() throws IOException{
		byte[] data = readData("screenshot");
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
        ImageIO.write(img, "jpg", new File("screen.jpg"));
    }

    void touch(long x, long y) throws IOException {
        String command  = String.format("%s %d %d","shell input tap", x, y);
        System.out.println(command);
        executeCommandInADBAndWait(command);
    }

    public static void main(String[] args){
        AndroidWrapperRuntime awr = new AndroidWrapperRuntime();
        try {
			System.out.println(new String(readData("dump")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    }
