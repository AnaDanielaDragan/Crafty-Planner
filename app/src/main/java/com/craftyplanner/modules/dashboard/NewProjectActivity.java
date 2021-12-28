package com.craftyplanner.modules.dashboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.MainActivity;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.dao.ProjectDaoImplSQLite;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class NewProjectActivity extends AppCompatActivity {
    //TODO: edit project (maybe add new task here)
    //TODO: delete project

    private EditText titleInputText;
    private EditText descriptionInputText;
    private EditText tasksInputText;

    private Button saveProjectButton;

    private ProjectDao projectDao;

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


        saveProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInputText.getText().toString();
                String description = descriptionInputText.getText().toString();
                String tasks = tasksInputText.getText().toString();

                if(title.isEmpty() && description.isEmpty() && tasks.isEmpty()){
                    Toast.makeText(NewProjectActivity.this, "Please enter all the data.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Task> tasksAsList = new ArrayList<>();
                Arrays.stream(tasks.split(",")).forEach(task -> tasksAsList.add(new Task(task, "UNCHECKED")));

                Project currentProject = new Project(title, description, tasksAsList);
                projectDao.addProject(currentProject);

                Toast.makeText(NewProjectActivity.this, "Project saved.", Toast.LENGTH_SHORT).show();
                titleInputText.setText("");
                descriptionInputText.setText("");
                tasksInputText.setText("");
            }
        });
    }
}
