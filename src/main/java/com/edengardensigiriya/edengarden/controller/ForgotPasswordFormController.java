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

public class ForgotPasswordFormController {
    public Hyperlink wbLnk;
    public Rectangle rectangle;
    public AnchorPane root;
    public Button resrPwdBtn;
    public TextField txtOldPwd;
    public TextField txtNewPwd;
    public TextField txtReEnterPwd;
    public Button backBtn;
    public TextField txtOldUserName;
    public TextField txtNewUserName;

    public void loadBrowserOnAction(ActionEvent actionEvent) {
        try {

            URI uri = new URI("https://www.edengardensigiriya.com");

            java.awt.Desktop.getDesktop().browse(uri);

        } catch (Exception e) {
        }
    }

    public void resetPwdOnAction(ActionEvent actionEvent) throws IOException {
        reset();
    }

    private void reset() throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        boolean isDone;
        if (stage.getTitle().equals("Change Receptionist Password")){
            isDone=User.changePwd(txtOldUserName.getText(),txtOldPwd.getText(),txtNewUserName.getText(),txtNewPwd.getText(),txtReEnterPwd.getText(),"Receptionist");
        }else{
            isDone=User.changePwd(txtOldUserName.getText(),txtOldPwd.getText(),txtNewUserName.getText(),txtNewPwd.getText(),txtReEnterPwd.getText(),"Manager");
        }
        if (isDone){
            new Alert(Alert.AlertType.INFORMATION, "Username and Password Changed!").showAndWait();
            if (stage.getTitle().equals("Change Receptionist Password")){
                setUI("Receptionist Login Form","/view/LoginForm.fxml");
            }else{
                setUI("Manager Login Form","/view/LoginForm.fxml");
            }
        }else {
            new Alert(Alert.AlertType.WARNING, "Re-Check Entered Details!").showAndWait();
        }
    }

    public void escapeOldOnAction(ActionEvent actionEvent) {
        txtOldPwd.requestFocus();
    }

    public void escapeNewOnAction(ActionEvent actionEvent) {
        txtNewPwd.requestFocus();
    }

    public void escapeReEntrOnAction(ActionEvent actionEvent) {
        txtReEnterPwd.requestFocus();
    }

    public void resetOnAction(ActionEvent actionEvent) throws IOException {
        reset();
    }

    public void backOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        if (stage.getTitle().equals("Change Receptionist Password")){
            setUI("Receptionist Login Form","/view/LoginForm.fxml");
        }else{
            setUI("Manager Login Form","/view/LoginForm.fxml");
        }
    }

    public void escapeOnAction(ActionEvent actionEvent) {
        txtNewUserName.requestFocus();
    }

    private void setUI(String title, String path) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.setTitle(title);
        stage.centerOnScreen();
    }
}
