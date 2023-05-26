package com.edengardensigiriya.edengarden.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class ReceptionistDashboardFormController {
    public JFXButton customerBtn;
    public JFXButton transportBtn;
    public JFXButton bookingBtn;
    public JFXButton rentalBtn;
    public JFXButton paymentBtn;
    public AnchorPane root;
    public AnchorPane homeRoot;
    public JFXButton filterBtn;
    public JFXButton homeBtn;
    public JFXButton profileBtn;
    public Label dateLbl;
    public Label timeLbl;

    public void initialize() throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/homeContentForm.fxml")));
        loadDateAndTime();
    }

    private void loadDateAndTime() {
        dateLbl.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        Timeline clock=new Timeline(new KeyFrame(Duration.ZERO,e->{
            LocalTime currentTime=LocalTime.now();
            timeLbl.setText(currentTime.getHour()+" : "+ currentTime.getMinute()+" : "+ currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void showCustomerManageOnAction(ActionEvent actionEvent) throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/customerManageForm.fxml")));
    }

    public void showTransportManageOnAction(ActionEvent actionEvent) throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/transportManageForm.fxml")));
    }

    public void showBookingManageOnAction(ActionEvent actionEvent) throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/bookingManageForm.fxml")));
    }

    public void showRentalManageOnAction(ActionEvent actionEvent) throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/rentalManageForm.fxml")));
    }

    public void showPaymentManageOnAction(ActionEvent actionEvent) throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/paymentManageForm.fxml")));
    }

    public void showFilterFormOnAction(ActionEvent actionEvent) throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/filterCustomerForm.fxml")));
    }

    public void loadProfileSelectionOnAction(ActionEvent actionEvent) throws IOException {
        setUI("Profile Selection","/view/profileSelectionForm.fxml");
    }

    public void loadHomeOnAction(ActionEvent actionEvent) throws IOException {
        homeRoot.getChildren().clear();
        homeRoot.getChildren().add(FXMLLoader.load(getClass().getResource("/view/homeContentForm.fxml")));
    }
    private void setUI(String title, String path) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.setTitle(title);
        stage.centerOnScreen();
    }
}
