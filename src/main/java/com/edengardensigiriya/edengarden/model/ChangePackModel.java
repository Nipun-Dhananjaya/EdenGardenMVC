package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.controller.ChangePacksFormController;
import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Customer;
import com.edengardensigiriya.edengarden.util.CrudUtil;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChangePackModel {
    public static void setArrayList() throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet = CrudUtil.execute("SELECT cost_per_day FROM room;");
            ArrayList<String> tempList=new ArrayList<>();
            while (resultSet.next()) {
                tempList.add(resultSet.getString(1));
            }
            for (int i = 0; i < tempList.size(); i++) {
                if (!(ChangePacksFormController.roomPrices.contains(tempList.get(i)))){
                    ChangePacksFormController.roomPrices.add(tempList.get(i));
                }else{
                    continue;
                }
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static String getSleepCount(String price) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet = CrudUtil.execute("SELECT bed_count FROM room WHERE cost_per_day=?;",price);
            ArrayList<String> tempList=new ArrayList<>();
            while (resultSet.next()) {
                tempList.add(resultSet.getString(1));
            }
            DBConnection.getInstance().getConnection().commit();
            if(!tempList.isEmpty()){
                return tempList.get(0);
            }else{
                return "No Rooms Available";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
            return "No Rooms Available";
        }
    }

    public static boolean changePakage(String packageOldPrice,String packageNewPrice) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT room_No FROM room WHERE cost_per_day=?;",packageOldPrice);
        ArrayList<String> tempList=new ArrayList<>();
        boolean isUpdated=false;
        while (resultSet.next()) {
            tempList.add(resultSet.getString(1));
        }
        for (int i = 0; i < tempList.size(); i++) {
            isUpdated=CrudUtil.execute("UPDATE room SET cost_per_day=? WHERE room_No=?;",packageNewPrice,tempList.get(i));
        }
        if (isUpdated){
            return true;
        }else{
            return false;
        }
    }
}
