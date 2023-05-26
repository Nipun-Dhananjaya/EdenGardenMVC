package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangeUserModel {

    public static boolean changeUser(String oldUserName, String oldPassword,String newUserEmpId, String newUserName, String newPassword,  String jobroll) {
        String userId="";
        try {
            System.out.println(jobroll);
            ResultSet result = CrudUtil.execute("SELECT user_id FROM user WHERE user_name=? AND password=? AND job_role=?;", oldUserName, oldPassword, jobroll);
            while (result.next()) {
                System.out.println("Inside");
                userId = result.getString(1);
            }

            System.out.println(userId);
            //System.out.println("res:   "+result.getString(1));
            boolean IsAffected=false;
            if (CrudUtil.execute("DELETE FROM user WHERE user_id=?;",userId)){
                System.out.println("Deleted");
                IsAffected=CrudUtil.execute("INSERT INTO user VALUES (?,?,?,?,?);",userId,newUserEmpId, newUserName, newPassword, jobroll);
            }
            if(IsAffected){
                return true;
            }
            else{
                System.out.println("failed");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
