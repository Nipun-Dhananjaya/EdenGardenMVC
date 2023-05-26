package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.*;
import com.edengardensigiriya.edengarden.dto.tm.CustomerTM;
import com.edengardensigiriya.edengarden.dto.tm.SupplierTM;
import com.edengardensigiriya.edengarden.model.*;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SupplierManageFormController {
    public AnchorPane suppRoot;
    public TextField idTxt;
    public TextField nameTxt;
    public TextField itemTypeTxt;
    public TextField emailTxt;
    public TextField addressTxt;
    public TextField contactTxt;
    public Button addContactBtn;
    public TableView suppTbl;
    public Button addBtn;
    public Button updateBtn;
    public Button removeBtn;
    public DatePicker startDtPckr;
    public DatePicker endDtPckr;
    public TableColumn columnId;
    public TableColumn columnName;
    public TableColumn columnItmType;
    public TableColumn columnEmail;
    public TableColumn columnAddress;
    public TableColumn columnContact;
    public TableColumn columnContractStartDate;
    public TableColumn columnContractEndDate;
    public ComboBox contactCmbBx;
    static String con;

    public void initialize() throws SQLException {
        setCellValueFactory();
        getAllSuppliers();
    }

    private void getAllSuppliers() throws SQLException {
        ObservableList<SupplierTM> obList = FXCollections.observableArrayList();
        List<Supplier> supplierList = SupplierModel.getAll();

        for (Supplier supplier : supplierList) {
            obList.add(new SupplierTM(
                    supplier.getSuppId(),
                    supplier.getSuppName(),
                    supplier.getSuppAddress(),
                    supplier.getSuppEmail(),
                    supplier.getSuppContact(),
                    supplier.getItemType(),
                    supplier.getContactStartDate(),
                    supplier.getContactEndDate()
            ));
        }
        suppTbl.setItems(obList);
    }
    void setCellValueFactory() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("suppId"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("suppName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("suppAddress"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("suppEmail"));
        columnContact.setCellValueFactory(new PropertyValueFactory<>("suppContact"));
        columnItmType.setCellValueFactory(new PropertyValueFactory<>("ItemType"));
        columnContractStartDate.setCellValueFactory(new PropertyValueFactory<>("contactStartDate"));
        columnContractEndDate.setCellValueFactory(new PropertyValueFactory<>("contactEndDate"));
    }
    public void idSearchOnAction(ActionEvent actionEvent) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            SupplierModel.contact.clear();
            List<Supplier> suppList = SupplierModel.searchSupplier(idTxt.getText());
            if (!suppList.isEmpty()){
                for (Supplier supplier : suppList) {
                    nameTxt.setText(supplier.getSuppName());
                    emailTxt.setText(supplier.getSuppEmail());
                    addressTxt.setText(supplier.getSuppAddress());
                    itemTypeTxt.setText(supplier.getItemType());
                    contactCmbBx.setItems(getContactObList(supplier.getSuppContact()));
                    startDtPckr.setValue(LocalDate.parse(supplier.getContactStartDate()));
                    endDtPckr.setValue(LocalDate.parse(supplier.getContactEndDate()));
                    idTxt.setDisable(true);
                }
            }else{
                new Alert(Alert.AlertType.WARNING, "Supplier Not Found!").showAndWait();
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
        }finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public void addSupplierOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected = SupplierModel.addSupplier(SupplierModel.generateID(),
                        nameTxt.getText(), addressTxt.getText(), emailTxt.getText(), String.join(" , ", SupplierModel.contact),
                        itemTypeTxt.getText(),startDtPckr.getValue(),endDtPckr.getValue());
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Added!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                sendMail("Add");
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

    public void updateSupplierOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isAffected=false;
            if (isCorrectPattern()){
                isAffected = SupplierModel.updateSupplier(idTxt.getText(),
                        nameTxt.getText(), addressTxt.getText(), emailTxt.getText(), String.join(" , ", SupplierModel.contact),
                        itemTypeTxt.getText(),startDtPckr.getValue(),endDtPckr.getValue());
            }
            if (isAffected) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Updated!").showAndWait();
                DBConnection.getInstance().getConnection().commit();
                sendMail("Update");
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

    public void removeSupplierOnAction(ActionEvent actionEvent) throws SQLException, MessagingException, GeneralSecurityException, IOException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            Optional<ButtonType> comfirm=new Alert(Alert.AlertType.CONFIRMATION, "Do you want to remove supplier?").showAndWait();
            if (comfirm.isPresent()){
                boolean isAffected=false;
                if (isCorrectPattern()){
                    isAffected = SupplierModel.removeSupplier(idTxt.getText());
                }
                if (isAffected) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier Removed!").showAndWait();
                    DBConnection.getInstance().getConnection().commit();
                    sendMail("Remove");
                    idTxt.setDisable(false);
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
    public void resetPage() throws SQLException {
        idTxt.setText("");
        nameTxt.setText("");
        emailTxt.setText("");
        addressTxt.setText("");
        contactTxt.setText("");
        SupplierModel.contact.clear();
        setCellValueFactory();
        getAllSuppliers();
    }

    public void setTxtBxValueOnAction(ActionEvent actionEvent) {
        contactTxt.setText(String.valueOf(contactCmbBx.getSelectionModel().getSelectedItem()));
        con = contactTxt.getText();
        System.out.println(con);
    }
    private ObservableList getContactObList(String custContact) {
        String[] contacts=custContact.split(" , ");
        List<String> conList = Arrays.asList(contacts);
        ObservableList<String> cont = FXCollections.observableList(conList);
        int length=contacts.length;
        while (length>0){
            EmployerModel.contact.add(contacts[length-1]);
            length-=1;
        }
        return cont;
    }

    public void addContactOnAction(ActionEvent actionEvent) {
        if (contactCmbBx.getItems().contains(con)) {
            System.out.println(" 12: "+con);
            SupplierModel.contact.remove(con);
        }
        if (RegExPatterns.getMobilePattern().matcher(contactTxt.getText()).matches()){
            boolean isAlreadyHas = SupplierModel.addContact(contactTxt.getText());
            if (!isAlreadyHas) {
                new Alert(Alert.AlertType.WARNING, "Contact Already Added!").showAndWait();
            } else {
                ObservableList<String> cont = FXCollections.observableList(SupplierModel.contact);
                contactCmbBx.setItems(cont);
                contactCmbBx.setValue("Contact List");
                contactTxt.setText("");
            }
        }
        else{
            new Alert(Alert.AlertType.WARNING, "Invalid Contact Number!").showAndWait();
        }
    }
    private boolean isCorrectPattern(){
        if ((RegExPatterns.getEmailPattern().matcher(emailTxt.getText()).matches()) && RegExPatterns.getNamePattern().matcher(nameTxt.getText()).matches() && RegExPatterns.getAddressPattern().matcher(addressTxt.getText()).matches()){
            return true;
        }
        return false;
    }
    public void sendMail(String status) throws MessagingException, GeneralSecurityException, IOException, SQLException {
        SendEmail.sendMail(emailTxt.getText(),
                (status.equals("Add")?"Register As Supplier!":status.equals("Update")?"Update Supplier Details!":"Remove Supplier"),
                "Your Supplier ID:"+(status.equals("Add")?SupplierModel.getSuppId():status.equals("Update")?idTxt.getText():idTxt.getText())+
                        "\nItem Type:"+ itemTypeTxt.getText()+"\nContract Start From:"+startDtPckr.getValue()+"\nContract Valid To:"+endDtPckr.getValue()+
                        "\n"+(status.equals("Add")?"Registered As Supplier Successfully!":status.equals("Update")?"Supplier Details Update Successfully":"Supplier Removed Successfully!"+"\n\nThank you!\n\nHotel Eden Garden,\nInamaluwa,\nSeegiriya"));
    }
}
