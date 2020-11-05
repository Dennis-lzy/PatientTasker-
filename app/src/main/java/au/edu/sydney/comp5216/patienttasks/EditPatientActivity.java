package au.edu.sydney.comp5216.patienttasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class EditPatientActivity extends AppCompatActivity {

    Patient p;
    boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        isEditing = getIntent().getBooleanExtra("editing", false);
        if (isEditing) {
            p = (Patient) getIntent().getSerializableExtra("patient");
            CheckBox cb = findViewById(R.id.checkBox_dc_summary);
            CheckBox cb2 = findViewById(R.id.checkBox_order_bloods);
            CheckBox cb3 = findViewById(R.id.checkBox_send_meds);
            CheckBox cb4 = findViewById(R.id.checkBox_discharged);

            cb2.setChecked(p.isPatientBloods());
            cb3.setChecked(p.isPatientMeds());
            cb4.setChecked(p.isPatientDischarged());

            EditText mrn = findViewById(R.id.editText_mrn);
            EditText editText_dc_dest = findViewById(R.id.editText_dc_dest);
            EditText editText_dc_date = findViewById(R.id.editText_dc_date);
            EditText editText_admission_date = findViewById(R.id.editText_admission_date);
            EditText editText_consultant = findViewById(R.id.editText_consultant);
            EditText editText_name = findViewById(R.id.editText_name);
            EditText editText_diagnosis = findViewById(R.id.editText_diagnosis);
            EditText editText_notes = findViewById(R.id.editText_notes);

            editText_dc_dest.setText(p.getPatientDcDest());
            editText_dc_date.setText(p.getPatientDcDate());
            editText_admission_date.setText(p.getPatientAdmDate());
            editText_consultant.setText(p.getPatientConsultant());
            editText_name.setText(p.getPatientName());

            mrn.setText(String.valueOf(p.getPatientRefNumber()));

            //TODO: Add diagnosis field
            //p.setDiagnosis(editText_diagnosis.getText().toString());
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
        CheckBox cb = findViewById(R.id.checkBox_dc_summary);
        CheckBox cb2 = findViewById(R.id.checkBox_order_bloods);
        CheckBox cb3 = findViewById(R.id.checkBox_send_meds);
        CheckBox cb4 = findViewById(R.id.checkBox_discharged);

        p.setPatientBloods(cb2.isChecked());
        p.setPatientMeds(cb3.isChecked());
        p.setPatientDischarged(cb4.isChecked());

        EditText mrn = findViewById(R.id.editText_mrn);
        EditText editText_dc_dest = findViewById(R.id.editText_dc_dest);
        EditText editText_dc_date = findViewById(R.id.editText_dc_date);
        EditText editText_admission_date = findViewById(R.id.editText_admission_date);
        EditText editText_consultant = findViewById(R.id.editText_consultant);
        EditText editText_name = findViewById(R.id.editText_name);
        EditText editText_diagnosis = findViewById(R.id.editText_diagnosis);
        EditText editText_notes = findViewById(R.id.editText_notes);

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
        //TODO: Add diagnosis field
        //p.setDiagnosis(editText_diagnosis.getText().toString());
        p.setPatientNotes(editText_notes.getText().toString());

        if (isEditing) {
            //update exiting patient
            //PatientTasksDB.getDatabase(this).patientDao().update(p.getPatientID(), p);
        } else {
            //add new patient (automatically sets a new primary key as the next available integer? Or does that have to be done here?)
            PatientTasksDB.getDatabase(this).patientDao().insert(p);
            isEditing = true;
        }


        Toast.makeText(this, "Saved patient info",
                Toast.LENGTH_SHORT).show();
    }

    //checkbox toggle
    public void onDischarge(View v) {
        /*CheckBox cb = findViewById(R.id.checkBox_discharged);
        if (cb.isChecked()) {

        }*/
    }
}