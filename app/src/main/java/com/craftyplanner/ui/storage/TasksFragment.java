package com.craftyplanner.ui.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.modules.tasks.TasksAdapter;

public class TasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProjectDao projectDao;
    private CustomApplication application;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        application = (CustomApplication) getActivity().getApplication();
        projectDao = application.getProjectDao();

        recyclerView = view.findViewById(R.id.id_recyclerView_tasks_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new TasksAdapter(view.getContext(), application));

        return view;
    }
}