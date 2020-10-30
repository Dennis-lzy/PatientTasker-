package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> listAll();

    @Insert
    void insert(User user);

    @Insert
    void insertAll(User... users);

    @Query("DELETE FROM user")
    void deleteAll();
}