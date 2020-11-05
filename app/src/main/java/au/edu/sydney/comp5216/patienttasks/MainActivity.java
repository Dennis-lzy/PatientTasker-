package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Firebase
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    PatientTasksDB db;
    UserDao userDao;
    PatientDao patientDao;
    TaskDao taskDao;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    Button registerBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore dbFire = FirebaseFirestore.getInstance();
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
        intent.putExtra("editing", false);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
    }

    public void addNewTask(View v) {
        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
        intent.putExtra("editing", false);

        //which patient do we associate with this task? Select a patient from a dropdown menu perhaps?
        intent.putExtra("patient", "");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    public void onTaskCheckboxClick(View v) {
        //user has clicked a checkbox for this current View v (in the TaskView Fragment)
        //update the database for this current task to be marked as a subtask.
        //subtask of which task though?
    }

    public void patientsFBListener(FirebaseFirestore dbFire) {
        dbFire.collection("patients")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("patientListen", "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("patientsFBListener", "New patient: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d("patientsFBListener", "Modified patient: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d("patientsFBListener", "Removed patient: " + dc.getDocument().getData());
                                    break;
                            }
                        }

                    }
                });
}