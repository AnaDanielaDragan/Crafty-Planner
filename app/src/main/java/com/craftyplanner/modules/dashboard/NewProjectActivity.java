package com.craftyplanner.modules.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class NewProjectActivity extends AppCompatActivity {

    private EditText titleInputText;
    private EditText descriptionInputText;
    private EditText tasksInputText;

    private Button saveProjectButton;

    private ProjectDao projectDao;

    private Project currentProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        titleInputText = findViewById(R.id.id_title_textField);
        descriptionInputText = findViewById(R.id.id_description_textField);
        tasksInputText = findViewById(R.id.id_task_textField);
        saveProjectButton = findViewById(R.id.id_save_new_project_button);

        CustomApplication application = (CustomApplication) getApplication();
        projectDao = application.getProjectDao();

        Intent intent = getIntent();
        String option = intent.getStringExtra("Option");

        if(option.equals("edit")){
            String currentProjectID = intent.getStringExtra("ProjectID");
            currentProject = projectDao.getProjects().get(currentProjectID);

            titleInputText.setText(currentProject.getTitle());
            descriptionInputText.setText(currentProject.getDescription());

            AtomicReference<String> tasks = new AtomicReference<>();
            currentProject.getTasks().forEach(task -> {
                tasks.set(tasks + "," + task.getText());
            });
            tasksInputText.setText(tasks.get());
        }


        saveProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(option.equals("edit")){
                    editProject(currentProject);
                }
                else if(option.equals("save")){
                    saveProject();
                }
            }
        });
    }

    private void editProject(Project currentProject) {
        String title = titleInputText.getText().toString();
        String description = descriptionInputText.getText().toString();
        String tasks = tasksInputText.getText().toString();

        ArrayList<Task> tasksAsList = new ArrayList<>();
        Arrays.stream(tasks.split(",")).forEach(task -> tasksAsList.add(new Task(task, "UNCHECKED")));

        Project project = new Project(currentProject.getId(),title, description, tasksAsList, 0);
        projectDao.updateProject(currentProject.getTitle(), project);

        Toast.makeText(NewProjectActivity.this, "Project saved.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveProject() {
        String title = titleInputText.getText().toString();
        String description = descriptionInputText.getText().toString();
        String tasks = tasksInputText.getText().toString();

        if(title.isEmpty() && description.isEmpty() && tasks.isEmpty()){
            Toast.makeText(NewProjectActivity.this, "Please enter all the data.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Task> tasksAsList = new ArrayList<>();
        Arrays.stream(tasks.split(",")).forEach(task -> tasksAsList.add(new Task(task, "UNCHECKED")));

        Project currentProject = new Project(title, description, tasksAsList, 0);
        projectDao.addProject(currentProject);

        Toast.makeText(NewProjectActivity.this, "Project saved.", Toast.LENGTH_SHORT).show();
        titleInputText.setText("");
        descriptionInputText.setText("");
        tasksInputText.setText("");
        finish();
    }
}
