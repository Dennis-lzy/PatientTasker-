package au.edu.sydney.comp5216.patienttasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "team_user")
public class Team_User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "team_userID")
    private int team_userID;

    @ColumnInfo(name = "team_ID")
    private int team_ID;

    @ColumnInfo(name = "user_ID")
    private int user_ID;

    public Team_User(){

    }

    public int getTeam_userID() {
        return team_userID;
    }

    public void setTeam_userID(int team_userID) {
        this.team_userID = team_userID;
    }

    public int getTeam_ID() {
        return team_ID;
    }

    public void setTeam_ID(int team_ID) {
        this.team_ID = team_ID;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }
}