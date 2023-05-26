package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.dto.Customer;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    static final int stringLength = 5;
    public static ArrayList<String> contact = new ArrayList<>();
    public static boolean addContact(String tele) {
        if (!contact.contains(tele)){
            contact.add(tele);
            return true;
        }
        else{
            return false;
        }
    }
    public static String generateID() {
        ResultSet result=null;
        String[] idParts;
        String id="C-00000";
        try {
            result= CrudUtil.execute("SELECT cust_id FROM customer ORDER BY cust_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            idParts=id.split("-");
            int number=Integer.parseInt(idParts[1]);
            String num=setNextIdValue(++number);
            return "C-"+num;
        } catch (SQLException e) {
            return "C-00000";
        }
    }

    private static String setNextIdValue(int number) {
        String returnVal="";
        int length=String.valueOf(number).length();
        if(length<stringLength){
            int difference=stringLength-length;
            for (int i = 0; i < difference; i++) {
                returnVal+="0";
            }
            returnVal+=String.valueOf(number);
            return returnVal;
        }
        return String.valueOf(number);
    }

    public static List<Customer> getAll() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM customer ORDER BY cust_id;");
        List<Customer> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
            ));
        }
        return data;
    }

    public static List<Customer> searchCustomer(String idTxt) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM customer WHERE cust_id=?;",idTxt);
        List<Customer> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
            ));
        }
        return data;
    }

    public static boolean addCustomer(String ID, String name, String nic, String address, String email, String contacts, String gender) {
        try {
            /*if (!(name.matches("^[a-zA-Z][ ]*$") | (!email.matches("^(.+)@(\\S+) $")) | (nic.matches()))){

            }*/
            boolean isAffected =CrudUtil.execute("INSERT INTO customer VALUES(?,?,?,?,?,?,?);", ID, name, nic, email, address, contacts, gender);
            if (isAffected){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateCustomer(String ID, String name, String nic, String address, String email, String contacts, String gender) {
        try {
            boolean isAffected =CrudUtil.execute("UPDATE customer SET cust_name=?,cust_nic=?,cust_address=?,cust_email=?," +
                    "cust_contact=?,gender=? WHERE cust_id=?;", name, nic, address, email, contacts, gender, ID);
            if (isAffected){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getCustId() {
        ResultSet result=null;
        String id="";
        try {
            result= CrudUtil.execute("SELECT cust_id FROM customer ORDER BY cust_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            return id;
        } catch (SQLException e) {
            return id;
        }
    }
}
