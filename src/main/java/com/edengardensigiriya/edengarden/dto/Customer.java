package com.edengardensigiriya.edengarden.dto;

import javafx.scene.control.Button;

public class Customer {
    private String custId;
    private String custName;
    private String custNic;
    private String custEmail;
    private String custAddress;
    private String custContact;
    private String custGender;

    public Customer(String custId, String custName, String custNic, String custEmail,String custAddress,String custContact,String custGender) {
        this.custId=custId;
        this.custName=custName;
        this.custNic=custNic;
        this.custEmail=custEmail;
        this.custAddress=custAddress;
        this.custContact=custContact;
        this.custGender=custGender;
    }
    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNic() {
        return custNic;
    }

    public void setCustNic(String custNic) {
        this.custNic = custNic;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustContact() {
        return custContact;
    }

    public void setCustContact(String custContact) {
        this.custContact = custContact;
    }

    public String getCustGender() {
        return custGender;
    }

    public void setCustGender(String custGender) {
        this.custGender = custGender;
    }
}
