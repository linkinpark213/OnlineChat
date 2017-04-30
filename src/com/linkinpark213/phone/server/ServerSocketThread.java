package com.linkinpark213.phone.server;

import com.linkinpark213.phone.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by ooo on 2017/4/30 0030.
 */
public class ServerSocketThread extends Thread {
    private Socket socket;
    private ArrayList<Socket> clientList;
    private boolean keepAlive;

    public ServerSocketThread(Socket socket, ArrayList<Socket> clientList) {
        this.socket = socket;
        this.clientList = clientList;
        this.keepAlive = true;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void broadcast(int type, String content) {
        for (Socket socket : clientList) {
            if (socket != this.socket) {
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(new Message(type, content));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void messageIncoming(Message message, ObjectOutputStream outputStream) {
        try {
            switch (message.getType()) {
                case Message.SIGN_OUT:
                    System.out.println("Chatter " + socket.getRemoteSocketAddress() + " Left.");
                    outputStream.writeObject(new Message(Message.SIGN_OUT_GRANT, ""));
                    broadcast(Message.BROADCAST, "Chatter " + socket.getRemoteSocketAddress() + " Left.");
                    setKeepAlive(false);
                    break;
                case Message.CHAT:
                    System.out.println("Chatter " + socket.getRemoteSocketAddress() + " Sent: " + message.getContent());
                    broadcast(Message.BROADCAST, "> " + socket.getRemoteSocketAddress() + ": " + message.getContent());
                    break;
                default:
                    System.out.println("Invalid Request From " + socket.getRemoteSocketAddress());
                    outputStream.writeObject(new Message(Message.INVALID_MESSAGE, ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new Message(Message.SIGN_IN_GRANT, ""));
            broadcast(Message.BROADCAST, "Chatter: " + socket.getRemoteSocketAddress() + " Joined.");
            System.out.println("New chatter: " + socket.getRemoteSocketAddress());
            while (isKeepAlive()) {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) inputStream.readObject();
                messageIncoming(message, outputStream);
            }
            System.out.println("Connection Closing...");
            clientList.remove(socket);
            socket.close();
            System.out.println("Connection Closed.");
        } catch (SocketException e) {
            System.out.println("Connection Lost.");
            clientList.remove(socket);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
