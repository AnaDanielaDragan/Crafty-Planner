package com.craftyplanner.dao;

import android.content.Context;
import android.util.ArrayMap;
import com.craftyplanner.database.DBHandler;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;

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
        return dbHandler.readProject(projectId);
    }

    @Override
    public ArrayMap<String, Task> getTasks() {
        return dbHandler.readTasks();
    }

    @Override
    public void addProject(Project newProject) {
        dbHandler.addNewProject(newProject);
    }

    @Override
    public void updateProject(Project project) {
        dbHandler.updateProject(project);
    }

    @Override
    public void deleteProject(Project project) {
        dbHandler.deleteProject(project);
    }
}
