package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.sql.SQLException;

public class FilterCustomerFormController {
    public AnchorPane filterRoot;
    public TextField idTxt;
    public Label name;
    public Label email;
    public JFXButton custReportBtn;
    public JFXButton bookingReportBtn;
    public JFXButton paymentReportBtn;
    public JFXButton carRentalReportBtn;
    public JFXButton bicycleRentalReportBtn;

    public void showCustReportOnAction(ActionEvent actionEvent) throws SQLException, JRException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edengardenCust.jrxml","SELECT * FROM customer;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    private void getReport(String path, String sql) throws JRException, SQLException {
        JasperDesign load = null;
        load = JRXmlLoader.load(new File(path));
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        load.setQuery(newQuery);
        JasperReport js = JasperCompileManager.compileReport(load);
        JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
        JasperViewer viewer = new JasperViewer(jp, false);
        viewer.show();
    }

    public void showBookingReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenBooking.jrxml","SELECT booking.booking_id,booking.customer_id, room_booking.room_No, room_booking.booking_made_date_time, booking.booked_date_time, booking.booking_duration,room_booking.availability FROM booking INNER JOIN room_booking ON room_booking.booking_id=booking.booking_id;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    public void showPaymentReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenPay.jrxml","SELECT * FROM payment;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    public void showcarRentalReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenCarRental.jrxml","SELECT rental.rental_id,rental.customer_id,customer.cust_name,car.car_type, car_rental.car_reg_num,rental.rental_takeover_date_time,rental.rented_duration,payment.paid_amount,rental.rent_status FROM rental INNER JOIN car_rental ON rental.rental_id=car_rental.rent_id INNER JOIN customer ON customer.cust_id=rental.customer_id INNER JOIN payment ON payment.pay_id=rental.payment_id INNER JOIN car ON car.car_reg_num=car_rental.car_reg_num;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    public void showbicycleRentalReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(() -> {
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenBicycleRental.jrxml", "SELECT rental.rental_id,rental.customer_id,customer.cust_name,bicycle.bicycle_type, bicycle_rental.bicycle_id,rental.rental_takeover_date_time,rental.rented_duration,payment.paid_amount,rental.rent_status FROM rental INNER JOIN bicycle_rental ON rental.rental_id=bicycle_rental.rent_id INNER JOIN customer ON customer.cust_id=rental.customer_id INNER JOIN payment ON payment.pay_id=rental.payment_id INNER JOIN bicycle ON bicycle.bicycle_id=bicycle_rental.bicycle_id;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }
}
