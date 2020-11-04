package au.edu.sydney.comp5216.patienttasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientViewAdapter extends RecyclerView.Adapter<PatientViewAdapter.ViewHolder> {

    // Keep all Patients in list
    public ArrayList<Patient> patients = new ArrayList<Patient>();
    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public PatientViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);

        //load patients from database into the list (memory)
        //TODO
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Patient p = patients.get(position);
        holder.name.setText(p.getPatientName());
        holder.mrn.setText("MRN: "+p.getPatientRefNumber());
        //TODO: get number of tasks for this patient from database
        //do it for all patients to optimise query - only 1 query should be required for all patients
        //SELECT p.name, p.mrn, p.diagnosis, p.consultant, COUNT(t2.id) as inprogresstasks, COUNT(t.id) as totaltasks FROM patients AS p INNER JOIN tasks as t ON t.patient=p.id INNER JOIN tasks as t2 ON t2.patient=p.id WHERE t2.complete=0 GROUP BY p.id
        //holder.tasks.setText(p.getPatientName());
        //holder.diagnosis.setText(p.getPatientAdmission());
        holder.consultant.setText(p.getPatientConsultant());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return patients.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView mrn;
        TextView tasks;
        TextView diagnosis;
        TextView consultant;
        TextView tasksCount;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            mrn = itemView.findViewById(R.id.text_mrn);
            tasks = itemView.findViewById(R.id.text_tasks);
            diagnosis = itemView.findViewById(R.id.text_diagnosis);
            consultant = itemView.findViewById(R.id.text_consultant);
            tasksCount = itemView.findViewById(R.id.text_task_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
