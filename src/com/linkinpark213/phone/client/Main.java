package com.linkinpark213.phone.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    private ClientController clientController;
    private TextArea chatWindow;
    private TextArea chatEdit;

    public Main() {
        chatWindow = new TextArea();
        this.clientController = new ClientController(chatWindow);
    }

    public void sendChatMessage() {
        try {
            clientController.chat(chatEdit.getText());
            chatWindow.appendText("Me: " + chatEdit.getText());
            chatWindow.appendText("\n");
            chatEdit.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowPane root = new FlowPane();

        chatWindow.setLayoutX(400);
        chatWindow.setLayoutY(10);
        chatWindow.setPrefRowCount(13);

        chatEdit = new TextArea();
        chatEdit.setPrefRowCount(1);

        Button sendButton = new Button("Send");
        sendButton.setPrefWidth(200);
        sendButton.setPrefHeight(40);
        Button recordButton = new Button("Hold to Record Sound");
        recordButton.setPrefWidth(200);
        recordButton.setPrefHeight(40);

        root.getChildren().add(chatWindow);
        root.getChildren().add(chatEdit);
        root.getChildren().add(sendButton);
        root.getChildren().add(recordButton);

        chatEdit.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().getName().equals("Enter")) {
                    sendChatMessage();
                }
            }
        });

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendChatMessage();
            }
        });

        recordButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                recordButton.setText("Release to Stop");
                clientController.startRecording();
            }
        });

        recordButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                recordButton.setText("Hold to Record");
                clientController.stopRecordingAndSend();
            }
        });

        primaryStage.setTitle("Chatting Room #213");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                clientController.signOut();
            }
        });
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
