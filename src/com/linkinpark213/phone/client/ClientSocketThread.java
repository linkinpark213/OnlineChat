package com.linkinpark213.phone.client;

import com.linkinpark213.phone.common.Message;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;

/**
 * Created by ooo on 2017/4/30 0030.
 */
public class ClientSocketThread extends Thread {
    private Socket socket;
    private TextArea chatWindow;
    private boolean keepAlive;

    public ClientSocketThread(Socket socket, TextArea chatWindow) {
        this.socket = socket;
        this.chatWindow = chatWindow;
        keepAlive = true;
        System.out.println("Client Listening...");
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void messageIncoming(Message message) {
        switch (message.getType()) {
            case Message.SIGN_IN_GRANT:
                System.out.println("You Joined Room.");
                chatWindow.appendText("You Joined Room.\n");
                break;
            case Message.SIGN_OUT_GRANT:
                setKeepAlive(false);
                break;
            case Message.BROADCAST:
                System.out.println(message.getContent());
                chatWindow.appendText(message.getContent());
                chatWindow.appendText("\n");
                break;
            case Message.SPEAK:
                System.out.println(message.getContent());
                chatWindow.appendText(message.getContent());
                chatWindow.appendText("\n");
                try {
                    File dir = new File("cache\\" + socket.getLocalPort());
                    dir.mkdirs();
                    String fileName = "\\download" + (int) (Math.random() * 65536) + ".wav";
                    FileOutputStream fileOutputStream = new FileOutputStream(new File("cache\\" + socket.getLocalPort() + fileName));
                    fileOutputStream.write(message.getAudioByteArray());
                    fileOutputStream.close();
                    ClientPlayThread clientPlayThread = new ClientPlayThread("cache\\" + socket.getLocalPort() + fileName);
                    clientPlayThread.start();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Invalid Message From Server.");
                break;
        }
    }

    @Override
    public void run() {
        try {
            while (isKeepAlive()) {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) inputStream.readObject();
                messageIncoming(message);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
