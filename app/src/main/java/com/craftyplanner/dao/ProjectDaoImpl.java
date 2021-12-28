package com.craftyplanner.dao;

import android.util.ArrayMap;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;
import java.util.ArrayList;

public class ProjectDaoImpl implements ProjectDao{

    private static ArrayMap<String, Project> projects;

    public static void initializeProjectList() {

        if(projects == null){
            createProjects();
        }
    }

    private static void createProjects() {
        projects = new ArrayMap<>();

        Project project;
        ArrayList<Task> tasks;

        //Project 1
        tasks = new ArrayList<>();
        tasks.add(new Task("Task 1 of Project 1", "UNCHECKED"));
        tasks.add(new Task("Task 2 of Project 1", "UNCHECKED"));
        tasks.add(new Task("Task 3 of Project 1", "UNCHECKED"));
        tasks.add(new Task("Task 4 of Project 1", "UNCHECKED"));
        tasks.add(new Task("Task 5 of Project 1", "UNCHECKED"));
        project = new Project("Project 1", "This is the description of Project 1", tasks);
        projects.put(project.getId(), project);

        //Project 2
        tasks = new ArrayList<>();
        tasks.add(new Task("Task 1 of Project 2", "UNCHECKED"));
        tasks.add(new Task("Task 2 of Project 2", "UNCHECKED"));
        tasks.add(new Task("Task 3 of Project 2", "UNCHECKED"));
        tasks.add(new Task("Task 4 of Project 2", "UNCHECKED"));
        tasks.add(new Task("Task 5 of Project 2", "UNCHECKED"));
        project = new Project("Project 2", "This is the description of Project 2", tasks);
        projects.put(project.getId(), project);

        //Project 3
        tasks = new ArrayList<>();
        tasks.add(new Task("Task 1 of Project 3", "UNCHECKED"));
        tasks.add(new Task("Task 2 of Project 3", "UNCHECKED"));
        tasks.add(new Task("Task 3 of Project 3", "UNCHECKED"));
        tasks.add(new Task("Task 4 of Project 3", "UNCHECKED"));
        tasks.add(new Task("Task 5 of Project 3", "UNCHECKED"));
        project = new Project("Project 3", "This is the description of Project 3", tasks);
        projects.put(project.getId(), project);

        //Project 4
        tasks = new ArrayList<>();
        tasks.add(new Task("Task 1 of Project 4", "UNCHECKED"));
        tasks.add(new Task("Task 2 of Project 4", "UNCHECKED"));
        tasks.add(new Task("Task 3 of Project 4", "UNCHECKED"));
        tasks.add(new Task("Task 4 of Project 4", "UNCHECKED"));
        tasks.add(new Task("Task 5 of Project 4", "UNCHECKED"));
        project = new Project("Project 4", "This is the description of Project 4", tasks);
        projects.put(project.getId(), project);

        //Project 5
        tasks = new ArrayList<>();
        tasks.add(new Task("Task 1 of Project 5", "UNCHECKED"));
        tasks.add(new Task("Task 2 of Project 5", "UNCHECKED"));
        tasks.add(new Task("Task 3 of Project 5", "UNCHECKED"));
        tasks.add(new Task("Task 4 of Project 5", "UNCHECKED"));
        tasks.add(new Task("Task 5 of Project 5", "UNCHECKED"));
        project = new Project("Project 5", "This is the description of Project 5", tasks);
        projects.put(project.getId(), project);
    }

    @Override
    public ArrayMap<String, Project> getProjects() {
        return projects;
    }

    @Override
    public Project getProject(String projectId) {
        return projects.get(projectId);
    }

    @Override
    public void addProject(Project newProject) {
        projects.put(newProject.getId(), newProject);
    }

    @Override
    public void deleteProject(String projectID) {
        projects.remove(projectID);
    }

    @Override
    public void updateProject(Project project) {
        deleteProject(project.getId());
        addProject(project);
    }
}
