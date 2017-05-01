package com.linkinpark213.phone.client;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by ooo on 2017/5/1 0001.
 */
public class ClientRecorder {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private ClientRecordThread clientRecordThread;
    private String dirName;

    public ClientRecorder(String dirName) {
        try {
            audioFormat = getDefaultAudioFormat();
            DataLine.Info targetDataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(targetDataLineInfo);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        this.dirName = dirName;
    }

    public AudioFormat getDefaultAudioFormat() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 8000f;
        int sampleSize = 16;
        String signedString = "signed";
        boolean bigEndian = true;
        int channels = 1;
        return new AudioFormat(encoding, rate, sampleSize, channels,
                (sampleSize / 8) * channels, rate, bigEndian);
    }

    public void startRecording() {
        new ClientRecordThread(audioFormat, targetDataLine, dirName).start();
    }

    public void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}
