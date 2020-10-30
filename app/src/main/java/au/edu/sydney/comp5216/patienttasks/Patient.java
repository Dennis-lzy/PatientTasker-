package au.edu.sydney.comp5216.patienttasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.sql.Date;

@Entity(tableName = "patient")
public class Patient {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "patientID")
    private int patientID;

    @ColumnInfo(name = "patientName")
    private String patientName;

    @ColumnInfo(name = "patientRefNumber")
    private int patientRefNumber;

    @ColumnInfo(name = "patientConsultant")
    private String patientConsultant;

    @ColumnInfo(name = "patientAdmission")
    private String patientAdmission;

    @ColumnInfo(name = "patientDcDate")
    private String patientDcDate;

    @ColumnInfo(name = "patientDcDest")
    private String patientDcDest;

    @ColumnInfo(name = "patientBloods")
    private boolean patientBloods;

    @ColumnInfo(name = "patientMeds")
    private boolean patientMeds;

    @ColumnInfo(name = "patientSumm")
    private boolean patientSumm;

    public Patient(String patientName){
        this.patientName = patientName;
    }

    public int getPatientRefNumber() {
        return patientRefNumber;
    }

    public void setPatientRefNumber(int patientRefNumber) {
        this.patientRefNumber = patientRefNumber;
    }

    public String getPatientConsultant() {
        return patientConsultant;
    }

    public void setPatientConsultant(String patientConsultant) {
        this.patientConsultant = patientConsultant;
    }

    public String getPatientAdmission() {
        return patientAdmission;
    }

    public void setPatientAdmission(String patientAdmission) {
        this.patientAdmission = patientAdmission;
    }

    public String getPatientDcDate() {
        return patientDcDate;
    }

    public void setPatientDcDate(String patientDcDate) {
        this.patientDcDate = patientDcDate;
    }

    public String getPatientDcDest() {
        return patientDcDest;
    }

    public void setPatientDcDest(String patientDcDest) {
        this.patientDcDest = patientDcDest;
    }

    public boolean isPatientBloods() {
        return patientBloods;
    }

    public void setPatientBloods(boolean patientBloods) {
        this.patientBloods = patientBloods;
    }

    public boolean isPatientMeds() {
        return patientMeds;
    }

    public void setPatientMeds(boolean patientMeds) {
        this.patientMeds = patientMeds;
    }

    public boolean isPatientSumm() {
        return patientSumm;
    }

    public void setPatientSumm(boolean patientSumm) {
        this.patientSumm = patientSumm;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}