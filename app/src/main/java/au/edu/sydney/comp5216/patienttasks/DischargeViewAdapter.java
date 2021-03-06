package au.edu.sydney.comp5216.patienttasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DischargeViewAdapter extends RecyclerView.Adapter<DischargeViewAdapter.ViewHolder> {

    // Keep all Patients in list - only ones that are pending discharge
    public ArrayList<Patient> patients = new ArrayList<Patient>();
    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public DischargeViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);

        //load patients from database into the list (memory)
        //TODO
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_discharge, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Patient p = patients.get(position);
        holder.text_name.setText(p.getPatientName());
        holder.text_dc_date.setText(p.getPatientDcDate());
        holder.text_dc_dest.setText(p.getPatientDcDest());
        holder.text_consultant.setText(p.getPatientConsultant());
        //holder.mrn.setText("MRN: "+p.getPatientRefNumber());
        //TODO: get patients that are currently pending discharge from the database
        //SELECT p.name, p.mrn, p.diagnosis, p.consultant, etc. FROM patients as p WHERE p.discharge = false
        //ie. exclude patients that have already been discharged

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
        CheckBox checkBox_webster;
        TextView text_name;
        TextView text_dc_date;
        TextView text_consultant;
        TextView text_dc_dest;
        //Should the seek bar be able to be adjusted from here? If not, it's better to use ProgressBar (Horizontal)
        SeekBar seekBar_dc_progress;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox_webster = itemView.findViewById(R.id.checkBox_webster);
            text_name = itemView.findViewById(R.id.text_name);
            text_dc_date = itemView.findViewById(R.id.text_dc_date);
            text_consultant = itemView.findViewById(R.id.text_consultant);
            text_dc_dest = itemView.findViewById(R.id.text_dc_dest);

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
