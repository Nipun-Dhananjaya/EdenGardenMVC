package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.dto.Booking;
import com.edengardensigiriya.edengarden.dto.Payment;
import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.edengardensigiriya.edengarden.model.CustomerModel.stringLength;

public class PaymentModel {
    public static String generateID() {
        ResultSet result=null;
        String[] idParts;
        String id="P-00000";
        try {
            result= CrudUtil.execute("SELECT pay_id FROM payment ORDER BY pay_id DESC LIMIT 1");
            if(result.next()) {
                id=result.getString(1);
            }
            idParts=id.split("-");
            int number=Integer.parseInt(idParts[1]);
            String num=setNextIdValue(++number);
            return "P-"+num;
        } catch (SQLException | ClassCastException e) {
            e.printStackTrace();
            return "P-00000";
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

    public static List<Payment> getAllRentalPay() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT payment.pay_id,rental.customer_id,payment.pay_made_date, payment.reason,payment.paid_amount,payment.status FROM payment INNER JOIN rental ON payment.pay_id=rental.payment_id ORDER BY payment.pay_id;");
        List<Payment> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Payment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return data;
    }
    public static List<Payment> getAllBookingPay() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT payment.pay_id,booking.customer_id,payment.pay_made_date, payment.reason,payment.paid_amount,payment.status FROM payment INNER JOIN booking ON payment.pay_id=booking.payment_id ORDER BY payment.pay_id;");
        List<Payment> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Payment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return data;
    }

    public static List<Payment> getAllTransportPay() throws SQLException {
        ResultSet resultSet=CrudUtil.execute("SELECT payment.pay_id,transport.customer_id,payment.pay_made_date, payment.reason,payment.paid_amount,payment.status FROM payment INNER JOIN transport ON payment.pay_id=transport.payment_id ORDER BY payment.pay_id;");
        List<Payment> data = new ArrayList<>();

        while (resultSet.next()) {
            data.add(new Payment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return data;
    }
}
