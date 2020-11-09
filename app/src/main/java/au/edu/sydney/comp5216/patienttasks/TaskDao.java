package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> listAll();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Query("UPDATE task SET taskCompleted=1 WHERE taskID=:tid")
    void completeTask(int tid);

    @Query("UPDATE task SET taskCompleted=0 WHERE taskID=:tid")
    void uncompleteTask(int tid);

    @Insert
    void insertAll(Task... tasks);

    @Delete
    void delete(Task task);
}