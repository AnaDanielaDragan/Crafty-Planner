package com.craftyplanner.dao;

import android.content.Context;
import android.util.ArrayMap;
import com.craftyplanner.database.DBHandler;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ProjectDaoImplSQLite implements ProjectDao{

    private DBHandler dbHandler;

    public ProjectDaoImplSQLite(Context context){
        dbHandler = new DBHandler(context);
    }

    @Override
    public ArrayMap<String, Project> getProjects() {
        return dbHandler.readProjects();
    }

    @Override
    public Project getProject(String projectId) {
        return null;
    }

    @Override
    public void addProject(Project newProject) {
        AtomicReference<String> tasks = new AtomicReference<>();
        tasks.set("");

        newProject.getTasks().forEach(task -> {
            tasks.set(tasks + "," + task.getText());
        });
        dbHandler.addNewProject(newProject.getTitle(), newProject.getDescription(), tasks.get());
    }

    @Override
    public void updateProject(Project project) {

    }

    @Override
    public void deleteProject(String projectID) {

    }
}
