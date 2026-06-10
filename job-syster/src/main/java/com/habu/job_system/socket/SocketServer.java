package com.habu.job_system.socket;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void start() throws Exception {

        ServerSocket server = new ServerSocket(9999);
        System.out.println("Socket server started...");

        while(true) {
            Socket client = server.accept();
            System.out.println("Client connected: " + client);
        }
    }
}