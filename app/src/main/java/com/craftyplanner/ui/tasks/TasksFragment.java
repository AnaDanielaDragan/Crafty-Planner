package com.craftyplanner.ui.tasks;

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
import com.craftyplanner.R;;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.modules.tasks.TasksAdapter;
import com.craftyplanner.objects.Project;

public class TasksFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    private Spinner selectedProjects;

    private ProjectDao projectDao;
    private CustomApplication application;
    private String projectStatus = "ALL";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tasks, container, false);

        application = (CustomApplication) getActivity().getApplication();
        projectDao = application.getProjectDao();

        initializeFilterSpinner();
        initializeTaskListRecyclerView();

        return view;
    }

    private void initializeTaskListRecyclerView() {
        recyclerView = view.findViewById(R.id.id_recyclerView_tasks_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        setRecyclerViewAdapter();
    }

    private void initializeFilterSpinner() {
        selectedProjects = view.findViewById(R.id.id_spinner_projects_selected);
        setArrayAdapter();
        selectedProjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

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
    }

    private void setArrayAdapter() {
        String [] statusList = {"ALL","IN_PROGRESS"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<> (getActivity().getBaseContext(), android.R.layout.simple_spinner_dropdown_item, statusList);
        selectedProjects.setAdapter(arrayAdapter);
    }

    private void setRecyclerViewAdapter(){
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

        recyclerView.setAdapter(new TasksAdapter(view.getContext(), application, filteredProjects));
    }
}