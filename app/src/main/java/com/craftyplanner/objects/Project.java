package com.craftyplanner.objects;

import androidx.lifecycle.MutableLiveData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Project {
    private String id;
    private String title;
    private String description;
    private ArrayList<Task> tasks;
    private MutableLiveData<Integer> taskCount;

    public Project(String title, String description, ArrayList<Task> tasks){
        this.id = createId();
        this.title = title;
        this.description = description;
        this.tasks = tasks;
        this.taskCount = new MutableLiveData<>(0);
    }

    public Project(String id, String title, String description, ArrayList<Task> tasks){
        this.id = id;
        this.title = title;
        this.description = description;
        this.tasks = tasks;
        this.taskCount = new MutableLiveData<>(0);
    }

    public Project(String id, String title, String description, ArrayList<Task> tasks, String taskCount){
        this.id = id;
        this.title = title;
        this.description = description;
        this.tasks = tasks;
        this.taskCount = new MutableLiveData<>(Integer.getInteger(taskCount));
    }

    public String getId() { return id; }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    public MutableLiveData<Integer> getTaskCount(){ return taskCount; }

    public void updateTaskCount(){
        Integer taskCounter = 0;
        for (Task task:tasks
        ) {
            if(task.getStatus().equals("CHECKED")){
                taskCounter++;
            }
        }
        taskCount.setValue(taskCounter);
    }

    private String createId(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}