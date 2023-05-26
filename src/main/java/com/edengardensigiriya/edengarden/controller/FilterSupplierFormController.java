package com.edengardensigiriya.edengarden.controller;

import com.edengardensigiriya.edengarden.db.DBConnection;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.sql.SQLException;

public class FilterSupplierFormController {
    public AnchorPane filterRoot;
    public TextField idTxt;
    public Label name;
    public Label email;
    public JFXButton empReportBtn;
    public JFXButton suppReportBtn;
    public JFXButton roomReportBtn;
    public JFXButton orderReportBtn;
    public JFXButton itmReportBtn;

    public void showEmpReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenEmp.jrxml","SELECT * FROM employer;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    public void showSuppReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenSupp.jrxml","SELECT * FROM supplier;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    public void showRoomReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenRoom.jrxml","SELECT * FROM room;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    public void showOrderReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenOrder.jrxml","SELECT order_.ord_id,order_.supp_id,item.description,purchase_detail.bought_qty,order_.ord_made_date,order_.ord_dilever_date,purchase_detail.ord_cost,order_.status FROM order_ INNER JOIN purchase_detail ON purchase_detail.order_id=order_.ord_id INNER JOIN item ON item.item_code=purchase_detail.itm_code;");
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

    public void showItemReportOnAction(ActionEvent actionEvent) throws JRException, SQLException, InterruptedException {
        Thread t1= new Thread(()->{
            try {
                getReport("C:\\Users\\nipun\\JaspersoftWorkspace\\MyReports\\edenGardenItem.jrxml","SELECT * FROM item;");
            } catch (JRException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }
}
