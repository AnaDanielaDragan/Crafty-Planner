package com.craftyplanner.dao;

import android.util.ArrayMap;
import com.craftyplanner.objects.Project;

public interface ProjectDao{
    ArrayMap<String, Project> getProjects();
    Project getProject(String projectId);
    void addProject(Project newProject);
    void updateProject(String originalTitle, Project project);
    void deleteProject(Project project);
}
