package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Customer;
import com.edengardensigiriya.edengarden.dto.Employer;
import com.edengardensigiriya.edengarden.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.edengardensigiriya.edengarden.model.CustomerModel.stringLength;

public class EmployerModel {
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
        String id="E-00000";
        try {
            result= CrudUtil.execute("SELECT emp_id FROM employer ORDER BY emp_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            System.out.println(id);
            idParts=id.split("-");
            System.out.println(idParts.length);
            int number=Integer.parseInt(idParts[1]);
            String num=setNextIdValue(++number);
            return "E-"+num;
        } catch (SQLException e) {
            return "E-00000";
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

    public static boolean addEmployer(String ID, String name, String nic, String address, String email, String contacts,
                                      LocalDate dob, String gender, String jobRole, String salary, LocalDate serviceStrtDate,
                                      LocalDate serviceEndDate) {
        try {
            boolean isAffected =CrudUtil.execute("INSERT INTO employer VALUES(?,?,?,?,?,?,?,?,?,?,?,?);", ID, name,
                    nic, address, email, contacts, dob, gender, jobRole, salary, serviceStrtDate, serviceEndDate);
            if (isAffected){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    public static List<Employer> getAll() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM employer ORDER BY emp_id;");
        List<Employer> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Employer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9),
                    resultSet.getString(10),
                    resultSet.getString(11),
                    resultSet.getString(12)
            ));
        }
        return data;
    }

    public static boolean updateEmployer(String ID, String name, String nic, String address, String email, String contacts,
                                         LocalDate dob, String gender, String jobRole, String salary, LocalDate serviceStrtDate,
                                         LocalDate serviceEndDate) {
        try {
            boolean isAffected =CrudUtil.execute("UPDATE employer SET emp_name=?,emp_nic=?,emp_address=?,emp_email=?,emp_contact=?,emp_dob=?,gender=?,job_role=?,monthly_salary=?,entered_date=?,service_end_date=? WHERE emp_id=?;", name,
                    nic, address, email, contacts, dob, gender, jobRole, salary, serviceStrtDate, serviceEndDate,ID);
            if (isAffected){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<Employer> searchEmployer(String empId) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM employer WHERE emp_id=?;",empId);
        List<Employer> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Employer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9),
                    resultSet.getString(10),
                    resultSet.getString(11),
                    resultSet.getString(12)
            ));
        }
        return data;
    }

    public static String getEmpId() throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT emp_id FROM employer ORDER BY emp_id DESC LIMIT 1;");
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
