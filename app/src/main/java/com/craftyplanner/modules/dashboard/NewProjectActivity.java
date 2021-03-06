package com.craftyplanner.modules.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;

import java.util.ArrayList;

public class NewProjectActivity extends AppCompatActivity {

    private EditText titleInputText;
    private EditText descriptionInputText;
    private EditText taskInputText;
    private RecyclerView recyclerView;
    private Button saveProjectButton;
    private Button addTaskButton;

    private AddTaskAdapter adapter;

    private ProjectDao projectDao;
    private Project currentProject;
    private String option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        CustomApplication application = (CustomApplication) getApplication();
        projectDao = application.getProjectDao();

        Intent intent = getIntent();
        option = intent.getStringExtra("Option");

        initializeElements();

        if(option.equals("edit")){
            String currentProjectID = intent.getStringExtra("ProjectID");
            currentProject = projectDao.getProjects().get(currentProjectID);

            titleInputText.setText(currentProject.getTitle());
            descriptionInputText.setText(currentProject.getDescription());
            setRecyclerViewAdapter();
        }
        if(option.equals("save")){
            currentProject = new Project("", "", new ArrayList<>(), 0, "");

            setRecyclerViewAdapter();
        }
    }

    private void setRecyclerViewAdapter() {
        adapter = new AddTaskAdapter(currentProject.getTasks());
        recyclerView.setAdapter(adapter);
    }

    private void initializeElements() {
        titleInputText = findViewById(R.id.id_title_textField);
        descriptionInputText = findViewById(R.id.id_description_textField);
        taskInputText = findViewById(R.id.enter_task_text);
        saveProjectButton = findViewById(R.id.id_save_new_project_button);
        addTaskButton = findViewById(R.id.add_task_button);

        recyclerView = findViewById(R.id.id_recyclerView_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        saveProjectButton.setOnClickListener(v -> {
            if(option.equals("edit")){
                editProject(currentProject);
            }
            else if(option.equals("save")){
                saveProject();
            }
        });

        addTaskButton.setOnClickListener(v -> {
                currentProject.getTasks().add(new Task(taskInputText.getText().toString(), "UNCHECKED"));
            setRecyclerViewAdapter();
            taskInputText.setText("");
        });
    }

    private void editProject(Project currentProject) {
        String title = titleInputText.getText().toString();
        String description = descriptionInputText.getText().toString();

        Project project = new Project(currentProject.getId(),title, description, adapter.getTasks(), 0, currentProject.getStatus());
        projectDao.updateProject(project);

        Toast.makeText(NewProjectActivity.this, "Project saved.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveProject() {
        String title = titleInputText.getText().toString();
        String description = descriptionInputText.getText().toString();

        if(title.isEmpty() && description.isEmpty()){
            Toast.makeText(NewProjectActivity.this, "Please enter all the data.", Toast.LENGTH_SHORT).show();
            return;
        }

        Project currentProject = new Project(title, description, adapter.getTasks(), 0, "NEW");
        projectDao.addProject(currentProject);

        Toast.makeText(NewProjectActivity.this, "Project saved.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
