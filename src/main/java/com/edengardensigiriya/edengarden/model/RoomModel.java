package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.dto.Customer;
import com.edengardensigiriya.edengarden.dto.Room;
import com.edengardensigiriya.edengarden.dto.RoomUpdate;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomModel {
    public static boolean addRoom(String roomNo, String roomType, int sleepCount, double costPerDay) {
        try {
            boolean isAdded=CrudUtil.execute("INSERT INTO room VALUES (?,?,?,?,?);",roomNo,roomType,sleepCount,costPerDay,"Available");
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
    public static String generateID() {
        ResultSet result=null;
        String id="0010";
        try {
            result= CrudUtil.execute("SELECT room_no FROM room ORDER BY room_no DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            int number=Integer.parseInt(id);
            String num=setNextIdValue(++number);
            return num;
        } catch (SQLException e) {
            return "0010";
        }
    }
    private static String setNextIdValue(int number) {
        String returnVal="";
        int length=String.valueOf(number).length();
        if(length<4){
            int difference=4-length;
            for (int i = 0; i < difference; i++) {
                returnVal+="0";
            }
            returnVal+=String.valueOf(number);
            return returnVal;
        }
        return String.valueOf(number);
    }
    public static List<Room> getAll() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT room_No,room_type,bed_count,cost_per_day,status FROM room ORDER BY room_No;");
        List<Room> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Room(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }

    public static boolean updateRoom(String roomNo, String roomType, int sleepCount, double costPerDay) {
        try {
            boolean isUpdated=CrudUtil.execute("UPDATE room SET room_type=?,bed_count=?,cost_per_day=? WHERE room_No=?;",roomType,sleepCount,costPerDay,roomNo);
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

    public static boolean removeRoom(String roomNo) {
        try {
            boolean isRemoved=CrudUtil.execute("DELETE FROM room WHERE room_No=?;",roomNo);
            if (isRemoved){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<RoomUpdate> searchRoom(String roomNo) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM room WHERE room_No=?;",roomNo);
        List<RoomUpdate> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new RoomUpdate(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return data;
    }
}
