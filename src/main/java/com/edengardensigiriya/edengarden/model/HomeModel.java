package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.RoomUpdate;
import com.edengardensigiriya.edengarden.util.CrudUtil;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

public class HomeModel {

    public static Integer getCount(String sql, LocalDate now) throws SQLException {
        ResultSet result=null;
        int count=0;
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            result= CrudUtil.execute(sql,now);
            if(result.next()) {
                count= Integer.parseInt(result.getString(1));
            }
            DBConnection.getInstance().getConnection().commit();
            System.out.println(count);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
            return count;
        }
    }

    public static Integer getTransCount(String sql, LocalDate now, String transport) throws SQLException {
        ResultSet result=null;
        int count=0;
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            result= CrudUtil.execute(sql,now,transport);
            if(result.next()) {
                count= Integer.parseInt(result.getString(1));
            }
            DBConnection.getInstance().getConnection().commit();
            System.out.println(count);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
            return count;
        }
    }

    public static Integer getMonthCount(String sql, int month) throws SQLException {
        ResultSet result=null;
        int count=0;
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            result= CrudUtil.execute(sql,month);
            if(result.next()) {
                count= Integer.parseInt(result.getString(1));
            }
            DBConnection.getInstance().getConnection().commit();
            System.out.println(count);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
            return count;
        }
    }

    public static Integer getMonthTransCount(String sql, int month, String transport) throws SQLException {
        ResultSet result=null;
        int count=0;
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            result= CrudUtil.execute(sql,month,transport);
            if(result.next()) {
                count= Integer.parseInt(result.getString(1));
            }
            DBConnection.getInstance().getConnection().commit();
            System.out.println(count);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
            return count;
        }
    }
}
