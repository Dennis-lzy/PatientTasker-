package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PatientDao {
    @Query("SELECT * FROM patient")
    List<Patient> listAll();

    @Insert
    void insert(Patient patient);

    @Insert
    void insertAll(Patient... patients);

    @Query("DELETE FROM patient")
    void deleteAll();

    @Query("SELECT p.patientID," +
            "p.patientName," +
            "p.patientRefNumber," +
            "p.patientConsultant," +
            "p.patientAdmDate," +
            "p.patientDcDate," +
            "p.patientDcDest," +
            "p.patientBloods," +
            "p.patientMeds," +
            "p.patientSumm," +
            "p.patientWebster," +
            "p.patientDischarged," +
            "p.patientNotes," +
            "COUNT(t.taskID) as tasksInProgress," +
            "COUNT(t2.taskID) as tasksCompleted " +
            "FROM patient as p " +
            "INNER JOIN task as t ON p.patientID=t.task_patientID " +
            "INNER JOIN task as t2 ON p.patientID=t2.task_patientID " +
            "WHERE t2.taskCompleted=1 AND t.taskCompleted=0 " +
            "GROUP BY p.patientID")
    List<PatientWithTaskCount> getPatientView();
}