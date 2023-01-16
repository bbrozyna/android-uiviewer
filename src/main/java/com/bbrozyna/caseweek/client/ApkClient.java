package com.bbrozyna.caseweek.client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import com.bbrozyna.caseweek.ADBExecutor;

public class ApkClient {

	public static void installAPKs() throws IOException {
		ADBExecutor.executeCommandInADBAndWait("push app-debug.apk /data/local/tmp/com.screenaccessibilityreader");
		ADBExecutor.executeCommandInADBAndWait("shell pm install -t -r /data/local/tmp/com.screenaccessibilityreader");
		ADBExecutor.executeCommandInADBAndWait("push app-debug-androidTest.apk /data/local/tmp/com.screenaccessibilityreader.test");
		ADBExecutor.executeCommandInADBAndWait("shell pm install -t -r /data/local/tmp/com.screenaccessibilityreader.test");
	}
	
	public static byte[] readData(String command) throws IOException {
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

}
