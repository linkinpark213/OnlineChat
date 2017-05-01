package com.linkinpark213.phone.client;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by ooo on 2017/5/1 0001.
 */
public class ClientRecordThread extends Thread {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private boolean keepRecording;
    private ByteArrayOutputStream byteArrayOutputStream;
    private String dirName;

    public boolean isKeepRecording() {
        return keepRecording;
    }

    public void setKeepRecording(boolean keepRecording) {
        this.keepRecording = keepRecording;
    }

    public ClientRecordThread(AudioFormat audioFormat, TargetDataLine targetDataLine, String dirName) {
        keepRecording = true;
        this.audioFormat = audioFormat;
        this.targetDataLine = targetDataLine;
        this.byteArrayOutputStream = byteArrayOutputStream;
        this.dirName = dirName;
    }

    @Override
    public void run() {
        try {
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            File file = new File(dirName);
            file.mkdirs();
            AudioSystem.write(new AudioInputStream(targetDataLine), AudioFileFormat.Type.AU, new File(dirName + "\\record.wav"));
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
