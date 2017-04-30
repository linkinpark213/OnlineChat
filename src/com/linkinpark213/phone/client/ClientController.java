package com.linkinpark213.phone.client;

import com.linkinpark213.phone.common.Message;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;

public class ClientController {
    private Socket socket;
    private ClientSocketThread clientSocketThread;

    public ClientController(TextArea textArea) {
        try {
            socket = new Socket("127.0.0.1", 4213);
            clientSocketThread = new ClientSocketThread(socket, textArea);
            clientSocketThread.start();
        } catch (IOException e) {
            System.out.println("Connection Failed.");
            System.exit(0);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean chat(String message) {
        boolean status = false;
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new Message(Message.CHAT, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean signOut() {
        boolean status = true;
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new Message(Message.SIGN_OUT, ""));
            System.out.println("You Left Room.");
            clientSocketThread.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }
}
