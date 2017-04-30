package com.linkinpark213.phone.common;

import java.io.Serializable;

/**
 * Created by ooo on 2017/4/29 0029.
 */
public class Message implements Serializable {
    public final static int SIGN_IN = 0;
    public final static int SIGN_IN_GRANT = 1;
    public final static int SIGN_OUT = 2;
    public final static int SIGN_OUT_GRANT = 3;
    public final static int CHAT = 4;
    public final static int BROADCAST = 5;
    public final static int INVALID_MESSAGE = 6;
    public final static int SPEAK = 7;
    private int type;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Message(int type, String content) {
        this.type = type;
        this.content = content;
    }
}
