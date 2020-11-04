package au.edu.sydney.comp5216.patienttasks;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class, Patient.class, Task.class, Team.class, Team_User.class, SubTask.class}, version = 1, exportSchema = false)
public abstract class PatientTasksDB extends RoomDatabase {
    private static final String DATABASE_NAME = "patienttasks_db";
    private static PatientTasksDB DBINSTANCE;

    public abstract UserDao toDoItemDao();
    public abstract PatientDao patientDao();
    public abstract TaskDao taskDao();
    public abstract TeamDao teamDao();
    public abstract Team_UserDao team_userDao();
    public abstract SubTaskDao subTaskDao();

    public static PatientTasksDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (PatientTasksDB.class) {
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PatientTasksDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }

    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}