package com.linkinpark213.phone.client;

import com.linkinpark213.phone.common.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by ooo on 2017/4/30 0030.
 */
public class ClientTest {
    public static void main() {
        try {
            Socket socket = new Socket("127.0.0.1", 4213);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) inputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
