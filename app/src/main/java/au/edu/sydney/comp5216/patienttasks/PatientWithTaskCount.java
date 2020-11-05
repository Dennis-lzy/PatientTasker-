package au.edu.sydney.comp5216.patienttasks;

public class PatientWithTaskCount extends Patient {

    private int tasksInProgress;
    private int tasksCompleted;

    public PatientWithTaskCount(String patientName) {
        super(patientName);
    }

    public int getTasksInProgress() {return this.tasksInProgress;}
    public int getTasksCompleted() {return this.tasksCompleted;}

    public void setTasksInProgress(int t) {this.tasksInProgress=t;}
    public void setTasksCompleted(int t) {this.tasksCompleted=t;}
}
