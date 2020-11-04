package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Firebase
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity {

    PatientTasksDB db;
    UserDao userDao;
    PatientDao patientDao;
    TaskDao taskDao;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFirestore();

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PatientsFragment()).commit();

        db = PatientTasksDB.getDatabase(this.getApplication().getApplicationContext());
        userDao = db.toDoItemDao(); // User Dao
        patientDao = db.patientDao(); // Patient Dao
        CollectionReference restaurants = mFirestore.collection("patients");
    }

    private void initFirestore() {
        FirebaseApp.initializeApp(this);
        mFirestore = FirebaseFirestore.getInstance();
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

    public void addNewPatients(View v) {
        Intent intent = new Intent(MainActivity.this, EditPatientActivity.class);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
    }

    public void addNewTask(View v) {
        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    public void addNewDischarge(View v) {

    }
}