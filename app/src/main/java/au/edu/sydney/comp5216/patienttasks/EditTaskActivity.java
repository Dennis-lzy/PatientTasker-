package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EditTaskActivity extends AppCompatActivity implements TaskViewAdapter.ItemClickListener {
    private static final String TAG = "Tasks";
    EditText nameEdit;
    EditText dateEdit;
    Spinner userSpin;
    ArrayAdapter<String> userSpinAdapter;
    ArrayList<String> users;
    Spinner patientSpin;
    ArrayAdapter<String> patientSpinAdapter;
    ArrayList<String> patients;
    Spinner prioritySpin;
    ArrayAdapter<String> prioSpinAdapter;
    ArrayList<String> priorities;
    Spinner repeatSpin;
    ArrayAdapter<String> repeatSpinAdapter;
    ArrayList<String> repeats;
    RecyclerView subtaskView;
    TaskViewAdapter subtaskAdapter;
    ArrayList<String> subtasks;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        patients = new ArrayList<>();
        users = new ArrayList<>();
        priorities = new ArrayList<>();
        repeats = new ArrayList<>();
        for (int i = 1 ; i < 11; i++) {
            priorities.add(Integer.toString(i));
        }
        repeats.add("Yes");
        repeats.add("No");

        //database query (async) for fetching users
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    List<User> userDB = PatientTasksDB.getDatabase(EditTaskActivity.this).toDoItemDao().listAll();
                    Log.i("Users", "Database query for users retrieved " + userDB.size() + " users.");
                    for (User user : userDB) {
                        users.add(Integer.toString(user.getUserID()));
                    }
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // populate user spinner
        userSpin = findViewById(R.id.spinner_user);
        userSpinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, users);
        userSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpin.setAdapter(userSpinAdapter);

        //database query (async) for fetching patients
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    List<Patient> patientDB = PatientTasksDB.getDatabase(EditTaskActivity.this).patientDao().listAll();
                    Log.i("Patients", "Database query for patients retrieved " + patientDB.size() + " patients.");
                    for (Patient patient : patientDB) {
                        patients.add(Integer.toString(patient.getPatientID()));
                    }
                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // populate patients spinner
        patientSpin = findViewById(R.id.spinner_patient_select);
        patientSpinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, patients);
        patientSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patientSpin.setAdapter(patientSpinAdapter);

        // populate priority spinner
        prioritySpin = findViewById(R.id.spinner_priority);
        prioSpinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, priorities);
        prioSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpin.setAdapter(prioSpinAdapter);

        // populate repeat spinner
        repeatSpin = findViewById(R.id.spinner_repeat);
        repeatSpinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, repeats);
        repeatSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpin.setAdapter(repeatSpinAdapter);

        subtaskView = findViewById(R.id.recyclerview_subtasks);
        subtaskAdapter = new TaskViewAdapter(this.getApplicationContext());
        subtaskAdapter.setClickListener(this);
        subtaskView.setAdapter(subtaskAdapter);
    }

    public void onSave(View v) {

        //fetch data from the input fields
        nameEdit = findViewById(R.id.editText_task_name);
        dateEdit = findViewById(R.id.editText_due_date);
        String name = nameEdit.getText().toString();
        task = new Task(name);

        if (userSpin.getSelectedItem() != null) {
            Integer user = Integer.parseInt(userSpin.getSelectedItem().toString());
            task.setTaskAssign_userID(user);
        }
        if (patientSpin.getSelectedItem() != null) {
            Integer patient = Integer.parseInt(patientSpin.getSelectedItem().toString());
            task.setTask_patientID(patient);
        }
        Integer priority = Integer.parseInt(prioritySpin.getSelectedItem().toString());
        String repeat = repeatSpin.getSelectedItem().toString();

        String date = dateEdit.getText().toString();
        task.setTaskDueDate(date);
        task.setTaskPriority(priority);
        task.setTaskRepeat(repeat);

        // insert task in database (async)
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    PatientTasksDB.getDatabase(EditTaskActivity.this).taskDao().insert(task);
                    Log.i("SQLite saved item", "Task: "+ task.getTaskName() + '\n');
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Add to Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Need to add fields
        Map<String, Object> patientTask = new HashMap<>();
        patientTask.put("Name", name);
        if (userSpin.getSelectedItem() != null) {
            patientTask.put("Assigned to", userSpin.getSelectedItem());
        }
        if (patientSpin.getSelectedItem() != null) {
            patientTask.put("Patient", patientSpin.getSelectedItem());
        }
        patientTask.put("Dee Date", date);
        patientTask.put("Priority", priority);
        patientTask.put("Repeat", repeat);

        db.collection("Tasks").document()
                .set(patientTask)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

        setResult(RESULT_OK); // set result code
        finish(); // closes the activity, pass data to parent
    }

    public void onCancel(View v) {
        setResult(RESULT_CANCELED); // set result code
        finish(); // closes the activity
    }

    public void addSubtask(View v) {
        Toast.makeText(this.getApplicationContext(), "Adding subtask ", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this.getApplicationContext(), EditTaskActivity.class);
        intent.putExtra("editing", false);
        intent.putExtra("task", subtaskAdapter.tasks.size());

        if (intent.resolveActivity(this.getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this.getApplicationContext(), "Editing task " + subtaskAdapter.tasks.get(position).getTaskName() + " on row number " + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this.getApplicationContext(), EditTaskActivity.class);
        intent.putExtra("editing", true);
        intent.putExtra("task", subtaskAdapter.tasks.get(position));

        if (intent.resolveActivity(this.getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
    }

    //Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_patient_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_delete was selected
            case R.id.action_delete:
                //Create dialog to prompt user to delete item
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTaskActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(EditTaskActivity.this, "Delete selected",
                                        Toast.LENGTH_SHORT)
                                        .show();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
                break;
            default:
                break;
        }

        return true;
    }
}