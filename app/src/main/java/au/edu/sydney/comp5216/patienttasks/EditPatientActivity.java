package au.edu.sydney.comp5216.patienttasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditPatientActivity extends AppCompatActivity {
    private static final String TAG = "Patient";
    Patient p;
    boolean isEditing;

    EditText mrn;
    EditText editText_dc_dest;
    EditText editText_dc_date;
    EditText editText_admission_date;
    EditText editText_consultant;
    EditText editText_name;
    EditText editText_diagnosis;
    EditText editText_notes;

    CheckBox cb;
    CheckBox cb2;
    CheckBox cb3;
    CheckBox cb4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        isEditing = getIntent().getBooleanExtra("editing", false);
        Log.i("EditPatientActivity", "Editing = "+isEditing);

        cb = findViewById(R.id.checkBox_dc_summary);
        cb2 = findViewById(R.id.checkBox_order_bloods);
        cb3 = findViewById(R.id.checkBox_send_meds);
        cb4 = findViewById(R.id.checkBox_discharged);

        mrn = findViewById(R.id.editText_mrn);
        editText_dc_dest = findViewById(R.id.editText_dc_dest);
        editText_dc_date = findViewById(R.id.editText_dc_date);
        editText_admission_date = findViewById(R.id.editText_admission_date);
        editText_consultant = findViewById(R.id.editText_consultant);
        editText_name = findViewById(R.id.editText_name);
        editText_diagnosis = findViewById(R.id.editText_diagnosis);
        editText_notes = findViewById(R.id.editText_notes);

        if (isEditing) {
            p = (Patient) getIntent().getSerializableExtra("patient");

            cb2.setChecked(p.isPatientBloods());
            cb3.setChecked(p.isPatientMeds());
            cb4.setChecked(p.isPatientDischarged());

            editText_dc_dest.setText(p.getPatientDcDest());
            editText_dc_date.setText(p.getPatientDcDate());
            editText_admission_date.setText(p.getPatientAdmDate());
            editText_consultant.setText(p.getPatientConsultant());
            editText_name.setText(p.getPatientName());

            mrn.setText(String.valueOf(p.getPatientRefNumber()));

            editText_diagnosis.setText(p.getDiagnosis());
            editText_notes.setText(p.getPatientNotes());

        } else {
            p = new Patient("");
        }
    }

    //checkbox toggle
    //these methods are not needed because the database attributes is only updated when the "Save" button is clicked
    public void onClickDCSUMMARY(View v) {
        //CheckBox cb = findViewById(R.id.checkBox_dc_summary);
    }

    //checkbox toggle
    public void onOrderBloods(View v) {
        //CheckBox cb = findViewById(R.id.checkBox_order_bloods);
    }

    //checkbox toggle
    public void onSendMeds(View v) {
        //CheckBox cb = findViewById(R.id.checkBox_send_meds);

    }

    public void onAddTask(View v) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("editing", false);
        intent.putExtra("patient", p);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    public void onCancel(View v) {
        finish();
    }

    public void onSave(View v) {
        //update text fields into the database
        p.setPatientBloods(cb2.isChecked());
        p.setPatientMeds(cb3.isChecked());
        p.setPatientDischarged(cb4.isChecked());

        p.setPatientDcDest(editText_dc_dest.getText().toString());
        p.setPatientDcDate(editText_dc_date.getText().toString());
        p.setPatientAdmDate(editText_admission_date.getText().toString());
        p.setPatientConsultant(editText_consultant.getText().toString());
        p.setPatientName(editText_name.getText().toString());
        try {
            p.setPatientRefNumber(Integer.parseInt(mrn.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ERROR: Invalid MRN",
                    Toast.LENGTH_LONG).show();
            return;
        }

        p.setDiagnosis(editText_diagnosis.getText().toString());
        p.setPatientNotes(editText_notes.getText().toString());

        if (isEditing) {
            //update exiting patient
            //PatientTasksDB.getDatabase(this).patientDao().update(p.getPatientID(), p);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Patient oldPatient = (Patient)getIntent().getSerializableExtra("patient");
                    PatientTasksDB.getDatabase(EditPatientActivity.this).patientDao().delete(oldPatient);
                    PatientTasksDB.getDatabase(EditPatientActivity.this).patientDao().insert(p);
                    Log.i("SQLite saved item", "Patient "+p.getPatientName()+", "+p.getPatientID()+", "+p.getPatientRefNumber());
                    return null;
                }
            }.execute();

            if (MainActivity.pf != null) {
                MainActivity.pf.adapter.notifyDataSetChanged();
            }
        } else {
            //add new patient (automatically sets a new primary key as the next available integer? Or does that have to be done here?)
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    PatientTasksDB.getDatabase(EditPatientActivity.this).patientDao().insert(p);
                    Log.i("SQLite saved item", "Patient "+p.getPatientName()+", "+p.getPatientID()+", "+p.getPatientRefNumber());
                    return null;
                }
            }.execute();

            isEditing = true;
            if (MainActivity.pf != null) {
                MainActivity.pf.adapter.notifyDataSetChanged();
            }
        }

        //Make updates to firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> patient = new HashMap<>();
        patient.put("Name", editText_name.getText().toString());
        patient.put("MRN", mrn.getText().toString());
        patient.put("Diagnosis", editText_diagnosis.getText().toString());
        patient.put("Consultant", editText_consultant.getText().toString());
        patient.put("Admission date", editText_admission_date.getText().toString());
        patient.put("Est DC date", editText_dc_date.getText().toString());
        patient.put("DC destination", editText_dc_dest.getText().toString());
        patient.put("Notes", editText_notes.getText().toString());
        patient.put("Order Blood",cb2.isChecked());
        patient.put("Send Meds",cb3.isChecked());
        patient.put("Discharged",cb4.isChecked());

        db.collection("patients").document(mrn.getText().toString())
                .set(patient)
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


        Toast.makeText(this, "Saved patient info",
                Toast.LENGTH_SHORT).show();

        // Activity finished ok, return the data
        setResult(RESULT_OK); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

    //checkbox toggle
    public void onDischarge(View v) {
        /*CheckBox cb = findViewById(R.id.checkBox_discharged);
        if (cb.isChecked()) {

        }*/
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPatientActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(EditPatientActivity.this, "Delete selected",
                                        Toast.LENGTH_SHORT)
                                        .show();

                                int position = getIntent().getIntExtra("position", 0);
                                // Prepare data intent for sending it back
                                Intent data = new Intent();

                                // Pass relevant data back as a result
                                data.putExtra("position", position);

                                // Activity finished ok, return the data
                                setResult(RESULT_OK, data); // set result code and bundle data for response
                                finish(); // closes the activity, pass data to parent
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