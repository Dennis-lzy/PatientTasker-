package au.edu.sydney.comp5216.patienttasks;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SubtaskViewAdapter extends RecyclerView.Adapter<SubtaskViewAdapter.ViewHolder> {

    // Keep all subtasks in list
    public List<SubTask> subtasks = new ArrayList<SubTask>();

    private Context mContext;
    private LayoutInflater mInflater;
    private SubtaskViewAdapter.ItemClickListener mClickListener;

    public SubtaskViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public SubtaskViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_subtask, parent, false);
        return new SubtaskViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(SubtaskViewAdapter.ViewHolder holder, int position) {
        SubTask p = subtasks.get(position);
        holder.name.setText(p.getSubTaskName());
        holder.completed.setChecked(p.getSubTaskCompleted() == 1);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return subtasks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView name;
        CheckBox completed;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_subtask_name);
            completed = itemView.findViewById(R.id.checkBox_subtask_complete);
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
                            subtasks.remove(getAdapterPosition());
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
    void setClickListener(SubtaskViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
