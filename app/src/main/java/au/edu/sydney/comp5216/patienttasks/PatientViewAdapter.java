package au.edu.sydney.comp5216.patienttasks;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientViewAdapter extends RecyclerView.Adapter<PatientViewAdapter.ViewHolder> {

    // Keep all Patients in list
    public List<PatientWithTaskCount> patients = new ArrayList<>();
    public List<PatientWithTaskCount> fullPatients;

    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public PatientViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        //TEST Patients:
        //patients.add(new PatientWithTaskCount("John Doe"));
        //patients.add(new PatientWithTaskCount("Patient X"));
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
        PatientWithTaskCount p = patients.get(position);
        holder.name.setText(p.getPatientName());
        holder.mrn.setText("MRN: "+p.getPatientRefNumber());
        holder.diagnosis.setText("Dx: "+p.getDiagnosis());
        holder.consultant.setText(p.getPatientConsultant());
        holder.tasksCount.setText(p.getTasksCompleted()+"/"+String.valueOf(p.getTasksCompleted()+p.getTasksInProgress()));

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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView name;
        TextView mrn;
        //TextView tasks;
        TextView diagnosis;
        TextView consultant;
        TextView tasksCount;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            mrn = itemView.findViewById(R.id.text_mrn);
            //tasks = itemView.findViewById(R.id.text_tasks);
            diagnosis = itemView.findViewById(R.id.text_diagnosis);
            consultant = itemView.findViewById(R.id.text_consultant);
            tasksCount = itemView.findViewById(R.id.text_task_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            // Handle long click
            // Return true to indicate the click was handled
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(R.string.dialog_delete_title)
                    .setMessage(R.string.dialog_delete_msg)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            patients.remove(getAdapterPosition());
                            notifyDataSetChanged(); // Notify listView adapter to update the list
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User cancelled the dialog
                            // Nothing happens
                        }
                    });
            builder.create().show();
            return true;
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
