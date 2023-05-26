package com.edengardensigiriya.edengarden.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SettingsFormController {
    public AnchorPane settingsRoot;
    public JFXButton userChangeBtn;
    public JFXButton changePackBtn;
    public AnchorPane settingInsideRoot;

    public void initialize() throws IOException {
        settingInsideRoot.getChildren().clear();
        settingInsideRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/changeUserForm.fxml")));
    }
    public void showChangePackOnAction(ActionEvent actionEvent) throws IOException {
        settingInsideRoot.getChildren().clear();
        settingInsideRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/changePacksForm.fxml")));
    }

    public void showChangeUserOnAction(ActionEvent actionEvent) throws IOException {
        settingInsideRoot.getChildren().clear();
        settingInsideRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/changeUserForm.fxml")));
    }
}