package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Employer;
import com.edengardensigiriya.edengarden.dto.RegExPatterns;
import com.edengardensigiriya.edengarden.dto.Room;
import com.edengardensigiriya.edengarden.dto.RoomUpdate;
import com.edengardensigiriya.edengarden.dto.tm.RoomTM;
import com.edengardensigiriya.edengarden.model.CustomerModel;
import com.edengardensigiriya.edengarden.model.RoomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomManageFormController {
    public AnchorPane roomsRoot;
    public TextField roomNoTxt;
    public TextField costTxt;
    public TableView roomTbl;
    public Button addBtn;
    public Button updateBtn;
    public Button removeBtn;
    public ComboBox roomTypeCmbBx;
    public ComboBox sleepsCountCmbBx;
    public TableColumn columnRoomNo;
    public TableColumn columnBedCount;
    public TableColumn columnCostPerDay;
    public TableColumn columnRoomState;
    public TableColumn columnRoomType;
    public static ArrayList<String> roomTypes = new ArrayList<>();
    public static ArrayList<Integer> sleepCount = new ArrayList<>();


    public void initialize() throws SQLException {
        roomTypes.add("Deluxe Room");
        roomTypes.add("Standard Room");
        ObservableList<String> roomType = FXCollections.observableList(roomTypes);
        roomTypeCmbBx.setItems(roomType);

        sleepCount.add(1);
        sleepCount.add(2);
        sleepCount.add(3);
        ObservableList<Integer> sleeps = FXCollections.observableList(sleepCount);
        sleepsCountCmbBx.setItems(sleeps);
        setCellValueFactory();
        getAllRooms();
    }

    private void getAllRooms() throws SQLException {
        ObservableList<RoomTM> obList = FXCollections.observableArrayList();
        List<Room> roomList = RoomModel.getAll();

        for (Room room : roomList) {
            obList.add(new RoomTM(
                    room.getRoomNo(),
                    room.getRoomType(),
                    room.getSleepCount(),
                    room.getCostPerDay(),
                    room.getAvailability()
            ));
        }
        roomTbl.setItems(obList);
    }

    void setCellValueFactory() {
        columnRoomNo.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        columnRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        columnBedCount.setCellValueFactory(new PropertyValueFactory<>("sleepCount"));
        columnCostPerDay.setCellValueFactory(new PropertyValueFactory<>("costPerDay"));
        columnRoomState.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    public void roomNoSearchOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            List<RoomUpdate> roomList = RoomModel.searchRoom(roomNoTxt.getText());
            if (!roomList.isEmpty()){
                for (RoomUpdate room : roomList) {
                    roomNoTxt.setText(room.getRoomNo());
                    roomTypeCmbBx.setValue(room.getRoomType());
                    sleepsCountCmbBx.setValue(room.getSleepCount());
                    costTxt.setText(room.getCostPerDay());
                    roomNoTxt.setDisable(true);
                }
            }else{
                new Alert(Alert.AlertType.WARNING, "Room Not Found!").showAndWait();
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void updateDetailsOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            String roomType = String.valueOf(roomTypeCmbBx.getSelectionModel().getSelectedItem());
            int sleepCount = Integer.parseInt(String.valueOf(sleepsCountCmbBx.getSelectionModel().getSelectedItem()));
            double costPerDay = Double.parseDouble(costTxt.getText());
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected = RoomModel.updateRoom(roomNoTxt.getText(), roomType, sleepCount, costPerDay);
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Room Updated Successfully!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                roomNoTxt.setDisable(false);
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

    public void removeRoomOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            Optional<ButtonType> comfirm=new Alert(Alert.AlertType.CONFIRMATION, "Do you want to remove the room?").showAndWait();
            if (comfirm.isPresent()){
                boolean isAffected=false;
                if (isCorrectPattern()){
                    isAffected = RoomModel.removeRoom(roomNoTxt.getText());
                }
                if (isAffected) {
                    new Alert(Alert.AlertType.INFORMATION, "Room Removed Successfully!").showAndWait();
                    DBConnection.getInstance().getConnection().commit();
                    roomNoTxt.setDisable(false);
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Re-Check Submitted Details!").showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void addRoomOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            String roomType = String.valueOf(roomTypeCmbBx.getSelectionModel().getSelectedItem());
            int sleepCount = Integer.parseInt(String.valueOf(sleepsCountCmbBx.getSelectionModel().getSelectedItem()));
            double costPerDay = Double.parseDouble(costTxt.getText());
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected = RoomModel.addRoom(RoomModel.generateID(), roomType, sleepCount, costPerDay);
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Room Added Successfully!").showAndWait();
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

    public void resetPage() throws SQLException {
        roomNoTxt.setText("");
        roomTypeCmbBx.setValue("Room Type");
        sleepsCountCmbBx.setValue("Sleeps");
        costTxt.setText("");
        setCellValueFactory();
        getAllRooms();
    }
    private boolean isCorrectPattern(){
        if (RegExPatterns.getDoublePattern().matcher(costTxt.getText()).matches()){
            return true;
        }
        return false;
    }
}
