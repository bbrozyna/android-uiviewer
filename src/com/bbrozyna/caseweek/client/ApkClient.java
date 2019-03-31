package com.bbrozyna.caseweek.client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ApkClient {

    public static String getDumpFromAPK(String address, int port) {
        String data = "";
        try ( Socket socket = new Socket(address, port)){
            DataInputStream input = new DataInputStream(socket.getInputStream());
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            data = input.readUTF();
            System.out.println(data);
            output.write("OK\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
      return data;
    }
}
