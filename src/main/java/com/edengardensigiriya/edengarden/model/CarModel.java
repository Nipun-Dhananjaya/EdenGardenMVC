package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.dto.Car;
import com.edengardensigiriya.edengarden.dto.Room;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarModel {
    public static boolean addCar(String regNo, String carType, String colour, String brand) {
        try {
            boolean isAdded= CrudUtil.execute("INSERT INTO car VALUES (?,?,?,?,?);",regNo,brand,carType,colour,"Available");
            if (isAdded){
                return true;
            }else{
                System.out.println(123);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Car> getAll() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM car ORDER BY car_reg_num;");
        List<Car> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Car(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }

    public static boolean updateCar(String regNo, String carType, String colour, String brand) {
        try {
            boolean isUpdated= CrudUtil.execute("UPDATE car SET brand=?,car_type=?,colour=? WHERE car_reg_num=?;",brand,carType,colour,regNo);
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

    public static boolean removeCar(String regNo) {
        try {
            boolean isDeleted= CrudUtil.execute("DELETE FROM car WHERE car_reg_num=?;",regNo);
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

    public static List<Car> searchCar(String regNo) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM car WHERE car_reg_num=?;",regNo);
        List<Car> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Car(
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
