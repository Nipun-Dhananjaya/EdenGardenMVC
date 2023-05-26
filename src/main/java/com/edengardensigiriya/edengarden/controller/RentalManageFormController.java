package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.*;
import com.edengardensigiriya.edengarden.dto.tm.BookingTM;
import com.edengardensigiriya.edengarden.dto.tm.RentalTM;
import com.edengardensigiriya.edengarden.model.BookingModel;
import com.edengardensigiriya.edengarden.model.PaymentModel;
import com.edengardensigiriya.edengarden.model.RentalModel;
import com.edengardensigiriya.edengarden.model.RoomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.edengardensigiriya.edengarden.controller.BicycleManageFormController.bicycleTypes;
import static com.edengardensigiriya.edengarden.controller.CarManageFormController.carTypes;

public class RentalManageFormController {
    public AnchorPane rentRoot;
    public TextField bookingIdTxt;
    public TextField custIdTxt;
    public TextField nameTxt;
    public TextField startTimeTxt;
    public DatePicker startDateDtPckr;
    public TextField durationTxt;
    public ComboBox vehicleTypeCmbBx;
    public ComboBox vehicleIdCmbBx;
    public TextField costTxt;
    public Button cancelRentBtn;
    public ComboBox vehicleCmbBx;
    public Button updateBtn;
    public Button rentBtn;
    private static ArrayList<String> vehicleType=new ArrayList<>();
    public static ArrayList<String> vehicleId = new ArrayList<>();
    public TableColumn columnRentId;
    public TableColumn columnCustId;
    public TableColumn columnName;
    public TableColumn columnVehicleType;
    public TableColumn columnVehicleId;
    public TableColumn columnFrom;
    public TableColumn columnDuration;
    public TableColumn columnCost;
    public TableView bicycleRentalTbl;
    public TableColumn columnRentId1;
    public TableColumn columnCustId1;
    public TableColumn columnName1;
    public TableColumn columnVehicleType1;
    public TableColumn columnVehicleId1;
    public TableColumn columnFrom1;
    public TableColumn columnDuration1;
    public TableColumn columnCost1;
    public TableView carRentalTbl;
    public TableColumn columnStatus;
    public TableColumn columnStatus1;

    public void initialize() throws SQLException {
        vehicleType.add("Bicycle");
        vehicleType.add("Car");
        ObservableList<String> Type = FXCollections.observableList(vehicleType);
        vehicleCmbBx.setItems(Type);
        RentalModel.updateStatus();
        setCarCellValueFactory();
        getAllCarRentals();
        setBicycleCellValueFactory();
        getAllBicycleRentals();
        costTxt.setText("0.00");
    }

