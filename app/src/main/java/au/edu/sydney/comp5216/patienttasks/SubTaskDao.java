package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SubTaskDao {
    @Query("SELECT * FROM subtask")
    List<SubTask> listAll();

    @Query("SELECT * FROM subtask WHERE subTask_TaskID =:tid")
    List<SubTask> listWhere(int tid);

    @Insert
    void insert(SubTask subTask);

    @Update
    void update(SubTask subTask);

    @Insert
    void insertAll(SubTask... subTasks);

    @Query("DELETE FROM subtask")
    void deleteAll();
}