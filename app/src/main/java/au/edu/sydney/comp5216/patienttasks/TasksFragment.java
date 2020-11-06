package au.edu.sydney.comp5216.patienttasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TasksFragment extends Fragment implements TaskViewAdapter.ItemClickListener {

    TaskViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // set up the RecyclerView
        View view = inflater.inflate(R.layout.fragment_patients, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new TaskViewAdapter(this.getContext());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //database query (async) for fetching tasks
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    List<Task> taskDB = PatientTasksDB.getDatabase(TasksFragment.this.getContext()).taskDao().listAll();

                    Log.i("Tasks", "Database query for tasks retrieved " + taskDB.size() + " tasks.");

                    adapter.tasks.addAll(taskDB);
                    adapter.notifyDataSetChanged();

                    return null;
                }
            }.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    //@Override
    //public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // set up the RecyclerView
        //RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //adapter = new TaskViewAdapter(this.getContext());
        //adapter.setClickListener(this);
        //recyclerView.setAdapter(adapter);
    //}

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this.getContext(), "Editing task " + adapter.tasks.get(position).getTaskName() + " on row number " + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this.getContext(), EditTaskActivity.class);
        intent.putExtra("editing", true);
        intent.putExtra("task", adapter.tasks.get(position));

        if (intent.resolveActivity(this.getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
    }
}
