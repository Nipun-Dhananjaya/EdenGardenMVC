package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.dto.Bicycle;
import com.edengardensigiriya.edengarden.dto.Car;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BicycleModel {
    public static boolean addBicycle(String bicycleNo, String bicycleType, String colour, String brand) {
        try {
            boolean isAdded= CrudUtil.execute("INSERT INTO bicycle VALUES (?,?,?,?,?);",bicycleNo,brand,bicycleType,colour,"Available");
            if (isAdded){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Bicycle> getAll() throws SQLException {
        ResultSet resultSet= CrudUtil.execute("SELECT * FROM bicycle ORDER BY bicycle_id;");
        List<Bicycle> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Bicycle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }
    public static String generateID() {
        ResultSet result=null;
        String[] idParts;
        String id="Bike-000";
        try {
            result= CrudUtil.execute("SELECT bicycle_id FROM bicycle ORDER BY bicycle_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            idParts=id.split("-");
            int number=Integer.parseInt(idParts[1]);
            String num=setNextIdValue(++number);
            return "Bike-"+num;
        } catch (SQLException e) {
            return "Bike-000";
        }
    }

    private static String setNextIdValue(int number) {
        String returnVal="";
        int length=String.valueOf(number).length();
        if(length<3){
            int difference=3-length;
            for (int i = 0; i < difference; i++) {
                returnVal+="0";
            }
            returnVal+=String.valueOf(number);
            return returnVal;
        }
        return String.valueOf(number);
    }

    public static boolean updateBicycle(String bicycleNo, String bicycleType, String colour, String brand) {
        try {
            boolean isUpdated= CrudUtil.execute("UPDATE bicycle SET brand=?,bicycle_type=?,colour=? WHERE bicycle_id=?;",brand,bicycleType,colour,bicycleNo);
            if (isUpdated){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeBicycle(String bicycleNo) {
        try {
            boolean isDeleted= CrudUtil.execute("DELETE FROM bicycle WHERE bicycle_id=?;",bicycleNo);
            if (isDeleted){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Bicycle> searchBicycle(String bicycleNo) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM bicycle WHERE bicycle_id=?;",bicycleNo);
        List<Bicycle> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Bicycle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }
}
