package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Customer;
import com.edengardensigiriya.edengarden.dto.Supplier;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.edengardensigiriya.edengarden.model.CustomerModel.stringLength;

public class SupplierModel {
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
        String id="S-00000";
        try {
            result= CrudUtil.execute("SELECT supp_id FROM supplier ORDER BY supp_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            idParts=id.split("-");
            int number=Integer.parseInt(idParts[1]);
            String num=setNextIdValue(++number);
            return "S-"+num;
        } catch (SQLException e) {
            return "S-00000";
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

    public static boolean addSupplier(String ID, String name, String address, String email, String contacts, String itemType, LocalDate conStrtDate, LocalDate conEndDate) {
        try {
            boolean isAffected =CrudUtil.execute("INSERT INTO supplier VALUES(?,?,?,?,?,?,?,?);", ID, name, address, email, contacts,itemType,conStrtDate,conEndDate);
            if (isAffected){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    public static List<Supplier> getAll() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM supplier ORDER BY supp_id;");
        List<Supplier> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            ));
        }
        return data;
    }

    public static boolean updateSupplier(String ID, String name, String address, String email, String contacts, String itemType, LocalDate conStrtDate, LocalDate conEndDate) {
        try {
            boolean isAffected =CrudUtil.execute("UPDATE supplier SET supp_name=?,supp_address=?,supp_email=?,supp_contact=?,supp_items=?,contract_start_date=?,contract_end_date=? WHERE supp_id=?;", name, address, email, contacts,itemType,conStrtDate,conEndDate, ID);
            if (isAffected){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean removeSupplier(String suppId) {
        try {
            boolean isAffected =CrudUtil.execute("DELETE FROM supplier WHERE supp_id=?;", suppId);
            if (isAffected){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Supplier> searchSupplier(String suppId) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM supplier WHERE supp_id=?;",suppId);
        List<Supplier> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            ));
        }
        return data;
    }

    public static String getSuppId() throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT supp_id FROM supplier ORDER BY supp_id DESC LIMIT 1");
            String tempIds="";
            while (resultSet.next()){
                tempIds=resultSet.getString(1);
            }
            DBConnection.getInstance().getConnection().commit();
            return tempIds;
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
            return "";
        }
    }
}
