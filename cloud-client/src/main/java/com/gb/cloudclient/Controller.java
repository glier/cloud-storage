package com.gb.cloudclient;

import com.gb.cloudcore.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    private Stage stage;
    private Network network;
    private User user;
    private Path currentPath;
    private FileChooser fileChooser;

    @FXML
    private TableView<FileTableRow> filesTableView;

    @FXML
    private TableColumn<FileTableRow, String> elementName;
    @FXML
    private TableColumn<FileTableRow, Long> elementSize;
    @FXML
    private TableColumn<FileTableRow, String> lastModifiedTime;
    @FXML
    private TableColumn<FileTableRow, Boolean> isDirectory;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.network = new Network(this);
        this.user = new User("Alex");
        this.currentPath = Paths.get("/");
        this.fileChooser = new FileChooser();
        filesTableView.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
                rowDoubleClickAction(filesTableView.getSelectionModel().getSelectedItem());
            }
        });
        elementName.setCellValueFactory(cellData -> cellData.getValue().elementNameProperty());
        elementSize.setCellValueFactory(cellData -> cellData.getValue().elementSizeProperty().asObject());
        lastModifiedTime.setCellValueFactory(cellData -> cellData.getValue().lastModifiedTimeProperty());
        isDirectory.setCellValueFactory(cellData -> cellData.getValue().isDirectoryProperty().asObject());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void exitAction() {
        network.close();
        Platform.exit();
    }

    public void setUserStorageStructure(UserStorageStructure o) {
        filesTableView.getItems().clear();

        o.getUserStorageStructure().forEach(p -> {
            filesTableView.getItems().add(new FileTableRow(
                    new SimpleStringProperty(p.getElementName()),
                    new SimpleLongProperty(p.getElementSize()),
                    new SimpleStringProperty(p.getLastModifiedTime()),
                    new SimpleBooleanProperty(p.getIsDirectory())));
        });

    }

    private void rowDoubleClickAction(DirElement row) {
        if (row != null) {
            if (row.getElementName().equals("..") && row.getIsDirectory()) {
                currentPath = currentPath.getParent();
            } else if (row.getIsDirectory()) {
                currentPath = Paths.get(currentPath.toString(), row.getElementName());
            }
            logger.info(currentPath);
            dirUpdateAction(null);
        }
    }

    public void dirUpdateAction(ActionEvent actionEvent) {
        network.sendMessage(new Command(CommandEvent.GET_USER_DIR_STRUCTURE, user, currentPath.toString(), null));
    }

    public void fileUploadAction(ActionEvent actionEvent) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            logger.info("Choosed file " + file.getAbsolutePath());
            byte[] bytes = new byte[Constants.CHUNKED_FILE_LENGTH];
            int byteRead;
            ChunkedFile chunkedFile = new ChunkedFile();
            chunkedFile.setUser(user);
            chunkedFile.setClientPath(file.getAbsolutePath());
            chunkedFile.setStoragePath(currentPath.toString() + "/" + file.getName());
            chunkedFile.setStarPos(0);

            try (RandomAccessFile randomAccessFile = new RandomAccessFile(chunkedFile.getClientPath(), "r")) {
                randomAccessFile.seek(chunkedFile.getStarPos());

                if ((byteRead = randomAccessFile.read(bytes)) != -1) {
                    chunkedFile.setEndPos(byteRead);
                    chunkedFile.setBytes(bytes);
                    network.sendMessage(chunkedFile);
                } else {
                }
                logger.info("Read " + byteRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fileDownloadAction(ActionEvent actionEvent) {
        DirElement dirElement = filesTableView.getSelectionModel().getSelectedItem();

        if (!dirElement.getIsDirectory()) {
            Path serverPath = Paths.get(currentPath.toString(), dirElement.getElementName());
            fileChooser.setInitialFileName(serverPath.getFileName().toString());
            File clientPath = fileChooser.showSaveDialog(stage);
            if (clientPath != null) {
                logger.info(serverPath);
                logger.info(clientPath.getAbsolutePath());
                network.sendMessage(new Command(CommandEvent.FILE_DOWNLOAD, user, serverPath.toString(), clientPath.getAbsolutePath()));
            }
        }

    }

    public void fileDeleteAction(ActionEvent actionEvent) {
        DirElement dirElement = filesTableView.getSelectionModel().getSelectedItem();
        Path serverPath = Paths.get(currentPath.toString(), dirElement.getElementName());
        logger.info("For delete: " + serverPath.toString());
        network.sendMessage(new Command(CommandEvent.FILE_DELETE, user, serverPath.toString(), null));
    }

    public User getUser() {
        return user;
    }

    public String getCurrentPath() {
        return this.currentPath.toString();
    }
}