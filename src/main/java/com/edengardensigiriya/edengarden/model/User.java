package com.edengardensigiriya.edengarden.model;

import com.edengardensigiriya.edengarden.util.CrudUtil;
import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@Getter
@Setter
public class User {
    private static String UserName;
    private static String Password;
    private static User user;

    public User(String UserName, String Password) {
        this.UserName = UserName;
        this.Password = Password;
    }

    public static boolean IsCorrect(String userName, String password, String jobroll) {
        ResultSet result = null;
        try {
            result = CrudUtil.execute("SELECT user_name,password FROM user WHERE user_name=? AND password=? AND job_role=?;", userName, password, jobroll);
            if (result.next()) {
                user = new User(result.getString(1), result.getString(2));
            }else {
                return false;
            }
            if (user.UserName.equals(userName) & user.Password.equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean changePwd(String oldUserName, String oldPassword, String newUserName, String newPassword, String comfirmationPwd, String jobroll) {
        ResultSet result=null;
        String userId=null;
        if (newPassword.equals(comfirmationPwd)) {
            try {
                result = CrudUtil.execute("SELECT user_id FROM user WHERE user_name=? AND password=? AND job_role=?;", oldUserName, oldPassword, jobroll);
                if (result.next()) {
                    userId = result.getString(1);
                }
                boolean IsAffected=CrudUtil.execute("UPDATE user SET user_name=?, password=? WHERE user_id=?", newUserName, newPassword, userId);
                if(IsAffected){
                    return true;
                }
                else{
                    return false;
                }
            } catch (SQLException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
