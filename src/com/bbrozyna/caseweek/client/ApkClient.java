package com.bbrozyna.caseweek.client;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ApkClient {

    // try to connect, if not, throw exception -> AlertBox
    public static void getDumpFromAPK(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String data = input.readUTF();
        System.out.println(data);
        output.write("OK\n");
        socket.close();
    }
}
