package com.craftyplanner.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    private Spinner selectStatusSpinner;

    private ProjectDao projectDao;
    private DashboardAdapter adapter;
    private CustomApplication application;
    private View view;
    private String projectStatus = "ALL";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        application = (CustomApplication) getActivity().getApplication();

        recyclerView = view.findViewById(R.id.id_recyclerView_dashboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        setRecyclerViewAdapter();

        addNewProjectButton = view.findViewById(R.id.id_button_new_project);
        addNewProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(view.getContext(), NewProjectActivity.class);
                intent.putExtra("Option", "save");
                view.getContext().startActivity(intent);
            }
        });

        selectStatusSpinner = view.findViewById(R.id.id_spinner_status);
        String [] statusList = {"ALL","NEW","IN_PROGRESS","DONE"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, statusList);
        selectStatusSpinner.setAdapter(arrayAdapter);

        selectStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                projectStatus = parent.getItemAtPosition(position).toString();
                setRecyclerViewAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerViewAdapter();
    }

    private void setRecyclerViewAdapter() {
        projectDao = application.getProjectDao();
        ArrayMap<String, Project> filteredProjects = new ArrayMap<>();
        projectDao.getProjects().forEach((id, project) -> {
            if(project.getStatus().equals(projectStatus)){
                filteredProjects.put(id, project);
            }
            else if(projectStatus.equals("ALL")){
                filteredProjects.put(id, project);
            }
        });
        //
        adapter = new DashboardAdapter(view.getContext(), filteredProjects);
        recyclerView.setAdapter(adapter);
    }
}