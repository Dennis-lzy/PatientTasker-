package au.edu.sydney.comp5216.patienttasks;

import java.io.Serializable;

public class TaskWithPatient extends Task implements Serializable {

    String patientName;
    int patientMRN;
    String userName;

    public TaskWithPatient(String taskName) {
        super(taskName);
    }

    public TaskWithPatient(Task t) {
        super(t.getTaskName());
        setTask_patientID(t.getTask_patientID());
        setTaskAssign_userID(t.getTaskAssign_userID());
        setTaskCompleted(t.isTaskCompleted());
        setTaskDueDate(t.getTaskDueDate());
        setTaskID(t.getTaskID());
        setTaskPriority(t.getTaskPriority());
        setTaskRepeat(t.getTaskRepeat());
    }

    public String getPatientName() {return this.patientName;}
    public int getPatientMRN() {return this.patientMRN;}
    public String getUserName() {return this.userName;}

    public void setPatientName(String x) {this.patientName=x;}
    public void setPatientMRN(int x) {this.patientMRN=x;}
    public void setUserName(String x) {this.userName=x;}
}
