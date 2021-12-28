package com.craftyplanner.modules.dashboard;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.MainActivity;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.dao.ProjectDaoImpl;
import com.craftyplanner.objects.Project;

public class ProjectActivity extends AppCompatActivity {

    private TextView projectTitle;
    private TextView projectDescription;
    private TextView projectTaskCount;
    private RecyclerView recyclerView;
    private Project currentProject;

    private ProjectDao projectDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        CustomApplication application = (CustomApplication) getApplication();
        projectDao = application.getProjectDao();

        Intent intent = getIntent();
        String projectId = intent.getStringExtra("ProjectID");
        currentProject = projectDao.getProjects().get(projectId);

        initializeElements();
        handleProjectTaskCount();
    }

    private void initializeElements() {

        projectTitle = this.findViewById(R.id.id_projectTitle);
        projectDescription = this.findViewById(R.id.id_projectDescription);
        projectTaskCount = this.findViewById(R.id.id_taskCount);

        projectTitle.setText(currentProject.getTitle());
        projectDescription.setText(currentProject.getDescription());

        recyclerView = findViewById(R.id.id_recyclerView_project);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ProjectAdapter(this, currentProject));
    }

    //Live update the task count
    private void handleProjectTaskCount() {

        currentProject.getTaskCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                projectTaskCount.setText(integer + "/" + currentProject.getTasks().size());
            }
        });
    }
}