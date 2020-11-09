package au.edu.sydney.comp5216.patienttasks;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.ViewHolder> {

    // Keep all tasks in list
    public List<TaskWithPatient> tasks = new ArrayList<>();
    public List<TaskWithPatient> fullTasks = null;
    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public TaskViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
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
        final TaskWithPatient task = tasks.get(position);
        holder.name.setText(task.getTaskName());
        holder.patient.setText(task.getPatientName()+", "+task.getPatientMRN());
        holder.assigned.setText("Assigned to: " + task.getUserName());
        holder.due.setText("Due Date: " + task.getTaskDueDate());
        holder.subtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            if (task.isTaskCompleted()) {
                                PatientTasksDB.getDatabase(mContext).taskDao().uncompleteTask(task.getTaskID());
                                Log.i("SQLite uncompleted item", "Task: " + task.getTaskName() + '\n');
                            } else {
                                PatientTasksDB.getDatabase(mContext).taskDao().completeTask(task.getTaskID());
                                Log.i("SQLite completed item", "Task: " + task.getTaskName() + '\n');
                            }

                            return null;
                        }
                    }.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (task.isTaskCompleted()) {
            holder.subtask.setChecked(true);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
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
            itemView.setOnLongClickListener(this);
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
                            tasks.remove(getAdapterPosition());
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
