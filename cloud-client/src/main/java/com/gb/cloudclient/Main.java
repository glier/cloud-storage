package com.gb.cloudclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        primaryStage.setOnCloseRequest(event -> controller.exitAction());
        primaryStage.setTitle("Cloud Storage Client");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();

        controller.setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
