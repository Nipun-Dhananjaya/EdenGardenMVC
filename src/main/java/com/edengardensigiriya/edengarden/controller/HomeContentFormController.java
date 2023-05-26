package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.model.HomeModel;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static javafx.application.Application.launch;

public class HomeContentFormController {
    public AnchorPane homeRoot;
    public BarChart todayUsageBarChrt;
    public BarChart monthUsageBarChrt;

    public void initialize() throws SQLException {
        setDayBarChart();
        setMonthBarChart();
    }

    private void setMonthBarChart() throws SQLException {
        String rooms="Rooms";
        String rental="Rentals";
        String transport="Transport";
        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Services");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Customer Count");

        //Prepare XYChart.Series objects by setting data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        System.out.println(LocalDate.now().getMonth());
        series.getData().add(new XYChart.Data<>(rooms, HomeModel.getMonthCount("SELECT COUNT(DISTINCT booking_id) FROM room_booking WHERE MONTH(booking_made_date_time)=?;", LocalDate.now().getMonthValue())));
        series.getData().add(new XYChart.Data<>(rental, (HomeModel.getMonthCount("SELECT COUNT(DISTINCT rent_id) FROM car_rental WHERE MONTH(rental_made_date_time)=?;", LocalDate.now().getMonthValue())+HomeModel.getMonthCount("SELECT COUNT(DISTINCT rent_id) FROM bicycle_rental WHERE MONTH(rental_made_date_time)=?;",LocalDate.now().getMonthValue()))));
        series.getData().add(new XYChart.Data<>(transport, HomeModel.getMonthTransCount("SELECT COUNT(DISTINCT pay_id) FROM payment WHERE MONTH(pay_made_date)=? AND reason=?;", LocalDate.now().getMonthValue(),"Transport")));

        //Setting the data to bar chart
        monthUsageBarChrt.getData().addAll(series);
    }

    private void setDayBarChart() throws SQLException {
        String rooms="Rooms";
        String rental="Rentals";
        String transport="Transport";
        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Services");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Customer Count");

        //Prepare XYChart.Series objects by setting data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        System.out.println(LocalDate.now());
        series.getData().add(new XYChart.Data<>(rooms, HomeModel.getCount("SELECT COUNT(DISTINCT booking_id) FROM room_booking WHERE DATE(booking_made_date_time)=?;", LocalDate.now())));
        series.getData().add(new XYChart.Data<>(rental, (HomeModel.getCount("SELECT COUNT(DISTINCT rent_id) FROM car_rental WHERE DATE(rental_made_date_time)=?;", LocalDate.now())+HomeModel.getCount("SELECT COUNT(DISTINCT rent_id) FROM bicycle_rental WHERE DATE(rental_made_date_time)=?;",LocalDate.now()))));
        series.getData().add(new XYChart.Data<>(transport, HomeModel.getTransCount("SELECT COUNT(DISTINCT pay_id) FROM payment WHERE DATE(pay_made_date)=? AND reason=?;", LocalDate.now(),"Transport")));

        //Setting the data to bar chart
        todayUsageBarChrt.getData().addAll(series);
    }
}
