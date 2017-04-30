package com.linkinpark213.phone.test;

import com.linkinpark213.phone.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ooo on 2017/4/29 0029.
 */
public class TestClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 4213);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(new Message(Message.SIGN_IN, "213"));
            Message response = (Message) inputStream.readObject();
            System.out.println(response.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
