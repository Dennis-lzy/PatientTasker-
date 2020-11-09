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
        holder.text_dc_summary.setText("DC Summary");
        holder.text_all_ready.setText("All Ready");
        holder.text_meds_sent.setText("Meds Sent");
        holder.checkBox_webster.setChecked(p.isPatientWebster());
        holder.text_name.setText("Name: " + p.getPatientName());
        holder.text_dc_date.setText("DC Date: " + p.getPatientDcDate());
        holder.text_diagnosis.setText("Diagnosis: " + p.getPatientNotes());
        holder.text_consultant.setText("Consultant: " + p.getPatientConsultant());
        holder.text_dc_dest.setText("DC Dest: " + p.getPatientDcDest());

        //SELECT p.name, p.mrn, p.diagnosis, p.consultant, etc. FROM patients as p WHERE p.discharge = false
        //ie. exclude patients that have already been discharged
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
        TextView text_dc_summary;
        TextView text_all_ready;
        TextView text_meds_sent;
        CheckBox checkBox_webster;
        TextView text_name;
        TextView text_dc_date;
        TextView text_diagnosis;
        TextView text_consultant;
        TextView text_dc_dest;
        //Should the seek bar be able to be adjusted from here? If not, it's better to use ProgressBar (Horizontal)
        SeekBar seekBar_dc_progress;

        ViewHolder(View itemView) {
            super(itemView);
            text_dc_summary = itemView.findViewById(R.id.text_dc_summary);
            text_all_ready = itemView.findViewById(R.id.text_all_ready);
            text_meds_sent = itemView.findViewById(R.id.text_meds_sent);
            checkBox_webster = itemView.findViewById(R.id.checkBox_webster);
            text_name = itemView.findViewById(R.id.text_name);
            text_dc_date = itemView.findViewById(R.id.text_dc_date);
            text_diagnosis = itemView.findViewById(R.id.text_diagnosis);
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
