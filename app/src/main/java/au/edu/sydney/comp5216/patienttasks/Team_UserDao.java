package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Team_UserDao {
    @Query("SELECT * FROM team_user")
    List<Team_User> listAll();

    @Insert
    void insert(Team_User team_user);

    @Insert
    void insertAll(Team_User... team_users);

    @Query("DELETE FROM team_user")
    void deleteAll();
}