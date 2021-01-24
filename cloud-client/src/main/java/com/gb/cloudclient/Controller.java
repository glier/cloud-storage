package com.gb.cloudclient;

import com.gb.cloudcore.Command;
import com.gb.cloudcore.CommandEvent;
import com.gb.cloudcore.User;
import com.gb.cloudcore.UserStorageStructure;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Network network;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network(this);
    }

    public void sendMsgAction(ActionEvent actionEvent) {
        //network.sendMessage(msgField.getText());
        //network.sendMessage(new Command(CommandEvent.GET_USER_DIR_STRUCTURE, new User("Alex")));
        msgField.clear();
        msgField.requestFocus();
    }

    public void exitAction() {
        network.close();
        Platform.exit();
    }

    public void setUserStorageStructure(UserStorageStructure o) {
        for (String path : o.getUserStorageStructure()) {
            System.out.println(path);
        }
    }
}