    private void setCarCellValueFactory() {
        columnRentId.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        columnCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        columnVehicleType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        columnVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        columnFrom.setCellValueFactory(new PropertyValueFactory<>("rentFrom"));
        columnDuration.setCellValueFactory(new PropertyValueFactory<>("rentDuration"));
        columnCost.setCellValueFactory(new PropertyValueFactory<>("rentCost"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("rentStatus"));
    }
    private void setBicycleCellValueFactory() {
        columnRentId1.setCellValueFactory(new PropertyValueFactory<>("rentId"));
        columnCustId1.setCellValueFactory(new PropertyValueFactory<>("custId"));
        columnName1.setCellValueFactory(new PropertyValueFactory<>("custName"));
        columnVehicleType1.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        columnVehicleId1.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        columnFrom1.setCellValueFactory(new PropertyValueFactory<>("rentFrom"));
        columnDuration1.setCellValueFactory(new PropertyValueFactory<>("rentDuration"));
        columnCost1.setCellValueFactory(new PropertyValueFactory<>("rentCost"));
        columnStatus1.setCellValueFactory(new PropertyValueFactory<>("rentStatus"));
    }

    private void getAllCarRentals() throws SQLException {
        ObservableList<RentalTM> obList = FXCollections.observableArrayList();
        List<Rental> rentalList = RentalModel.getAllCars();

        for (Rental rental : rentalList) {
            obList.add(new RentalTM(
                    rental.getRentId(),
                    rental.getCustId(),
                    rental.getCustName(),
                    rental.getVehicleType(),
                    rental.getVehicleId(),
                    rental.getRentFrom(),
                    rental.getRentDuration(),
                    rental.getRentCost(),
                    rental.getRentStatus()
            ));
        }
        carRentalTbl.setItems(obList);
    }

    private void getAllBicycleRentals() throws SQLException {
        ObservableList<RentalTM> obList = FXCollections.observableArrayList();
        List<Rental> rentalList = RentalModel.getAllBicycles();

        for (Rental rental : rentalList) {
            obList.add(new RentalTM(
                    rental.getRentId(),
                    rental.getCustId(),
                    rental.getCustName(),
                    rental.getVehicleType(),
                    rental.getVehicleId(),
                    rental.getRentFrom(),
                    rental.getRentDuration(),
                    rental.getRentCost(),
                    rental.getRentStatus()
            ));
        }
        bicycleRentalTbl.setItems(obList);
    }

    public void idSearchOnAction(ActionEvent actionEvent) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            List<RentalUpdate> rentList = RentalModel.searchRental(bookingIdTxt.getText());
            if (!rentList.isEmpty()){
                for (RentalUpdate rental : rentList) {
                    bookingIdTxt.setText(rental.getBookingId());
                    custIdTxt.setText(rental.getCustId());
                    nameTxt.setText(rental.getCustName());
                    vehicleCmbBx.setValue(rental.getVehicle());
                    vehicleTypeCmbBx.setValue(rental.getVehicleType());
                    vehicleIdCmbBx.setValue(rental.getVehicleId());
                    String[] datTime=rental.getBookFrom().split(" ");
                    startDateDtPckr.setValue(LocalDate.parse(rental.getBookFrom(),formatter));
                    startTimeTxt.setText(datTime[1]);
                    durationTxt.setText(rental.getDuration());
                    costTxt.setText(rental.getCost());
                    bookingIdTxt.setDisable(true);
                    custIdTxt.setDisable(true);
                    nameTxt.setDisable(true);
                }
            }else{
                new Alert(Alert.AlertType.WARNING, "Rental Not Found!").showAndWait();
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void searchCustomerOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            String name= RentalModel.searchCustomer(custIdTxt.getText());
            if (name!=null){
                nameTxt.setText(name);
            }
            else{
                new Alert(Alert.AlertType.WARNING, "Customer ID not Found!").showAndWait();
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void cancelRentOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            Optional<ButtonType> comfirm=new Alert(Alert.AlertType.CONFIRMATION, "Do you want to cancel the booking?").showAndWait();
            if (comfirm.isPresent()){
                String bookingId = bookingIdTxt.getText();
                String paymentId = RentalModel.getPaymentId(bookingId);
                boolean isAffected=false;
                if (isCorrectPattern()){
                    isAffected=RentalModel.cancelRental(bookingId, custIdTxt.getText(),String.valueOf(vehicleTypeCmbBx.getSelectionModel().getSelectedItem()),
                            String.valueOf(vehicleIdCmbBx.getSelectionModel().getSelectedItem()), paymentId,"Cancelled");
                }
                if (isAffected) {
                    new Alert(Alert.AlertType.INFORMATION, "Rental Cancelled!").showAndWait();
                    sendMail("Cancel");
                    bookingIdTxt.setDisable(false);
                    custIdTxt.setDisable(false);
                    nameTxt.setDisable(false);
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Re-Check Submitted Details!").showAndWait();
                }
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void updateDetailsOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.of(startDateDtPckr.getValue(), LocalTime.parse(startTimeTxt.getText()));
            long duration = Long.parseLong(durationTxt.getText());
            String bookingId = bookingIdTxt.getText();
            String paymentId = RentalModel.getPaymentId(bookingId);
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected=RentalModel.updateRental(bookingId, custIdTxt.getText(), startDate, duration, paymentId,
                        String.valueOf(vehicleCmbBx.getValue()),String.valueOf(vehicleTypeCmbBx.getValue()),
                        String.valueOf(vehicleIdCmbBx.getValue()), costTxt.getText());
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Rental Updated!").showAndWait();
                sendMail("Update");
                bookingIdTxt.setDisable(false);
                custIdTxt.setDisable(false);
                nameTxt.setDisable(false);
                resetPage();
            } else {
                new Alert(Alert.AlertType.WARNING, "Re-Check Submitted Details!").showAndWait();
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void rentVehicleOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate = LocalDateTime.of(startDateDtPckr.getValue(), LocalTime.parse(startTimeTxt.getText()));
            long duration = Long.valueOf(durationTxt.getText());
            String paymentId = PaymentModel.generateID();
            String rentalId = RentalModel.generateID();
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected=RentalModel.isAdded(rentalId, custIdTxt.getText(), startDate, duration, paymentId, costTxt.getText(), now,
                        String.valueOf(vehicleCmbBx.getSelectionModel().getSelectedItem()),String.valueOf(vehicleTypeCmbBx.getSelectionModel().getSelectedItem()),
                        String.valueOf(vehicleIdCmbBx.getSelectionModel().getSelectedItem()));
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Rental Made Successfully!").showAndWait();
                sendMail("Booking");
                resetPage();
            } else {
                new Alert(Alert.AlertType.WARNING, "Re-Check Submitted Details!").showAndWait();
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void loadAvailableVehiclesOnAction(ActionEvent actionEvent) throws SQLException {
        vehicleId.clear();
        ResultSet resultSet=RentalModel.getVehicleId(String.valueOf(vehicleCmbBx.getSelectionModel().getSelectedItem()),
                String.valueOf(vehicleTypeCmbBx.getSelectionModel().getSelectedItem()));
        while (resultSet.next()){
            vehicleId.add(resultSet.getString(1));
        }
        ObservableList<String> vehicleIdObLst = FXCollections.observableList(vehicleId);
        vehicleIdCmbBx.setItems(vehicleIdObLst);
    }

    public void loadAvailableVehicleTypesOnAction(ActionEvent actionEvent) {
        carTypes.clear();
        bicycleTypes.clear();
        vehicleTypeCmbBx.setValue("Type");
        vehicleIdCmbBx.setValue("Vehicle ID");
        if (String.valueOf(vehicleCmbBx.getSelectionModel().getSelectedItem()).equals("Car")){
            if (carTypes.isEmpty()) {
                carTypes.add("SUV");
                carTypes.add("Hatchback");
                carTypes.add("Crossover");
                carTypes.add("Convertible");
                carTypes.add("Sedan");
                carTypes.add("Sports Car");
                carTypes.add("Coupe");
                carTypes.add("Minivan");
                carTypes.add("Station Wagon");
                carTypes.add("Pickup Truck");
            }
            ObservableList<String> carType = FXCollections.observableList(carTypes);
            vehicleTypeCmbBx.setItems(carType);
        }else{
            if (bicycleTypes.isEmpty()) {
                bicycleTypes.add("Cruiser");
                bicycleTypes.add("Fixed Gear");
                bicycleTypes.add("Mountain Bike");
                bicycleTypes.add("BMX");
                bicycleTypes.add("Touring Bike");
                bicycleTypes.add("Utility Bike");
                bicycleTypes.add("Folding Bike");
            }
            ObservableList<String> bicycleType = FXCollections.observableList(bicycleTypes);
            vehicleTypeCmbBx.setItems(bicycleType);
        }
    }
    public void resetPage() throws SQLException {
        bookingIdTxt.setText("");
        custIdTxt.setText("");
        nameTxt.setText("");
        startTimeTxt.setText("");
        durationTxt.setText("");
        costTxt.setText("");
        vehicleCmbBx.setValue("Vehicle");
        vehicleTypeCmbBx.setValue("Type");
        vehicleIdCmbBx.setValue("Vehicle ID");
        setCarCellValueFactory();
        setBicycleCellValueFactory();
        getAllCarRentals();
        getAllBicycleRentals();
    }
    private boolean isCorrectPattern(){
        if (RegExPatterns.getNamePattern().matcher(nameTxt.getText()).matches()  && RegExPatterns.getDoublePattern().matcher(costTxt.getText()).matches() && startTimeTxt.getText().matches("^(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$") && RegExPatterns.getDoublePattern().matcher(durationTxt.getText()).matches() ){
            return true;
        }
        return false;
    }
    public void sendMail(String status) throws MessagingException, GeneralSecurityException, IOException, SQLException {
        SendEmail.sendMail(RentalModel.getEmail(custIdTxt.getText()),
                (status.equals("Booking")?"Rent A Vehicle":status.equals("Update")?"Rental Update":"Rental Cancellation"),
                "Dear Customer,\nYour Rental ID:"+(status.equals("Booking")?RentalModel.getBookingId():status.equals("Update")?bookingIdTxt.getText():bookingIdTxt.getText())+"\nYour Customer ID:"+custIdTxt.getText()+"\nName:"+nameTxt.getText()+
                        "\nVehicle Type:"+ vehicleCmbBx.getSelectionModel().getSelectedItem()+"\nVehicle Model:"+ vehicleTypeCmbBx.getSelectionModel().getSelectedItem()+"\nVehicle ID:"+ vehicleIdCmbBx.getSelectionModel().getSelectedItem()+"\nFrom:"+startDateDtPckr.getValue()+"  "+startTimeTxt.getText()+"\nDuration: "+durationTxt.getText()+"\nTotal Cost:"+ costTxt.getText()+
                        "\n"+(status.equals("Booking")?"Rent Successful!":status.equals("Update")?"Rental Update Successfully":"Vehicle Rental Cancelled!"+"\n\nThank you for using our service!\n\nHotel Eden Garden,\nInamaluwa,\nSeegiriya"));
    }
}
