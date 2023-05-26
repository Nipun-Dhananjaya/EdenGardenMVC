package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.edengardensigiriya.edengarden.dto.Booking;
import com.edengardensigiriya.edengarden.dto.BookingUpdate;
import com.edengardensigiriya.edengarden.dto.Rental;
import com.edengardensigiriya.edengarden.dto.RentalUpdate;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.edengardensigiriya.edengarden.model.CustomerModel.stringLength;

public class RentalModel {
    public static String searchCustomer(String custId) {
        String name;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT cust_name FROM customer WHERE cust_id=?", custId);
            if (resultSet.next()) {
                name = resultSet.getString(1);
                return name;
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public static String generateID() {
        ResultSet result = null;
        String[] idParts;
        String id = "R-00000";
        try {
            result = CrudUtil.execute("SELECT rental_id FROM rental ORDER BY rental_id DESC LIMIT 1");
            if (result.next()) {
                id = result.getString(1);
            }
            idParts = id.split("-");
            int number = Integer.parseInt(idParts[1]);
            String num = setNextIdValue(++number);
            return "R-" + num;
        } catch (SQLException | ClassCastException e) {
            e.printStackTrace();
            return "R-00000";
        }
    }

    private static String setNextIdValue(int number) {
        String returnVal = "";
        int length = String.valueOf(number).length();
        if (length < stringLength) {
            int difference = stringLength - length;
            for (int i = 0; i < difference; i++) {
                returnVal += "0";
            }
            returnVal += String.valueOf(number);
            return returnVal;
        }
        return String.valueOf(number);
    }

    public static boolean isAdded(String rentalId, String custId, LocalDateTime startDate, long duration, String paymentId,
                                  String cost, LocalDateTime now, String vehicle, String vehicleType, String vehicleId) {
        boolean isAddedVehicleRental = false;
        boolean isAddedVehicle = false;
        try {
            boolean isAddedPayment = CrudUtil.execute("INSERT INTO payment VALUES(?,?,?,?,?);", paymentId, Double.parseDouble(cost), now, "Rental","Paid");
            boolean isAddedRental = CrudUtil.execute("INSERT INTO rental VALUES (?,?,?,?,?,?);", rentalId, startDate, String.valueOf(duration), paymentId, custId,"Active");
            if (vehicle.equals("Car")) {
                isAddedVehicleRental = CrudUtil.execute("INSERT INTO car_rental VALUES (?,?,?);", vehicleId, rentalId, now);
                isAddedVehicle = CrudUtil.execute("UPDATE car SET status=? WHERE car_reg_num=?;", "Booked",vehicleId);
            } else {
                isAddedVehicleRental = CrudUtil.execute("INSERT INTO bicycle_rental VALUES (?,?,?);", vehicleId, rentalId, now);
                isAddedVehicle = CrudUtil.execute("UPDATE bicycle SET status=? WHERE bicycle_id=?;", "Booked",vehicleId);
            }
            if (isAddedPayment & isAddedRental & isAddedVehicleRental & isAddedVehicle) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet getVehicleId(String vehicle, String vehicleType) {
        ResultSet resultSet = null;
        try {
            if (vehicle.equals("Car")) {
                resultSet = CrudUtil.execute("SELECT car_reg_num FROM car WHERE car_type=? AND status!=?;", vehicleType,"Booked");
            }else{
                resultSet = CrudUtil.execute("SELECT bicycle_id FROM bicycle WHERE bicycle_type=? AND status!=?;", vehicleType,"Booked");
            }
            return resultSet;
        } catch (SQLException e) {
            return resultSet;
        }
    }

    public static List<Rental> getAllCars() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT rental.rental_id,rental.customer_id,customer.cust_name,car.car_type, car_rental.car_reg_num,rental.rental_takeover_date_time,rental.rented_duration,payment.paid_amount,rental.rent_status FROM rental INNER JOIN car_rental ON rental.rental_id=car_rental.rent_id INNER JOIN customer ON customer.cust_id=rental.customer_id INNER JOIN payment ON payment.pay_id=rental.payment_id INNER JOIN car ON car.car_reg_num=car_rental.car_reg_num ORDER BY rental.rental_id;");
        List<Rental> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Rental(
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

    public static List<Rental> getAllBicycles() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT rental.rental_id,rental.customer_id,customer.cust_name,bicycle.bicycle_type, bicycle_rental.bicycle_id,rental.rental_takeover_date_time,rental.rented_duration,payment.paid_amount,rental.rent_status FROM rental INNER JOIN bicycle_rental ON rental.rental_id=bicycle_rental.rent_id INNER JOIN customer ON customer.cust_id=rental.customer_id INNER JOIN payment ON payment.pay_id=rental.payment_id INNER JOIN bicycle ON bicycle.bicycle_id=bicycle_rental.bicycle_id ORDER BY rental.rental_id;");
        List<Rental> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Rental(
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

    public static boolean updateRental(String rentalId, String custId, LocalDateTime startDate, long duration, String paymentId, String vehicle, String vehicleType, String vehicleId, String cost) throws SQLException {
        try {
            System.out.println(paymentId);
            boolean isAddedPayment = CrudUtil.execute("UPDATE payment SET paid_amount=? WHERE pay_id=?;", Double.parseDouble(cost),paymentId);
            boolean isAddedRental = CrudUtil.execute("UPDATE rental SET rental_takeover_date_time=?,rented_duration=? WHERE rental_id=?;", startDate, String.valueOf(duration), rentalId);
            if (isAddedPayment & isAddedRental ) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            DBConnection.getInstance().getConnection().rollback();
            e.printStackTrace();
            return false;
        }
    }


    public static List<RentalUpdate> searchRental(String rentId) throws SQLException {
        String vehicle="";
        ResultSet vehi=CrudUtil.execute("SELECT * FROM car_rental WHERE rent_id=?;",rentId);
        ResultSet resultSet=null;
        if (vehi.next()){
            vehicle="Car";
        }else{
            vehicle="Bicycle";
        }
        if (vehicle.equals("Car")){
            resultSet=CrudUtil.execute("SELECT rental.rental_id,rental.customer_id,customer.cust_name,car.car_type,car_rental.car_reg_num,rental.rental_takeover_date_time,rental.rented_duration,payment.paid_amount FROM rental INNER JOIN car_rental ON car_rental.rent_id=rental.rental_id INNER JOIN payment ON payment.pay_id=rental.payment_id INNER JOIN customer ON customer.cust_id=rental.customer_id INNER JOIN car ON car.car_reg_num=car_rental.car_reg_num WHERE rental.rental_id=?;",rentId);
        }else{
            resultSet=CrudUtil.execute("SELECT rental.rental_id,rental.customer_id,customer.cust_name,bicycle.bicycle_type,bicycle_rental.bicycle_id,rental.rental_takeover_date_time,rental.rented_duration,payment.paid_amount FROM rental INNER JOIN bicycle_rental ON bicycle_rental.rent_id=rental.rental_id INNER JOIN payment ON payment.pay_id=rental.payment_id INNER JOIN customer ON customer.cust_id=rental.customer_id INNER JOIN bicycle ON bicycle.bicycle_id=bicycle_rental.bicycle_id WHERE rental.rental_id=?;",rentId);
        }
        List<RentalUpdate> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new RentalUpdate(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    vehicle,
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            ));
        }
        return data;
    }
    public static String getPaymentId(String bookingId) {
        try {
            ResultSet resultSet=CrudUtil.execute("SELECT payment_id FROM rental WHERE rental_id=?",bookingId);
            String paymentId=null;
            if(resultSet.next()){
                paymentId=resultSet.getString(1);
            }
            return paymentId;
        } catch (SQLException e) {
            return null;
        }
    }

    public static boolean cancelRental(String rentalId, String custId,String vehicleType,String vehicleId ,String paymentId, String status) throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean isAddedVehicle =false;
            boolean isAddedPayment = CrudUtil.execute("UPDATE payment SET status=? WHERE pay_id=?;", status,paymentId);
            boolean isAddedRental = CrudUtil.execute("UPDATE rental SET rent_status=? WHERE rental_id=?;", status, rentalId);
            if (vehicleType.equals("Bicycle")) {
                isAddedVehicle = CrudUtil.execute("UPDATE bicycle SET status=? WHERE bicycle_id=?;", "Available", vehicleId);
            }else{
                isAddedVehicle = CrudUtil.execute("UPDATE car SET status=? WHERE car_reg_num=?;", "Available",vehicleId);
            }
            DBConnection.getInstance().getConnection().commit();
            if (isAddedPayment & isAddedRental & isAddedVehicle) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
            e.printStackTrace();
            return false;
        }
    }

    public static void updateStatus() throws SQLException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT rental.rental_id FROM rental INNER JOIN car_rental ON car_rental.rent_id=rental.rental_id WHERE rental.rental_takeover_date_time<? AND rental.rent_status!=?;",LocalDateTime.now(),"Cancelled");
            //ResultSet result=CrudUtil.execute("SELECT car_rental.car_reg_num FROM car_rental INNER JOIN rental ON car_rental.rent_id=rental.rental_id WHERE rental.rental_takeover_date_time<? AND rental.rent_status!=?;",LocalDateTime.now(),"Cancelled");
            ArrayList<String> tempIds=new ArrayList<>();
            while (resultSet.next()){
                tempIds.add(resultSet.getString(1));
            }
            boolean isAffected=false;
            boolean isUpdated=false;
            for (int i = 0; i < tempIds.size(); i++) {
                isAffected=CrudUtil.execute("UPDATE rental SET rent_status=? WHERE rental_id=?;","End",tempIds.get(i));
                //isUpdated=CrudUtil.execute("UPDATE car SET car.status=? INNER JOIN car_rental ON car_rental.car_reg_num=car.car_reg_num INNER JOIN rental ON rental.rental_id=car_reg_num.rent_id  WHERE rental.rental_id=?;","End",tempIds.get(i));
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT rental.rental_id FROM rental INNER JOIN bicycle_rental ON bicycle_rental.rent_id=rental.rental_id WHERE rental.rental_takeover_date_time<? AND rental.rent_status!=?;",LocalDateTime.now(),"Cancelled");
            ArrayList<String> tempIds=new ArrayList<>();
            while (resultSet.next()){
                tempIds.add(resultSet.getString(1));
            }
            boolean isAffected=false;
            for (int i = 0; i < tempIds.size(); i++) {
                isAffected=CrudUtil.execute("UPDATE rental SET rent_status=? WHERE rental_id=?;","End",tempIds.get(i));
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT car_rental.car_reg_num FROM car_rental INNER JOIN rental ON car_rental.rent_id=rental.rental_id WHERE rental.rental_takeover_date_time<?;",LocalDateTime.now());
            ArrayList<String> tempIds=new ArrayList<>();
            while (resultSet.next()){
                tempIds.add(resultSet.getString(1));
            }
            boolean isAffected=false;
            for (int i = 0; i < tempIds.size(); i++) {
                isAffected=CrudUtil.execute("UPDATE car SET status=? WHERE car_reg_num=?;","Available",tempIds.get(i));
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection.getInstance().getConnection().rollback();
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            ResultSet resultSet=CrudUtil.execute("SELECT bicycle_rental.bicycle_id FROM bicycle_rental INNER JOIN rental ON bicycle_rental.rent_id=rental.rental_id WHERE rental.rental_takeover_date_time<?;",LocalDateTime.now());
            ArrayList<String> tempIds=new ArrayList<>();
            while (resultSet.next()){
                tempIds.add(resultSet.getString(1));
            }
            boolean isAffected=false;
            for (int i = 0; i < tempIds.size(); i++) {
                isAffected=CrudUtil.execute("UPDATE bicycle SET status=? WHERE bicycle_id=?;","Available",tempIds.get(i));
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
            result= CrudUtil.execute("SELECT rental_id FROM rental ORDER BY rental_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            return id;
        } catch (SQLException e) {
            return id;
        }
    }
}
