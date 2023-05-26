package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Booking;
import com.edengardensigiriya.edengarden.dto.BookingUpdate;
import com.edengardensigiriya.edengarden.dto.UpdateStatus;
import com.edengardensigiriya.edengarden.util.CrudUtil;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.edengardensigiriya.edengarden.model.CustomerModel.stringLength;

public class BookingModel {
    public static ArrayList<String> deluxeRoomNo = new ArrayList<>();
    public static ArrayList<String> standardRoomNo = new ArrayList<>();

    public static String generateID() {
        ResultSet result=null;
        String[] idParts;
        String id="B-00000";
        try {
            result= CrudUtil.execute("SELECT booking_id FROM booking ORDER BY booking_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            idParts=id.split("-");
            int number=Integer.parseInt(idParts[1]);
            String num=setNextIdValue(++number);
            return "B-"+num;
        } catch (SQLException | ClassCastException e) {
            e.printStackTrace();
            return "B-00000";
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

    public static boolean isAdded(String bookingId, String custId, LocalDateTime startDate, long duration, String paymentId, String roomNum, String cost, LocalDateTime nowDateNTime,String availability,String status) throws SQLException {
        boolean isAffectedToPayment = CrudUtil.execute("INSERT INTO payment VALUES(?,?,?,?,?);", paymentId, Double.parseDouble(cost), nowDateNTime,"Room Booking",status);
        boolean isAffectedToBooking = CrudUtil.execute("INSERT INTO booking VALUES(?,?,?,?,?);", bookingId, startDate, String.valueOf(duration), paymentId,custId);
        boolean isAffectedToRoomBooking = CrudUtil.execute("INSERT INTO room_booking VALUES(?,?,?,?);", roomNum, bookingId, nowDateNTime,availability);
        boolean isAffectedToRoom = CrudUtil.execute("UPDATE room SET status=? WHERE room_No=?;","Booked" ,roomNum);

        return (isAffectedToBooking &isAffectedToRoomBooking & isAffectedToPayment & isAffectedToRoom) ? true:false;
    }

    public static String searchCustomer(String custId) {
        String name;
        try {
            ResultSet resultSet=CrudUtil.execute("SELECT cust_name FROM customer WHERE cust_id=?",custId);
            if (resultSet.next()){
                name= resultSet.getString(1);
                return name;
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static void setRoomNumbers(String room_Type) {
        ResultSet roomNoresultSet= null;
        try {
            roomNoresultSet = CrudUtil.execute("SELECT room_No FROM room WHERE room_type=? AND status!=?;",room_Type,"Booked");
            if (room_Type.equals("Deluxe Room")){
                while(roomNoresultSet.next()){
                    if (!deluxeRoomNo.contains(roomNoresultSet.getString(1))){
                        deluxeRoomNo.add(roomNoresultSet.getString(1));
                    }
                }
            }
            else{
                while(roomNoresultSet.next()){
                    if (!standardRoomNo.contains(roomNoresultSet.getString(1))){
                        standardRoomNo.add(roomNoresultSet.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String setSleepCount(String roomNo) {
        ResultSet sleepResultSet= null;
        String sleepCount=null;
        try {
            sleepResultSet= CrudUtil.execute("SELECT bed_count FROM room WHERE room_No=?",roomNo);
            if (sleepResultSet.next()){
                sleepCount=sleepResultSet.getString(1);
                return sleepCount;
            }
            else{
                return sleepCount;
            }
        } catch (SQLException e) {
            return sleepCount;
        }
    }

    public static List<Booking> getAll() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT booking.booking_id,booking.customer_id,customer.cust_name,room_booking.room_No, " +
                "booking.booked_date_time,booking.booking_duration,payment.paid_amount,room_booking.booking_made_date_time," +
                "room_booking.availability FROM booking INNER JOIN room_booking ON booking.booking_id=room_booking.booking_id " +
                "INNER JOIN customer ON customer.cust_id=booking.customer_id INNER JOIN payment ON payment.pay_id=booking.payment_id ORDER BY booking.booking_id;");
        List<Booking> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Booking(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            ));
        }
        return data;
    }

    public static List<BookingUpdate> searchBooking(String bookingId) throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT booking.booking_id,booking.customer_id,customer.cust_name,room.room_type,room_booking.room_No,room.bed_count,booking.booked_date_time,booking.booking_duration,payment.paid_amount FROM booking INNER JOIN room_booking ON room_booking.booking_id=booking.booking_id INNER JOIN customer ON customer.cust_id=booking.customer_id INNER JOIN payment ON payment.pay_id=booking.payment_id INNER JOIN room ON room.room_No=room_booking.room_No WHERE booking.booking_id=? AND room_booking.availability!=?;",bookingId,"Cancelled");
        List<BookingUpdate> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new BookingUpdate(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9)
            ));
        }
        return data;
    }

    public static String getPaymentId(String bookingId) {
        try {
            ResultSet resultSet=CrudUtil.execute("SELECT payment_id FROM booking WHERE booking_id=?",bookingId);
            String paymentId=null;
            if(resultSet.next()){
                paymentId=resultSet.getString(1);
            }
            return paymentId;
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean updateBooking(String bookingId, String custId, LocalDateTime startDate, long duration, String paymentId, String roomNum, String cost, LocalDateTime paidDateTime,String availability, String status) throws SQLException {
        boolean isAffectedToPayment = CrudUtil.execute("UPDATE payment SET paid_amount=?,status=? WHERE pay_id=?;",  Double.parseDouble(cost), status,paymentId);
        boolean isAffectedToBooking = CrudUtil.execute("UPDATE booking SET booked_date_time=?,booking_duration=? WHERE booking_id=?;", startDate, String.valueOf(duration),bookingId);
        boolean isAffectedToRoomBooking = CrudUtil.execute("UPDATE room_booking SET availability=? WHERE room_No=? AND booking_id=?;",availability, roomNum, bookingId);

        return (isAffectedToBooking &isAffectedToRoomBooking & isAffectedToPayment) ? true:false;
    }

    public static LocalDateTime getPaidDateTime(String bookingId) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ResultSet resultSet=CrudUtil.execute("SELECT booking_made_date_time FROM room_booking WHERE booking_id=?",bookingId);
            LocalDateTime paidDateTime=null;
            if(resultSet.next()){
                paidDateTime= LocalDateTime.parse(resultSet.getString(1),formatter);
            }
            return paidDateTime;
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean cancelBooking(String bookingId,String paymentId,String status,String roomNum) throws SQLException {
        boolean isAffectedToPayment = CrudUtil.execute("UPDATE payment SET status=? WHERE pay_id=?;", status,paymentId);
        boolean isAffectedToRoomBooking = CrudUtil.execute("UPDATE room_booking SET availability=? WHERE room_No=? AND booking_id=?;",status, roomNum, bookingId);
        boolean isAffectedToRoom = CrudUtil.execute("UPDATE room SET status=? WHERE room_No=?;","Available" ,roomNum);

        System.out.println(isAffectedToPayment+""+isAffectedToRoomBooking+""+isAffectedToRoom);
        return (isAffectedToRoomBooking & isAffectedToPayment & isAffectedToRoom) ? true:false;
    }

    public static void  updateStatus() throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            /*ResultSet result=CrudUtil.execute("SELECT booking.booking_id,booking.booked_date_time,booking.booking_duration FROM booking INNER JOIN room_booking ON room_booking.booking_id=booking.booking_id;");
            ArrayList<UpdateStatus> temp=new ArrayList<>();
            while (result.next()){
                temp.add(new UpdateStatus(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3)
                        ));
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            ArrayList<String> tempIds=new ArrayList<>();
            for (UpdateStatus update:temp) {
                LocalDateTime endDate = LocalDateTime.parse(update.getBookedDate()).plusHours(Long.parseLong(update.getDuration()));
                System.out.println(endDate);
                if (endDate.isAfter(LocalDateTime.now())){
                    tempIds.add(update.getId());
                }
            }*/
            ResultSet resultSet=CrudUtil.execute("SELECT booking.booking_id FROM booking INNER JOIN room_booking ON room_booking.booking_id=booking.booking_id WHERE booking.booked_date_time<? AND room_booking.availability!=?;",LocalDateTime.now(),"Cancelled");
            ArrayList<String> tempIds=new ArrayList<>();
            while (resultSet.next()){
                tempIds.add(resultSet.getString(1));
            }
            boolean isAffected=false;
            for (int i = 0; i < tempIds.size(); i++) {
                isAffected=CrudUtil.execute("UPDATE room_booking SET availability=? WHERE booking_id=?;","End",tempIds.get(i));
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static String getEmail(String id) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT cust_email FROM customer WHERE cust_id=?;",id);
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
            result= CrudUtil.execute("SELECT booking_id FROM booking ORDER BY booking_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            return id;
        } catch (SQLException e) {
            return id;
        }
    }
}
