package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.*;
import com.edengardensigiriya.edengarden.dto.tm.BookingTM;
import com.edengardensigiriya.edengarden.model.BookingModel;
import com.edengardensigiriya.edengarden.model.CustomerModel;
import com.edengardensigiriya.edengarden.model.PaymentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingManageFormController  {
    public AnchorPane bookRoot;
    public TextField bookingIdTxt;
    public TextField custIdTxt;
    public TextField nameTxt;
    public TextField startTimeTxt;
    public Button bookBtn;
    public DatePicker startDateDtPckr;
    public TextField endTimeTxt;
    public DatePicker endDateDtPckr;
    public ComboBox roomTypeCmbBx;
    public ComboBox roomNumCmbBx;
    public TextField costTxt;
    public Button cancelBookBtn;
    public Button updateBtn;
    public TableView bookingTbl;
    public static ArrayList<String> roomTypes = new ArrayList<>();
    public TextField sleepsTxt;
    public TableColumn columnBookingId;
    public TableColumn columnCustomerId;
    public TableColumn columnCustName;
    public TableColumn columnRoomNo;
    public TableColumn columnBookFrom;
    public TableColumn columnCost;
    public TableColumn columnDrtion;
    public TableColumn columnAvailability;
    public TableColumn columnBookedOn;

    public void initialize() throws SQLException {
        roomTypes.clear();
        roomTypes.add("Deluxe Room");
        roomTypes.add("Standard Room");

        ObservableList<String> roomType = FXCollections.observableList(roomTypes);
        roomTypeCmbBx.setItems(roomType);
        BookingModel.updateStatus();
        setCellValueFactory();
        getAllBookings();
        costTxt.setText("0.00");
    }

    private void getAllBookings() throws SQLException {
        ObservableList<BookingTM> obList = FXCollections.observableArrayList();
        List<Booking> bookList = BookingModel.getAll();

        for (Booking booking : bookList) {
            obList.add(new BookingTM(
                    booking.getBookingId(),
                    booking.getCustId(),
                    booking.getCustName(),
                    booking.getRoomNo(),
                    booking.getBookFrom(),
                    booking.getDuration(),
                    booking.getCost(),
                    booking.getBookedOn(),
                    booking.getAvailability()
            ));
        }
        bookingTbl.setItems(obList);
    }
    void setCellValueFactory() {
        columnBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        columnCustomerId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        columnCustName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        columnRoomNo.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        columnBookFrom.setCellValueFactory(new PropertyValueFactory<>("bookFrom"));
        columnDrtion.setCellValueFactory(new PropertyValueFactory<>("duration"));
        columnCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        columnBookedOn.setCellValueFactory(new PropertyValueFactory<>("bookedOn"));
        columnAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }
    public void idSearchOnAction(ActionEvent actionEvent) throws SQLException, ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            List<BookingUpdate> bookList = BookingModel.searchBooking(bookingIdTxt.getText());
            if (!bookList.isEmpty()){
                for (BookingUpdate booking : bookList) {
                    bookingIdTxt.setText(booking.getBookingId());
                    custIdTxt.setText(booking.getCustId());
                    nameTxt.setText(booking.getCustName());
                    roomTypeCmbBx.setValue(booking.getRoomType());
                    roomNumCmbBx.setValue(booking.getRoomNo());
                    sleepsTxt.setText(booking.getSleepCount());
                    String[] datTime=booking.getBookFrom().split(" ");
                    startDateDtPckr.setValue(LocalDate.parse(booking.getBookFrom(),formatter));
                    startTimeTxt.setText(datTime[1]);
                    LocalDateTime oldDateTime = LocalDateTime.parse(booking.getBookFrom(), formatter);
                    endDateDtPckr.setValue(LocalDate.from(oldDateTime.plusHours(Long.parseLong(booking.getDuration()))));
                    endTimeTxt.setText(String.valueOf(LocalTime.from(oldDateTime.plusHours(Long.parseLong(booking.getDuration()))))+":00");
                    costTxt.setText(booking.getCost());
                    bookingIdTxt.setDisable(true);
                    custIdTxt.setDisable(true);
                }
            }else{
                new Alert(Alert.AlertType.WARNING, "Booking Cancelled Or Not Found!").showAndWait();
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
            String name=BookingModel.searchCustomer(custIdTxt.getText());
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

    public void bookRoomOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate = LocalDateTime.of(startDateDtPckr.getValue(), LocalTime.parse(startTimeTxt.getText()));
            LocalDateTime endDate = LocalDateTime.of(endDateDtPckr.getValue(), LocalTime.parse(endTimeTxt.getText()));
            long duration = ChronoUnit.HOURS.between(startDate,endDate);
            String paymentId = PaymentModel.generateID();
            String bookingId = BookingModel.generateID();
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected=BookingModel.isAdded(bookingId, custIdTxt.getText(), startDate, duration, paymentId,String.valueOf(roomNumCmbBx.getValue()), costTxt.getText(), now,"Active","Paid");
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Booking Successful!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                sendMail("Booking");
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

    public void cancelBookRoomOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            Optional<ButtonType> comfirm=new Alert(Alert.AlertType.CONFIRMATION, "Do you want to cancel the booking?").showAndWait();
            if (comfirm.isPresent()){
                String bookingId = bookingIdTxt.getText();
                String paymentId = BookingModel.getPaymentId(bookingId);
                boolean isAffected=false;
                if (isCorrectPattern()){
                    System.out.println("correct");
                    isAffected=BookingModel.cancelBooking(bookingId,paymentId,"Cancelled",String.valueOf(roomNumCmbBx.getValue()));
                }
                if (isAffected) {
                    new Alert(Alert.AlertType.INFORMATION, "Booking Cancelled!").showAndWait();
                    DBConnection.getInstance().getConnection().commit();
                    sendMail("Cancel");
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Something went wrong!").showAndWait();
                }
            }
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
            LocalDateTime endDate = LocalDateTime.of(endDateDtPckr.getValue(), LocalTime.parse(endTimeTxt.getText()));
            long duration = ChronoUnit.HOURS.between(startDate,endDate);
            String bookingId = bookingIdTxt.getText();
            String paymentId = BookingModel.getPaymentId(bookingId);
            LocalDateTime paidDateTime = BookingModel.getPaidDateTime(bookingId);
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected=BookingModel.updateBooking(bookingId, custIdTxt.getText(), startDate, duration, paymentId,String.valueOf(roomNumCmbBx.getValue()), costTxt.getText(), paidDateTime,"Active","Paid");
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Booking Updated!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                sendMail("Update");
                bookingIdTxt.setDisable(false);
                custIdTxt.setDisable(false);
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
        bookingIdTxt.setText("");
        custIdTxt.setText("");
        nameTxt.setText("");
        startTimeTxt.setText("");
        endTimeTxt.setText("");
        costTxt.setText("");
        roomNumCmbBx.setValue("Room No");
        roomTypeCmbBx.setValue("Room Type");
        setCellValueFactory();
        getAllBookings();
    }

    public void setSelectedRoomTypeNoOnAction(ActionEvent actionEvent) {
        roomNumCmbBx.setItems(null);
        if (roomTypeCmbBx.getSelectionModel().getSelectedItem().equals("Deluxe Room")){
            BookingModel.setRoomNumbers("Deluxe Room");
            ObservableList<String> roomNo = FXCollections.observableList(BookingModel.deluxeRoomNo);
            roomNumCmbBx.setItems(roomNo);
        }else{
            BookingModel.setRoomNumbers("Standard Room");
            ObservableList<String> roomNo = FXCollections.observableList(BookingModel.standardRoomNo);
            roomNumCmbBx.setItems(roomNo);
        }
    }

    public void setSleepCountOnAction(ActionEvent actionEvent) {
        sleepsTxt.setText("");
        sleepsTxt.setText(BookingModel.setSleepCount(String.valueOf(roomNumCmbBx.getSelectionModel().getSelectedItem())));
    }
    private boolean isCorrectPattern(){
        if (RegExPatterns.getNamePattern().matcher(nameTxt.getText()).matches()  && RegExPatterns.getDoublePattern().matcher(costTxt.getText()).matches() && startTimeTxt.getText().matches("^(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$") && endTimeTxt.getText().matches("^(2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])$")){
            return true;
        }
        return false;
    }
    public void sendMail(String status) throws MessagingException, GeneralSecurityException, IOException, SQLException {
        SendEmail.sendMail(BookingModel.getEmail(custIdTxt.getText()),
                (status.equals("Booking")?"Room Booking":status.equals("Update")?"Room Booking Update":"Room Booking Cancellation"),
                "Dear Customer,\nYour Booking ID:"+(status.equals("Booking")?BookingModel.getBookingId():status.equals("Update")?bookingIdTxt.getText():bookingIdTxt.getText())+"\nYour Customer ID:"+custIdTxt.getText()+"\nName:"+nameTxt.getText()+
                "\nRoom Number:"+ roomNumCmbBx.getSelectionModel().getSelectedItem()+"\nFrom:"+startDateDtPckr.getValue()+"  "+startTimeTxt.getText()+"\nTo:"+endDateDtPckr.getValue()+"  "+endTimeTxt.getText()+"\nTotal Cost:"+ costTxt.getText()+
                "\n"+(status.equals("Booking")?"Room Booking Successful!":status.equals("Update")?"Room Booking Update Successfully":"Room Booking Cancelled!"+"\n\nThank you for using our service!\n\nHotel Eden Garden,\nInamaluwa,\nSeegiriya"));
    }
}
