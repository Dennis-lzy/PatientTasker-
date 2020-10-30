package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    PatientTasksDB db;
    UserDao userDao;
    PatientDao patientDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PatientsFragment()).commit();

        db = PatientTasksDB.getDatabase(this.getApplication().getApplicationContext());
        userDao = db.toDoItemDao(); // User Dao
        patientDao = db.patientDao(); // Patient Dao
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch(item.getItemId()){
                case R.id.nav_patients:
                    selectedFragment = new PatientsFragment();
                    break;
                case R.id.nav_tasks:
                    selectedFragment = new TasksFragment();
                    break;
                case R.id.nav_discharge:
                    selectedFragment = new DischargesFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        }
    };
}