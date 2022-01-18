package com.craftyplanner.modules.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;
import com.craftyplanner.connectivity.BluetoothHandler;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.objects.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProjectActivity extends AppCompatActivity {

    private TextView projectTitle;
    private TextView projectDescription;
    private TextView projectTaskCount;
    private RecyclerView recyclerView;
    private Project currentProject;

    private ProjectDao projectDao;

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
        handleProjectTaskCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentProject = projectDao.getProjects().get(projectId);
        initializeElements();
        handleProjectTaskCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_button_menu, menu);
        return true;
    }

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

        BluetoothHandler bluetoothHandler = new BluetoothHandler(getApplication().getApplicationContext());

        bluetoothHandler.initializeBluetooth();
        bluetoothHandler.enableBluetooth();

        String filename = "CraftyPlanner007";
        String fileContents = "Hello world!";
        try (FileOutputStream fos = application.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File internalFile = new File(getApplicationContext().getFilesDir(), filename);
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", internalFile);

        bluetoothHandler.sendFileViaBluetooth(contentUri);


        //Find a Device to pair with
        //Set connection
        //Transfer data
        // -> transfer anything to test the whole stuff (a text or project title)
        // -> transfer project
    }

    private void handleDeleteProjectAction() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete project");
        alert.setMessage("Are you sure you want to delete " +  currentProject.getTitle() + "?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                projectDao.deleteProject(currentProject);
                finish();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
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
        recyclerView.setAdapter(new ProjectAdapter(this, currentProject, projectDao));
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