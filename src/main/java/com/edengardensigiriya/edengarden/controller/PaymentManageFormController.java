package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.dto.Customer;
import com.edengardensigiriya.edengarden.dto.Payment;
import com.edengardensigiriya.edengarden.dto.tm.CustomerTM;
import com.edengardensigiriya.edengarden.dto.tm.PaymentTM;
import com.edengardensigiriya.edengarden.model.CustomerModel;
import com.edengardensigiriya.edengarden.model.PaymentModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.List;

public class PaymentManageFormController {
    public AnchorPane paymentRoot;
    public TextField custIdTxt;
    public TableColumn columnPayId;
    public TableColumn columnCustId;
    public TableColumn columnDateAndTime;
    public TableColumn columnReason;
    public TableColumn columnCost;
    public TableColumn columnStatus;
    public TableView bookingPaymentTbl;
    public TableView rentalPaymentTbl;
    public TableColumn columnRentDateAndTime;
    public TableColumn columnRentPayId;
    public TableColumn columnRentCustId;
    public TableColumn columnRentReason;
    public TableColumn columnRentCost;
    public TableColumn columnRentStatus;
    public TableView transportPaymentTbl;
    public TableColumn columnTransPayId;
    public TableColumn columnTransCustId;
    public TableColumn columnTransDateAndTime;
    public TableColumn columnTransReason;
    public TableColumn columnTransCost;
    public TableColumn columnTransStatus;

    public void initialize() throws SQLException {
        setBookingCellValueFactory();
        setRentalCellValueFactory();
        setTransportCellValueFactory();
        getAllBookingPayments();
        getAllRentalPayments();
        getAllTransportPayments();
    }

    private void getAllTransportPayments() throws SQLException {
        ObservableList<PaymentTM> obList = FXCollections.observableArrayList();
        List<Payment> paymentList = PaymentModel.getAllTransportPay();

        for (Payment payment : paymentList) {
            obList.add(new PaymentTM(
                    payment.getPaymentId(),
                    payment.getCustId(),
                    payment.getDateTime(),
                    payment.getReason(),
                    payment.getCost(),
                    payment.getStatus()
            ));
        }
        transportPaymentTbl.setItems(obList);
    }

    private void getAllBookingPayments() throws SQLException {
        ObservableList<PaymentTM> obList = FXCollections.observableArrayList();
        List<Payment> paymentList = PaymentModel.getAllBookingPay();

        for (Payment payment : paymentList) {
            obList.add(new PaymentTM(
                    payment.getPaymentId(),
                    payment.getCustId(),
                    payment.getDateTime(),
                    payment.getReason(),
                    payment.getCost(),
                    payment.getStatus()
            ));
        }
        bookingPaymentTbl.setItems(obList);
    }

    private void getAllRentalPayments() throws SQLException {
        ObservableList<PaymentTM> obList = FXCollections.observableArrayList();
        List<Payment> paymentList = PaymentModel.getAllRentalPay();

        for (Payment payment : paymentList) {
            obList.add(new PaymentTM(
                    payment.getPaymentId(),
                    payment.getCustId(),
                    payment.getDateTime(),
                    payment.getReason(),
                    payment.getCost(),
                    payment.getStatus()
            ));
        }
        rentalPaymentTbl.setItems(obList);
    }
    void setBookingCellValueFactory() {
        columnPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        columnCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        columnDateAndTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        columnReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        columnCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    void setRentalCellValueFactory() {
        columnRentPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        columnRentCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        columnRentDateAndTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        columnRentReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        columnRentCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        columnRentStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    void setTransportCellValueFactory() {
        columnTransPayId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        columnTransCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        columnTransDateAndTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        columnTransReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        columnTransCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        columnTransStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    public void searchCustomerOnAction(ActionEvent actionEvent) {

    }
}
