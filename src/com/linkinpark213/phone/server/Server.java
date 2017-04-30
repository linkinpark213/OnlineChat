package com.linkinpark213.phone.server;

import com.linkinpark213.phone.common.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ooo on 2017/4/29 0029.
 */
public class Server {
    private int port;
    private ArrayList<Socket> clientList;

    public Server(int port) {
        this.port = port;
        clientList = new ArrayList<>();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Listening on Port " + serverSocket.getLocalPort());
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Connection Established With " + socket.getRemoteSocketAddress());
                    clientList.add(socket);
                    new ServerSocketThread(socket, clientList).start();
                } catch (SocketException e) {
                    System.out.println("Connection Lost.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(4213);
        server.run();
    }
}
