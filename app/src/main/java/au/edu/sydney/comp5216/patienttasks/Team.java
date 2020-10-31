package au.edu.sydney.comp5216.patienttasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "team")
public class Team {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "teamID")
    private int teamID;

    @ColumnInfo(name = "teamName")
    private String teamName;

    @ColumnInfo(name = "teamLeader_userID")
    private int teamLeader_userID;

    @ColumnInfo(name = "teamMembers_userIDs")
    private int teamMembers_userIDs;

    public Team(String teamName){
        this.teamName = teamName;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTeamLeader_userID() {
        return teamLeader_userID;
    }

    public void setTeamLeader_userID(int teamLeader_userID) {
        this.teamLeader_userID = teamLeader_userID;
    }

    public int getTeamMembers_userIDs() {
        return teamMembers_userIDs;
    }

    public void setTeamMembers_userIDs(int teamMembers_userIDs) {
        this.teamMembers_userIDs = teamMembers_userIDs;
    }
}