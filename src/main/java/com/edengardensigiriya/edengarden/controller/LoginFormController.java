package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

public class LoginFormController {
    public Hyperlink wbLnk;
    public TextField txtUserName;
    public Button lgnBtn;
    public TextField txtPwd;
    public AnchorPane root;
    public Rectangle rectangle;
    public Hyperlink hprLnkChngePwd;

    public void loadBrowserOnAction(ActionEvent actionEvent) {
        try {

            URI uri = new URI("https://www.edengardensigiriya.com");

            java.awt.Desktop.getDesktop().browse(uri);

        } catch (Exception e) {
        }
    }

    public void escapeOnAction(ActionEvent actionEvent) {
        txtPwd.requestFocus();
    }

    public void lgnOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        login();
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException, SQLException {
        login();
    }

    private void login() throws IOException, SQLException {
        Stage stage = (Stage) root.getScene().getWindow();
        if (stage.getTitle().equals("Receptionist Login Form")) {
            if (User.IsCorrect(txtUserName.getText(), txtPwd.getText(),"Receptionist")) {
                setUI("Receptionist Dashboard", "/view/receptionistDashboardForm.fxml");
            }else{
                new Alert(Alert.AlertType.WARNING, "Username or password is incorrect!").show();
            }
        } else {
            if (User.IsCorrect(txtUserName.getText(), txtPwd.getText(),"Manager")) {
                setUI("Manager Dashboard", "/view/managerDashboardForm.fxml");
            }else{
                new Alert(Alert.AlertType.WARNING, "Username or password is incorrect!").show();
            }
        }
    }

    private void setUI(String title, String path) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.setTitle(title);
        stage.centerOnScreen();
        //stage.setMaximized(true);
    }

    public void loadChangePasswordOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        if (stage.getTitle().equals("Receptionist Login Form")) {
            setUI("Change Receptionist Password", "/view/forgotPasswordForm.fxml");
        } else {
            setUI("Change Manager Password", "/view/forgotPasswordForm.fxml");
        }
    }
}
