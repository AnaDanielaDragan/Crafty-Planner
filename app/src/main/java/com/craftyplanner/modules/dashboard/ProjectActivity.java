package com.craftyplanner.modules.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;
import com.craftyplanner.connectivity.ShareProjectHandler;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.objects.Project;

public class ProjectActivity extends AppCompatActivity {

    private TextView projectTitle;
    private TextView projectDescription;
    private TextView projectTaskCount;
    private RecyclerView recyclerView;

    private ProjectDao projectDao;
    private Project currentProject;
    private String projectId;

    private CustomApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        application = (CustomApplication) getApplication();
        projectDao = application.getProjectDao();

        Intent intent = getIntent();
        projectId = intent.getStringExtra("ProjectID");
        currentProject = projectDao.getProjects().get(projectId);

        initializeElements();
        updateProjectTaskCount();
    }

    @Override
    public void onResume() {
        super.onResume();

        currentProject = projectDao.getProjects().get(projectId);

        initializeElements();
        updateProjectTaskCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_action_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit:
                handleEditProjectActivity();
                return true;
            case R.id.delete:
                handleDeleteProjectAction();
                return true;
            case R.id.share:
                handleShareProjectAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleShareProjectAction() {

        ShareProjectHandler shareProjectHandler = new ShareProjectHandler(application.getApplicationContext());
        shareProjectHandler.startConnection();
        shareProjectHandler.sendProject(currentProject);

    }

    private void handleDeleteProjectAction() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete project");
        alert.setMessage("Are you sure you want to delete " +  currentProject.getTitle() + "?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            projectDao.deleteProject(currentProject);
            finish();
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
        alert.show();
    }

    private void handleEditProjectActivity() {
        Intent intent  = new Intent(this, NewProjectActivity.class);
        intent.putExtra("Option", "edit");
        intent.putExtra("ProjectID", currentProject.getId());
        this.startActivity(intent);
    }


    private void initializeElements() {

        projectTitle = this.findViewById(R.id.id_projectTitle);
        projectDescription = this.findViewById(R.id.id_projectDescription);
        projectTaskCount = this.findViewById(R.id.id_taskCount);

        projectTitle.setText(currentProject.getTitle());
        projectDescription.setText(currentProject.getDescription());

        recyclerView = findViewById(R.id.id_recyclerView_project);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new TaskListAdapter(currentProject, projectDao));
    }

    //Live update the task count
    @SuppressLint("SetTextI18n")
    private void updateProjectTaskCount() {

        currentProject.getTaskCount().observe(this, integer -> projectTaskCount.setText(integer + "/" + currentProject.getTasks().size()));
    }
}