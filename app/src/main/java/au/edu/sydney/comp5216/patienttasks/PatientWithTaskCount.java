package au.edu.sydney.comp5216.patienttasks;

import androidx.room.ColumnInfo;

public class PatientWithTaskCount extends Patient {

    private int tasksInProgress;
    private int tasksCompleted;

    public PatientWithTaskCount(String patientName) {
        super(patientName);
    }
    public PatientWithTaskCount(Patient p) {
        super(p.getPatientName());
        setPatientDischarged(p.isPatientDischarged());
        setPatientWebster(p.isPatientWebster());
        setPatientNotes(p.getPatientNotes());
        setPatientRefNumber(p.getPatientRefNumber());
        setPatientConsultant(p.getPatientConsultant());
        setPatientAdmDate(p.getPatientAdmDate());
        setPatientDcDate(p.getPatientDcDate());
        setPatientDcDest(p.getPatientDcDest());
        setPatientBloods(p.isPatientBloods());
        setPatientMeds(p.isPatientMeds());
        setPatientSumm(p.isPatientSumm());
        setPatientID(p.getPatientID());
        setDiagnosis(p.getDiagnosis());
    }

    public int getTasksInProgress() {return this.tasksInProgress;}
    public int getTasksCompleted() {return this.tasksCompleted;}

    public void setTasksInProgress(int t) {this.tasksInProgress=t;}
    public void setTasksCompleted(int t) {this.tasksCompleted=t;}
}
