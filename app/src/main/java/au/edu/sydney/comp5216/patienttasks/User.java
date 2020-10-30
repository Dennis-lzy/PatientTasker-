package au.edu.sydney.comp5216.patienttasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "userID")
    private int userID;

    @ColumnInfo(name = "userName")
    private String userName;

    @ColumnInfo(name = "userEmail")
    private String userEmail;

    @ColumnInfo(name = "userPassword")
    private String userPassword;

    @ColumnInfo(name = "userPin")
    private String userPin;

    @ColumnInfo(name = "userLogoff")
    private int userLogoff;

    public User(String userName){
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPin() {
        return userPin;
    }

    public int getUserLogoff() {
        return userLogoff;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public void setUserLogoff(int userLogoff) {
        this.userLogoff = userLogoff;
    }
}