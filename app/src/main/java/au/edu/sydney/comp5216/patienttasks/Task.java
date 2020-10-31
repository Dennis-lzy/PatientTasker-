package au.edu.sydney.comp5216.patienttasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "taskID")
    private int taskID;

    @ColumnInfo(name = "taskName")
    private String taskName;

    @ColumnInfo(name = "task_patientID")
    private int task_patientID;

    @ColumnInfo(name = "taskAssign_userID")
    private int taskAssign_userID;

    @ColumnInfo(name = "taskDueDate")
    private String taskDueDate;

    @ColumnInfo(name = "taskPriority")
    private int taskPriority;

    @ColumnInfo(name = "taskRepeat")
    private String taskRepeat;

    public Task(String taskName){
        this.taskName = taskName;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTask_patientID() {
        return task_patientID;
    }

    public void setTask_patientID(int task_patientID) {
        this.task_patientID = task_patientID;
    }

    public int getTaskAssign_userID() {
        return taskAssign_userID;
    }

    public void setTaskAssign_userID(int taskAssign_userID) {
        this.taskAssign_userID = taskAssign_userID;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(String taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskRepeat() {
        return taskRepeat;
    }

    public void setTaskRepeat(String taskRepeat) {
        this.taskRepeat = taskRepeat;
    }
}