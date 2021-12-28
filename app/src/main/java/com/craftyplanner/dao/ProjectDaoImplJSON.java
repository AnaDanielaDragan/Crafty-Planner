package com.craftyplanner.dao;

import android.content.Context;
import android.util.ArrayMap;
import com.craftyplanner.R;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;


public class ProjectDaoImplJSON implements ProjectDao {

    private Context context;

    public ProjectDaoImplJSON(Context context) {
        this.context = context;
    }

    //TODO: implement the methods (read & write data to json file)
    @Override
    public ArrayMap<String, Project> getProjects() {

        ArrayMap<String, Project> projects = new ArrayMap<>();

        JSONParser parser = new JSONParser();
        try {

            InputStream inputStream = context.getResources().openRawResource(R.raw.database);
            String jsonString = new Scanner(inputStream).useDelimiter("\\A").next();
            Object jsonFileContent = parser.parse(jsonString);
            JSONArray jsonProjectsArray = (JSONArray) jsonFileContent;

            Project project;
            ArrayList<Task> tasks;
            for (Object o : jsonProjectsArray) {
                JSONObject jsonProject = (JSONObject) o;

                JSONArray jsonTasks = (JSONArray) ((JSONObject) o).get("tasks");
                assert jsonTasks != null;
                tasks = new ArrayList<>();
                for (Object t : jsonTasks) {
                    JSONObject jsonTask = (JSONObject) t;
                    tasks.add(new Task(jsonTask.get("text").toString(), jsonTask.get("status").toString()));
                }

                project = new Project(jsonProject.get("id").toString(), jsonProject.get("title").toString(), jsonProject.get("description").toString(), tasks);
                projects.put(project.getId(), project);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public Project getProject(String projectId) {
        return getProjects().get(projectId);
    }

    @Override
    public void addProject(Project newProject) {
        ArrayMap<String, Project> projects = getProjects();
        projects.put(newProject.getId(), newProject);

        writeProjectsToJSONFile(projects);
    }

    @Override
    public void deleteProject(String projectId) {
        ArrayMap<String, Project> projects = getProjects();
        projects.remove(projectId);

        writeProjectsToJSONFile(projects);
    }

    @Override
    public void updateProject(Project project) {
        //TODO: see why project is not updating (taskCount is reset after exiting the project activity)
        deleteProject(project.getId());
        addProject(project);
    }

    private void writeProjectsToJSONFile(ArrayMap<String, Project> projects) {
       JSONArray jsonProjects = new JSONArray();

        projects.forEach((id, project) -> {
            JSONArray jsonTasks = new JSONArray();

            project.getTasks().forEach(task -> {
                JSONObject jsonTask = new JSONObject();
                jsonTask.put("text", task.getText());
                jsonTask.put("status", task.getStatus());

                jsonTasks.add(jsonTask);
            });


            JSONObject jsonProject = new JSONObject();
            jsonProject.put("id", id);
            jsonProject.put("title", project.getTitle());
            jsonProject.put("description", project.getDescription());
            jsonProject.put("taskCounter", project.getTaskCount().getValue());
            jsonProject.put("tasks", jsonTasks);

            jsonProjects.add(jsonProject);
        });

        try {

//            File jsonFile = new File("database.json");
//            Writer output = new BufferedWriter(new FileWriter(jsonFile));
//            output.write(jsonProjects.toJSONString());
//            output.close();
//
            FileOutputStream outputStream = context.openFileOutput(new File("database").getName(), Context.MODE_PRIVATE);
            outputStream.write(jsonProjects.toJSONString().getBytes());
            outputStream.close();
//            System.out.println(jsonProjects.toJSONString());
//
//            OutputStreamWriter outputStream = new OutputStreamWriter(context.openFileOutput("database.json", Context.MODE_PRIVATE));
//            outputStream.write(jsonProjects.toJSONString());
//            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
