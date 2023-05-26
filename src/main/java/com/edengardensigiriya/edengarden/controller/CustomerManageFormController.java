package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Customer;
import com.edengardensigiriya.edengarden.dto.RegExPatterns;
import com.edengardensigiriya.edengarden.dto.SendEmail;
import com.edengardensigiriya.edengarden.dto.tm.CustomerTM;
import com.edengardensigiriya.edengarden.model.BookingModel;
import com.edengardensigiriya.edengarden.model.CustomerModel;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerManageFormController{
    public AnchorPane custRoot;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField nicTxt;
    public TextField emailTxt;
    public TextField addressTxt;
    public TextField contactTxt;
    public Button addContactBtn;
    public RadioButton maleRdBtn;
    public ToggleGroup gender;
    public RadioButton femaleRdBtn;
    public TableView<CustomerTM> custTbl;
    public Button addBtn;
    public TableColumn columnCustId;
    public TableColumn columnCustName;
    public TableColumn columnCustNic;
    public TableColumn columnCustEmail;
    public TableColumn columnCustAddress;
    public TableColumn columnCustContact;
    public TableColumn columnCustGender;
    public Button updateBtn;
    public ComboBox contactCmbBx;
    static String con;

    public void initialize() throws SQLException {
        setCellValueFactory();
        getAllCustomers();
    }

    private void getAllCustomers() throws SQLException {
        ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
        List<Customer> cusList = CustomerModel.getAll();

        for (Customer customer : cusList) {
            obList.add(new CustomerTM(
                    customer.getCustId(),
                    customer.getCustName(),
                    customer.getCustNic(),
                    customer.getCustEmail(),
                    customer.getCustAddress(),
                    customer.getCustContact(),
                    customer.getCustGender()
            ));
        }
        custTbl.setItems(obList);
        contactCmbBx.getSelectionModel().getSelectedItem();
    }

    void setCellValueFactory() {
        columnCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        columnCustName.setCellValueFactory(new PropertyValueFactory<>("custName"));
        columnCustNic.setCellValueFactory(new PropertyValueFactory<>("custNic"));
        columnCustEmail.setCellValueFactory(new PropertyValueFactory<>("custEmail"));
        columnCustAddress.setCellValueFactory(new PropertyValueFactory<>("custAddress"));
        columnCustContact.setCellValueFactory(new PropertyValueFactory<>("custContact"));
        columnCustGender.setCellValueFactory(new PropertyValueFactory<>("custGender"));
    }

    public void idSearchOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            CustomerModel.contact.clear();
            List<Customer> cusList = CustomerModel.searchCustomer(idTxt.getText());
            if (!cusList.isEmpty()){
                for (Customer customer : cusList) {
                    nameTxt.setText(customer.getCustName());
                    nicTxt.setText(customer.getCustNic());
                    emailTxt.setText(customer.getCustEmail());
                    addressTxt.setText(customer.getCustAddress());
                    contactCmbBx.setItems(getContactObList(customer.getCustContact()));
                    if (customer.getCustGender().equals("MALE")){
                        maleRdBtn.setSelected(true);
                    }else{
                        femaleRdBtn.setSelected(true);
                    }
                    idTxt.setDisable(true);
                }
            }else{
                new Alert(Alert.AlertType.WARNING, "Customer Not Found!").showAndWait();
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    private ObservableList getContactObList(String custContact) {
        String[] contacts=custContact.split(" , ");
        List<String> conList = Arrays.asList(contacts);
        ObservableList<String> cont = FXCollections.observableList(conList);
        int length=contacts.length;
        while (length>0){
            CustomerModel.contact.add(contacts[length-1]);
            length-=1;
        }
        return cont;
    }

    public void addContactOnAction(ActionEvent actionEvent) {
        if (contactCmbBx.getItems().contains(con)) {
            CustomerModel.contact.remove(con);
        }
        if (RegExPatterns.getMobilePattern().matcher(contactTxt.getText()).matches()){
            boolean isAlreadyHas = CustomerModel.addContact(contactTxt.getText());
            if (!isAlreadyHas) {
                new Alert(Alert.AlertType.WARNING, "Contact Already Added!").showAndWait();
            } else {
                ObservableList<String> cont = FXCollections.observableList(CustomerModel.contact);
                contactCmbBx.setItems(cont);
                contactCmbBx.setValue("Contact List");
                contactTxt.setText("");
            }
        }
        else{
            new Alert(Alert.AlertType.WARNING, "Invalid Contact Number!").showAndWait();
        }
    }

    public void addCustomerOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isAffected=false;
            if (isCorrectPattern()){
                System.out.println(123);
                isAffected = CustomerModel.addCustomer(CustomerModel.generateID(),
                        nameTxt.getText(), nicTxt.getText(), addressTxt.getText(), emailTxt.getText(), String.join(" , ", CustomerModel.contact),
                        maleRdBtn.isSelected() ? "MALE" : "FEMALE");
            }
            if (isAffected) {
                System.out.println(456);
                new Alert(Alert.AlertType.INFORMATION, "Customer Added!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                sendMail();
                resetPage();
            } else {
                System.out.println(789);
                new Alert(Alert.AlertType.WARNING, "Re-Check Submitted Details!").showAndWait();
            }
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
            boolean isAffected =false;
            if (isCorrectPattern()){
                isAffected = CustomerModel.updateCustomer(idTxt.getText(),
                        nameTxt.getText(), nicTxt.getText(), addressTxt.getText(), emailTxt.getText(), String.join(" , ", CustomerModel.contact),
                        maleRdBtn.isSelected() ? "MALE" : "FEMALE");
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Updated!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                idTxt.setDisable(false);
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
        setCellValueFactory();
        getAllCustomers();
        idTxt.setText("");
        nameTxt.setText("");
        nicTxt.setText("");
        emailTxt.setText("");
        addressTxt.setText("");
        contactTxt.setText("");
        contactCmbBx.setValue("Contact List");
        CustomerModel.contact.clear();
    }

    public void setTxtBxValueOnAction(ActionEvent actionEvent) {
        contactTxt.setText(String.valueOf(contactCmbBx.getSelectionModel().getSelectedItem()));
        con = contactTxt.getText();
    }
    private boolean isCorrectPattern(){
        if ((RegExPatterns.getEmailPattern().matcher(emailTxt.getText()).matches()) && RegExPatterns.getNamePattern().matcher(nameTxt.getText()).matches() && (RegExPatterns.getIdPattern().matcher(nicTxt.getText()).matches() ||RegExPatterns.getOldIDPattern().matcher(nicTxt.getText()).matches() ) && RegExPatterns.getAddressPattern().matcher(addressTxt.getText()).matches()){
            return true;
        }
        return false;
    }
    public void sendMail() throws MessagingException, GeneralSecurityException, IOException {
        SendEmail.sendMail(emailTxt.getText(),"Customer Verification","Dear Customer," +
                "\nYour Customer ID:"+CustomerModel.getCustId()+"\nName:"+nameTxt.getText()+"\nRegistered time: "+ LocalDateTime.now()+"\n\nThank you for using our service!\n\nHotel Eden Garden,\nInamaluwa,\nSeegiriya");
    }
}
