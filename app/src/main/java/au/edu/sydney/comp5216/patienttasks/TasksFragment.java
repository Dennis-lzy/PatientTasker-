package au.edu.sydney.comp5216.patienttasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TasksFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    public void addNewTask(View v) {
        Intent intent = new Intent(this.getContext(), EditTaskActivity.class);

        if (intent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }
}
