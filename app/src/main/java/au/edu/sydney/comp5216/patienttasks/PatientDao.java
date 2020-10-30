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
    void insertAll(Patient... patient);

    @Query("DELETE FROM patient")
    void deleteAll();
}