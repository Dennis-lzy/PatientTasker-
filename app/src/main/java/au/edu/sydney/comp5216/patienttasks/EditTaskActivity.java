package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "Tasks";
    EditText nameEdit;
    EditText dateEdit;
    Spinner userSpin;
    ArrayAdapter<String> userSpinAdapter;
    ArrayList<String> users;
    Spinner prioritySpin;
    ArrayAdapter<Integer> prioSpinAdapter;
    ArrayList<Integer> priorities;
    Spinner repeatSpin;
    ArrayAdapter<Integer> repeatSpinAdapter;
    ArrayList<Integer> repeats;
    RecyclerView subtasks;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        users = new ArrayList<>();
        priorities = new ArrayList<>();
        repeats = new ArrayList<>();

        for (int i = 1 ; i < 11; i++) {
            priorities.add(i);
            repeats.add(i);
        }

        //database query (async) for fetching users
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    List<User> userDB = PatientTasksDB.getDatabase(EditTaskActivity.this).toDoItemDao().listAll();
                    Log.i("Users", "Database query for users retrieved " + userDB.size() + " users.");
                    for (User user : userDB) {
                        users.add(user.getUserName());
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

        // populate priority spinner
        prioritySpin = findViewById(R.id.spinner_priority);
        prioSpinAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, priorities);
        prioSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpin.setAdapter(prioSpinAdapter);

        // populate repeat spinner
        repeatSpin = findViewById(R.id.spinner_repeat);
        repeatSpinAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, repeats);
        repeatSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpin.setAdapter(repeatSpinAdapter);

        //TODO: need to populate recyclerView with ArrayAdapter of subtasks
        subtasks = findViewById(R.id.recyclerview_subtasks);
    }

    public void onSave(View v) {

        //fetch data from the input fields
        nameEdit = findViewById(R.id.editText_task_name);
        dateEdit = findViewById(R.id.editText_due_date);
        String name = nameEdit.getText().toString();
        String date = dateEdit.getText().toString();
        String user = userSpin.getSelectedItem().toString();
        String priority = prioritySpin.getSelectedItem().toString();
        String repeat = repeatSpin.getSelectedItem().toString();

        //Add to Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Need to add fields
        Map<String, Object> patientTask = new HashMap<>();
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


        Task task = new Task(name);

        // Prepare data intent for sending it back
        Intent data = new Intent();

        // Pass relevant data back as a result
        data.putExtra("name", name);
        data.putExtra("date", date);
        data.putExtra("user", user);
        data.putExtra("priority", priority);
        data.putExtra("repeat", repeat);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    public void onCancel(View v) {
        setResult(RESULT_CANCELED); // set result code
        finish(); // closes the activity
    }

    public void addSubtask(View v) {
        //TODO: Add a new subtask to recyclerView upon clicking the green button
    }
}