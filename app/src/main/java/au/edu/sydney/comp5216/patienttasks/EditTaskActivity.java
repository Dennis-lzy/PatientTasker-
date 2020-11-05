package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditTaskActivity extends AppCompatActivity {
    private static final String TAG = "Tasks";
    EditText nameEdit;
    EditText dateEdit;
    Spinner userSpin;
    Spinner prioritySpin;
    Spinner repeatSpin;
    RecyclerView subtasks;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        //TODO: need to populate spinners with ArrayAdapter of users, priority, and repeat
        userSpin = findViewById(R.id.spinner_user);
        prioritySpin = findViewById(R.id.spinner_priority);
        repeatSpin = findViewById(R.id.spinner_repeat);

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