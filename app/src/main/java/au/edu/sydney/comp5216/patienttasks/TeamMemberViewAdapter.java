package au.edu.sydney.comp5216.patienttasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamMemberViewAdapter extends RecyclerView.Adapter<TeamMemberViewAdapter.ViewHolder> {
    private static final String TAG = "TeamListViewAdapter";
    // Keep all teams in list
    //public List<PatientWithTaskCount> patients = new ArrayList<PatientWithTaskCount>();
    public ArrayList<String> teamMembers = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public TeamMemberViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_team_members, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String teamName = teamMembers.get(position);
        holder.name.setText(teamName);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return teamMembers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_member_name);
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
