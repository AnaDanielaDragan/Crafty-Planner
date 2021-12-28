package com.craftyplanner.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.modules.dashboard.DashboardAdapter;
import com.craftyplanner.modules.dashboard.NewProjectActivity;
import com.craftyplanner.objects.Project;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton addNewProjectButton;

    private ProjectDao projectDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        CustomApplication application = (CustomApplication) getActivity().getApplication();
        projectDao = application.getProjectDao();

        recyclerView = view.findViewById(R.id.id_recyclerView_dashboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        ArrayMap<String, Project> projects = projectDao.getProjects();
        recyclerView.setAdapter(new DashboardAdapter(view.getContext(), projectDao.getProjects()));

        addNewProjectButton = view.findViewById(R.id.id_button_new_project);
        addNewProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(view.getContext(), NewProjectActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }
}