package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.RegExPatterns;
import com.edengardensigiriya.edengarden.model.ChangePackModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChangePacksFormController{
    public TextField txtSleepCount;
    public Button changePriceBtn;
    public TextField txtNewPrice;
    public ComboBox packagesCmb;
    public static ArrayList<String> roomPrices = new ArrayList<>();

    public void initialize() throws SQLException {
        txtNewPrice.setDisable(true);
        txtSleepCount.setDisable(true);
        ChangePackModel.setArrayList();
        ObservableList<String> roomPack = FXCollections.observableList(roomPrices);
        packagesCmb.setItems(roomPack);
    }
    public void changePriceOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isAffected=false;
            if (isCorrectPattern()) {
                isAffected = ChangePackModel.changePakage(String.valueOf(packagesCmb.getSelectionModel().getSelectedItem()),txtNewPrice.getText());
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Package Updated!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                resetPage();
            } else {
                new Alert(Alert.AlertType.WARNING, "Re-Check Submitted Details!").showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    private void resetPage() throws SQLException {
        roomPrices.clear();
        ChangePackModel.setArrayList();
        ObservableList<String> roomPack = FXCollections.observableList(roomPrices);
        packagesCmb.setItems(roomPack);
        txtSleepCount.setText("");
        txtNewPrice.setText("");
    }

    public void selectPriceOnAction(ActionEvent actionEvent) throws SQLException {
        txtNewPrice.setDisable(false);
        String price= String.valueOf(packagesCmb.getSelectionModel().getSelectedItem());
        txtSleepCount.setText(ChangePackModel.getSleepCount(price));
        if (txtSleepCount.getText().equals("No Rooms Available")){
            txtNewPrice.setDisable(true);
        }
    }
    private boolean isCorrectPattern(){
        if (RegExPatterns.getDoublePattern().matcher(txtNewPrice.getText()).matches()){
            return true;
        }
        return false;
    }
}
