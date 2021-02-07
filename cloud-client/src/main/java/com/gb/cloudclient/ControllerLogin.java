package com.gb.cloudclient;

import com.gb.cloudcore.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {

    public TextField loginName;
    public PasswordField password;
    private Stage stage;
    private Stage primaryStage;
    private User user;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void auth(ActionEvent actionEvent) {
        user = new User(loginName.getText(), password.getText());
        stage.close();
    }

    public void setStage(Stage stage, Stage primaryStage) {
        this.stage = stage;
        this.primaryStage = primaryStage;
    }

    public User getUser() {
        return user;
    }
}
