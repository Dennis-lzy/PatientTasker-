package au.edu.sydney.comp5216.patienttasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "subtask")
public class SubTask {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "subTaskID")
    private int subTaskID;

    @ColumnInfo(name = "subTaskName")
    private String subTaskName;

    @ColumnInfo(name = "subTask_TaskID")
    private int subTask_TaskID;

    @ColumnInfo(name = "subTaskCompleted")
    private int subTaskCompleted;

    public int getSubTaskCompleted() {
        return subTaskCompleted;
    }

    public void setSubTaskCompleted(int subTaskCompleted) {
        this.subTaskCompleted = subTaskCompleted;
    }

    public SubTask(String subTaskName){
        this.subTaskName = subTaskName;
    }

    public int getSubTaskID() {
        return subTaskID;
    }

    public void setSubTaskID(int subTaskID) {
        this.subTaskID = subTaskID;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public int getSubTask_TaskID() {
        return subTask_TaskID;
    }

    public void setSubTask_TaskID(int subTask_TaskID) {
        this.subTask_TaskID = subTask_TaskID;
    }
}