package com.edengardensigiriya.edengarden.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;

public class ProfileSelectionFormController {

    public AnchorPane root;
    public JFXButton rcptnBtn;
    public JFXButton mngrBtn;
    public Hyperlink wbLnk;

    public void loadBrowserOnAction(ActionEvent actionEvent) {
        try {

            URI uri = new URI("https://www.edengardensigiriya.com");

            java.awt.Desktop.getDesktop().browse(uri);

        } catch (Exception e) {}
    }

    public void showrcptnLoginFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("Receptionist Login Form");
    }

    public void showmngrLoginFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("Manager Login Form");
    }

    private void setUI(String s) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.setTitle(s);
        stage.centerOnScreen();
    }
}