package com.craftyplanner.connectivity;

import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class ProjectParser {

    /*
    String structure:
        projectTitle;projectDescription;task1,task2,...
     */

    public static String parseProjectToString(Project project){
        AtomicReference<String> projectContent = new AtomicReference<>("");

        projectContent.set(projectContent + project.getTitle() + ";" + project.getDescription() + ";");
        project.getTasks().forEach(task -> {
            projectContent.set(projectContent + task.getText() + ",");
        });

        return projectContent.get();
    }

    public static Project parseStringToProject(String string){

        //TODO: test to see if created correctly
        String[] stringContent = string.split(";");
        String title = stringContent[0];
        String description = stringContent[1];
        String[] tasksContent = stringContent[2].split(",");

        ArrayList<Task> tasks = new ArrayList<>();
        Arrays.stream(tasksContent).forEach(task -> {
            tasks.add(new Task(task, "UNCHECKED"));
        });

        return new Project(title, description, tasks, 0);
    }
}
