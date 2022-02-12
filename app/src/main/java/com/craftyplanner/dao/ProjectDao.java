package com.craftyplanner.dao;

import android.util.ArrayMap;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;

public interface ProjectDao{
    ArrayMap<String, Project> getProjects();
    Project getProject(String projectId);
    ArrayMap<String, Task> getTasks();
    void addProject(Project newProject);
    void updateProject(Project project);
    void deleteProject(Project project);
}