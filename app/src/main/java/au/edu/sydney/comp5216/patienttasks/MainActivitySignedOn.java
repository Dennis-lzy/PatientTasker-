package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
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
import java.util.Comparator;
import java.util.List;


public class MainActivitySignedOn extends AppCompatActivity {

    PatientTasksDB db;
    UserDao userDao;
    PatientDao patientDao;
    TaskDao taskDao;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private static final int REQUEST_CODE_ADD_PATIENT = 0;
    private static final int REQUEST_CODE_ADD_TASK = 1;
    private static final int REQUEST_CODE_GO_TEAMS = 2;
    private static final int REQUEST_CODE_GO_SETTINGS = 3;

    public static PatientsFragment pf;
    public static TasksFragment tf;
    public static DischargesFragment df;

    Button registerBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore dbFire = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();


        db = PatientTasksDB.getDatabase(this.getApplication().getApplicationContext());
        userDao = db.userDao(); // User Dao
        patientDao = db.patientDao(); // Patient Dao
        taskDao = db.taskDao(); // Task Dao

        //Search Firebase for database -if not exist then go to TeamActivity

        //Display PatientFragment as default on start
        pf = new PatientsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                pf).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_patients:
                            pf = new PatientsFragment();
                            selectedFragment = pf;
                            break;
                        case R.id.nav_tasks:
                            tf = new TasksFragment();
                            selectedFragment = tf;
                            break;
                        case R.id.nav_discharge:
                            df = new DischargesFragment();
                            selectedFragment = df;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public void addNewPatients(View v) {
        Intent intent = new Intent(MainActivitySignedOn.this, EditPatientActivity.class);
        intent.putExtra("editing", false);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_ADD_PATIENT);
        }
    }

    public void addNewTask(View v) {
        Intent intent = new Intent(MainActivitySignedOn.this, EditTaskActivity.class);
        intent.putExtra("editing", false);
        intent.putExtra("patient", "");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
        }
    }

    //sort/filter buttons are clicked
    public void onSortFilterPatients(View v) {
        String[] sortOptions = {"Name", "MRN", "Admission Date", "Filter discharged"};
        final Comparator<PatientWithTaskCount> cp1 = new Comparator<PatientWithTaskCount>() {
            @Override
            public int compare(PatientWithTaskCount patientWithTaskCount, PatientWithTaskCount t1) {
                return patientWithTaskCount.getPatientName().compareTo(t1.getPatientName());
            }
        };
        final Comparator<PatientWithTaskCount> cp2 = new Comparator<PatientWithTaskCount>() {
            @Override
            public int compare(PatientWithTaskCount patientWithTaskCount, PatientWithTaskCount t1) {
                if (patientWithTaskCount.getPatientRefNumber() - t1.getPatientRefNumber() > 0) {
                    return 1;
                }
                return -1;
            }
        };
        final Comparator<PatientWithTaskCount> cp3 = new Comparator<PatientWithTaskCount>() {
            @Override
            public int compare(PatientWithTaskCount patientWithTaskCount, PatientWithTaskCount t1) {
                return patientWithTaskCount.getPatientAdmDate().compareTo(t1.getPatientAdmDate());
            }
        };
        int selectedItem = -1;
        if (pf.adapter.fullPatients != null) {
            selectedItem = 3;
        }
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivitySignedOn.this);
        builder.setTitle("Sort/Filter Patients")
                .setSingleChoiceItems(sortOptions, selectedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (pf != null) {
                            if (pf.adapter.fullPatients != null) {
                                pf.adapter.patients = pf.adapter.fullPatients;
                                pf.adapter.fullPatients = null;

                            }
                            if (i == 0) {
                                pf.adapter.fullPatients = pf.adapter.patients;
                                pf.adapter.patients = new ArrayList<>();
                                for (PatientWithTaskCount in : pf.adapter.fullPatients) {
                                    if (!in.isPatientDischarged()) {
                                        pf.adapter.patients.add(in);
                                    }
                                }

                                pf.adapter.patients.sort(cp1);
                            } else if (i == 1) {
                                pf.adapter.fullPatients = pf.adapter.patients;
                                pf.adapter.patients = new ArrayList<>();
                                for (PatientWithTaskCount in : pf.adapter.fullPatients) {
                                    if (!in.isPatientDischarged()) {
                                        pf.adapter.patients.add(in);
                                    }
                                }

                                pf.adapter.patients.sort(cp2);
                            } else if (i == 2) {
                                pf.adapter.fullPatients = pf.adapter.patients;
                                pf.adapter.patients = new ArrayList<>();
                                for (PatientWithTaskCount in : pf.adapter.fullPatients) {
                                    if (!in.isPatientDischarged()) {
                                        pf.adapter.patients.add(in);
                                    }
                                }

                                pf.adapter.patients.sort(cp3);
                            } else if (i == 3) {
                                pf.adapter.fullPatients = pf.adapter.patients;
                                pf.adapter.patients = new ArrayList<>();
                                for (PatientWithTaskCount in : pf.adapter.fullPatients) {
                                    if (in.isPatientDischarged()) {
                                        pf.adapter.patients.add(in);
                                    }
                                }
                            }
                            Log.i("Sort/Filter patients", String.valueOf(i));
                            pf.adapter.notifyDataSetChanged();
                        }
                    }
                });
        builder.create().show();
    }

    public void onSortFilterTasks(View v) {
        String[] sortOptions = {"Filter completed"};
        boolean[] selectedItem = {false};
        if (tf.adapter.fullTasks != null) {
            selectedItem[0] = true;
        }
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivitySignedOn.this);
        builder.setTitle("Sort/Filter Tasks")
                .setMultiChoiceItems(sortOptions, selectedItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (tf != null) {
                            if (b) {
                                tf.adapter.fullTasks = tf.adapter.tasks;
                                tf.adapter.tasks = new ArrayList<>();
                                for (TaskWithPatient in : tf.adapter.fullTasks) {
                                    if (!in.isTaskCompleted()) {
                                        tf.adapter.tasks.add(in);
                                    }
                                }
                            } else {
                                tf.adapter.tasks = tf.adapter.fullTasks;
                                tf.adapter.fullTasks = null;
                            }
                            Log.i("Sort/Filter tasks", String.valueOf(i)+","+String.valueOf(b));
                            tf.adapter.notifyDataSetChanged();
                        }
                    }

                });
        builder.create().show();
    }

    public void onSortFilterDischarges(View v) {

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

    //Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_delete was selected
            case R.id.action_team:
                Intent teamIntent = new Intent(MainActivitySignedOn.this, TeamActivity.class);
                if (teamIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(teamIntent, REQUEST_CODE_GO_TEAMS);
                }

                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivitySignedOn.this, SettingsActivity.class);
                if (settingsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(settingsIntent, REQUEST_CODE_GO_TEAMS);
                }
                break;
            default:
                break;
        }

        return true;
    }
}