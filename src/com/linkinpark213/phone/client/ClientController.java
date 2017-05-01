package com.linkinpark213.phone.client;

import com.linkinpark213.phone.common.Message;
import javafx.scene.control.TextArea;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.Socket;

public class ClientController {
    private Socket socket;
    private ClientSocketThread clientSocketThread;
    private ClientRecorder clientRecorder;

    public ClientController(TextArea textArea) {
        try {
            socket = new Socket("127.0.0.1", 4213);
            clientRecorder = new ClientRecorder("cache\\" + socket.getLocalPort());
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

    public void chat(String message) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new Message(Message.CHAT, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signOut() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new Message(Message.SIGN_OUT, ""));
            System.out.println("You Left Room.");
            clientSocketThread.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startRecording() {
        clientRecorder.startRecording();
    }

    public void stopRecordingAndSend() {
        clientRecorder.stopRecording();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("cache\\" + socket.getLocalPort() + "\\record.wav"));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.AU, byteArrayOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] audioByteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            audioInputStream.close();

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(new Message("", audioByteArray));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
}
