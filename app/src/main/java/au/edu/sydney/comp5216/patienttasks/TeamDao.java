package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TeamDao {
    @Query("SELECT * FROM team")
    List<Team> listAll();

    @Insert
    void insert(Team team);

    @Insert
    void insertAll(Team... teams);

    @Query("DELETE FROM team")
    void deleteAll();
}