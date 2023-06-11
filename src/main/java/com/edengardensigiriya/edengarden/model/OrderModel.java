package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Booking;
import com.edengardensigiriya.edengarden.dto.Order;
import com.edengardensigiriya.edengarden.dto.OrderItem;
import com.edengardensigiriya.edengarden.dto.OrderUpdate;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.edengardensigiriya.edengarden.model.CustomerModel.stringLength;

public class OrderModel {
    public static String generateID() {
        ResultSet result=null;
        String[] idParts;
        String id="O-00000";
        try {
            result= CrudUtil.execute("SELECT ord_id FROM order_ ORDER BY ord_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            idParts=id.split("-");
            int number=Integer.parseInt(idParts[1]);
            String num=setNextIdValue(++number);
            return "O-"+num;
        } catch (SQLException e) {
            return "O-00000";
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

    public static boolean setOrder(String ordId, String suppId, List<OrderItem> orderItem, LocalDateTime now, LocalDateTime deliverDate,String cost) {
        try {
            int length= orderItem.size();
            String itmCode = null;
            boolean isAddedOrd=CrudUtil.execute("INSERT INTO order_ VALUES (?,?,?,?,?);",ordId,suppId,now,deliverDate,"Active");
            System.out.println("isAd:   "+ isAddedOrd);
            System.out.println(length);
            boolean isAddedPurchDetls=false;
            for (int j = 0; j < length; j++) {
                OrderItem itm= orderItem.get(j);
                ResultSet resultSet=CrudUtil.execute("SELECT item_code FROM item WHERE description=?;",itm.getItem());
                if (resultSet.next()){
                    itmCode=resultSet.getString(1);
                }
                isAddedPurchDetls = CrudUtil.execute("INSERT INTO purchase_detail VALUES (?,?,?,?);", ordId,itmCode,Double.parseDouble(String.valueOf(itm.getQty())),cost!=null? Double.parseDouble(String.valueOf(cost)):0.00);
                System.out.println("isP:   "+isAddedPurchDetls);
            }
            if (isAddedOrd & isAddedPurchDetls){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Order> getAll() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT order_.ord_id,order_.supp_id,item.description,purchase_detail.bought_qty,order_.ord_made_date,order_.ord_dilever_date,purchase_detail.ord_cost,order_.status FROM order_ INNER JOIN purchase_detail ON purchase_detail.order_id=order_.ord_id INNER JOIN item ON item.item_code=purchase_detail.itm_code ORDER BY order_.ord_id;");
        List<Order> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Order(
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

    public static boolean updateOrder(String ordId, String suppId, List<OrderItem> orderItem, LocalDateTime now, LocalDateTime deliverDate,String cost) {
        try {
            int length= orderItem.size();
            String itmCode = null;
            boolean isUpdatedOrd=CrudUtil.execute("UPDATE order_ SET supp_id=?,ord_dilever_date=? WHERE ord_id=?;",suppId,deliverDate,ordId);
            boolean isUpdatedPurchDetls=false;
            for (int j = 0; j < length; j++) {
                OrderItem itm= orderItem.get(j);
                ResultSet resultSet=CrudUtil.execute("SELECT item_code FROM item WHERE description=?;",itm.getItem());
                if (resultSet.next()){
                    itmCode=resultSet.getString(1);
                }
                System.out.println(itmCode);
                isUpdatedPurchDetls = CrudUtil.execute("UPDATE purchase_detail SET bought_qty=?,ord_cost=? WHERE order_id=? AND itm_code=?;",Double.parseDouble(String.valueOf(itm.getQty())),cost!=null? Double.parseDouble(String.valueOf(cost)):0.00, ordId,itmCode);
                if (isUpdatedPurchDetls){
                    continue;
                }else{
                    isUpdatedPurchDetls = CrudUtil.execute("INSERT INTO purchase_detail VALUES(?,?,?,?);", ordId,itmCode,Double.parseDouble(String.valueOf(itm.getQty())),cost!=null? Double.parseDouble(String.valueOf(cost)):0.00);
                }
            }
            System.out.println(isUpdatedOrd);
            System.out.println(isUpdatedPurchDetls);
            if (isUpdatedOrd & isUpdatedPurchDetls){
                return true;
            }else{
                System.out.println(1234);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean cancelOrder(String ordId) {
        try {
            boolean isUpdatedOrd=CrudUtil.execute("UPDATE order_ SET status=? WHERE ord_id=?;","Cancelled",ordId);
            if (isUpdatedOrd ){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<OrderUpdate> searchOrderTxt(String ordId) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT order_.ord_id,order_.supp_id,order_.ord_dilever_date,purchase_detail.ord_cost FROM order_ INNER JOIN purchase_detail ON purchase_detail.order_id=order_.ord_id WHERE order_.ord_id=?;",ordId);
        List<OrderUpdate> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new OrderUpdate(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return data;
    }

    public static List<OrderItem> getAllItemsOfOrder(String ordId) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT item.description,purchase_detail.bought_qty FROM order_ INNER JOIN purchase_detail ON purchase_detail.order_id=order_.ord_id INNER JOIN item ON item.item_code=purchase_detail.itm_code WHERE order_.ord_id=?;",ordId);
        List<OrderItem> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new OrderItem(
                    resultSet.getString(1),
                    resultSet.getString(2)
            ));
        }
        return data;
    }
    public static String getEmail(String id) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT supp_email FROM supplier WHERE supp_id=?;",id);
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
    public static String getBookingId() {
        ResultSet result=null;
        String id="";
        try {
            result= CrudUtil.execute("SELECT ord_id FROM order_ ORDER BY ord_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            return id;
        } catch (SQLException e) {
            return id;
        }
    }
}
