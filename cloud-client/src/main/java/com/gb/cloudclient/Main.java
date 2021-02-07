package com.gb.cloudclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{

        Stage loginStage = new Stage();
        FXMLLoader fxmlLoaderLogin = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent rootLogin = fxmlLoaderLogin.load();
        ControllerLogin controllerLogin = fxmlLoaderLogin.getController();
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(rootLogin));
        controllerLogin.setStage(loginStage, primaryStage);
        loginStage.showAndWait();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setUser(controllerLogin.getUser());
        primaryStage.setOnCloseRequest(event -> controller.exitAction());
        primaryStage.setTitle("Cloud Storage Client");
        primaryStage.setScene(new Scene(root, 800, 400));
        controller.setStage(primaryStage);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
