package com.linkinpark213.phone.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    private ClientController clientController;
    private TextArea chatWindow;

    public Main() {
        chatWindow = new TextArea();
        this.clientController = new ClientController(chatWindow);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowPane root = new FlowPane();

        chatWindow.setLayoutX(300);
        chatWindow.setLayoutY(10);
        chatWindow.setPrefRowCount(10);

        TextArea chatEdit = new TextArea();
        chatEdit.setPrefRowCount(1);

        Button button = new Button("Send");

        root.getChildren().add(chatWindow);
        root.getChildren().add(chatEdit);
        root.getChildren().add(button);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    clientController.chat(chatEdit.getText());
                    chatWindow.appendText("Me: " + chatEdit.getText());
                    chatWindow.appendText("\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.setTitle("Chatting Room #213");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                clientController.signOut();
            }
        });
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
