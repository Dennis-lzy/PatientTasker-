package au.edu.sydney.comp5216.patienttasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.ViewHolder> {

    // Keep all tasks in list
    public ArrayList<Task> patients = new ArrayList<Task>();
    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public TaskViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);

        //load patients from database into the list (memory)
        //TODO
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task t = patients.get(position);
        //holder.name.setText(t.gettask);
        //holder.mrn.setText("MRN: "+p.getPatientRefNumber());
        //TODO: get task information from database, including patient info via INNER JOIN
        //only 1 query should be required for all tasks
        //holder.tasks.setText(p.getPatientName());
        //holder.diagnosis.setText(p.getPatientAdmission());
        //holder.consultant.setText(p.getPatientConsultant());

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
        TextView patient;
        TextView assigned;
        TextView due;
        CheckBox subtask;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_task_name);
            patient = itemView.findViewById(R.id.text_patient);
            assigned = itemView.findViewById(R.id.text_assigned);
            due = itemView.findViewById(R.id.text_task_due);
            subtask = itemView.findViewById(R.id.checkBox_subtask);

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
