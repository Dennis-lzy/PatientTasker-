package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> listAll();


    @Insert
    void insert(Task task);

    @Insert
    void insertAll(Task... tasks);

    @Query("DELETE FROM task")
    void deleteAll();
}