package com.craftyplanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "PROJECTS.DB";
    private static final int DB_VERSION = 7;

    //Projects table
    private static final String PROJECTS_TABLE_NAME = "PROJECTS";
    private static final String ID_COL = "ID";
    private static final String TITLE_COL = "title";
    private static final String DESCRIPTION_COL = "description";
    private static final String TASK_COUNT_COL = "taskCount";
    private static final String PROJECT_STATUS_COL = "status";

    private static final String CREATE_DB_QUERY_PROJECTS = "CREATE TABLE " + PROJECTS_TABLE_NAME + " ( "
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE_COL + " TEXT, "
            + DESCRIPTION_COL + " TEXT, "
            + TASK_COUNT_COL + " INTEGER, "
            + PROJECT_STATUS_COL + " TEXT)";

    //Tasks table
    private static final String TASKS_TABLE_NAME = "TASKS";
    private static final String TEXT_COL = "text";
    private static final String TASK_STATUS_COL = "status";
    private static final String PROJECT_ASSIGNED_COL = "project";


    private static final String CREATE_DB_QUERY_TASKS = "CREATE TABLE " + TASKS_TABLE_NAME + " ( "
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TEXT_COL + " TEXT, "
            + TASK_STATUS_COL + " TEXT, "
            + PROJECT_ASSIGNED_COL + " INTEGER)";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY_PROJECTS);
        db.execSQL(CREATE_DB_QUERY_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PROJECTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE_NAME);
        onCreate(db);
    }

    public void addNewProject(Project newProject) {
        writeProjectToDB(newProject);
        newProject.setId(getProjectID(newProject));
        writeTasksToDB(newProject);
    }

    public void writeTasksToDB(Project newProject) {
        SQLiteDatabase db1 = this.getWritableDatabase();
        ContentValues valuesTasks = new ContentValues();

        newProject.getTasks().forEach(task -> {
            valuesTasks.put(TEXT_COL, task.getText());
            valuesTasks.put(TASK_STATUS_COL, task.getStatus());
            valuesTasks.put(PROJECT_ASSIGNED_COL, newProject.getId());

            db1.insert(TASKS_TABLE_NAME, null, valuesTasks);

            valuesTasks.clear();
        });

        db1.close();
    }

    public void writeProjectToDB(Project newProject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE_COL, newProject.getTitle());
        values.put(DESCRIPTION_COL, newProject.getDescription());
        values.put(TASK_COUNT_COL, 0);
        values.put(PROJECT_STATUS_COL, newProject.getStatus());

        db.insert(PROJECTS_TABLE_NAME, null, values);

        db.close();
    }

    public String getProjectID(Project project){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PROJECTS_TABLE_NAME  + " WHERE title=?", new String[]{project.getTitle()});

        Integer id = null;

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();

        assert id != null;
        return id.toString();
    }
    public Project readProject(String projectID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PROJECTS_TABLE_NAME + " WHERE ID=?", new String[]{projectID});
        Project project = null;

        if(cursor.moveToFirst()){
            do {
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                Integer taskCount = cursor.getInt(3);
                String projectStatus = cursor.getString(4);

                ArrayList<Task> taskList = new ArrayList<>();

                //Read tasks values
                Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME + " WHERE project=?", new String[]{projectID} );
                if(cursorTasks.moveToFirst()){
                    do{
                        String text = cursorTasks.getString(1);
                        String status = cursorTasks.getString(2);

                        taskList.add(new Task(text, status));
                    } while (cursorTasks.moveToNext());
                }
                cursorTasks.close();
                project = new Project(projectID, title, description, taskList, taskCount, projectStatus);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return project;
    }

    public ArrayMap<String, Project> readProjects(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorProjects = db.rawQuery("SELECT * FROM " + PROJECTS_TABLE_NAME, null);

        ArrayMap<String, Project> projects = new ArrayMap<>();

        if (cursorProjects.moveToFirst()) {
            do {
                //Read project values
                String id = cursorProjects.getString(0);
                String title = cursorProjects.getString(1);
                String description = cursorProjects.getString(2);
                Integer taskCount = cursorProjects.getInt(3);
                String projectStatus = cursorProjects.getString(4);

                ArrayList<Task> taskList = new ArrayList<>();

                //Read tasks values
                Cursor cursorTasks = db.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME + " WHERE project=?", new String[]{id} );
                if(cursorTasks.moveToFirst()){
                    do{
                        String text = cursorTasks.getString(1);
                        String status = cursorTasks.getString(2);

                        taskList.add(new Task(text, status));
                    } while (cursorTasks.moveToNext());
                }
                cursorTasks.close();

                projects.put(id, new Project(id, title, description, taskList, taskCount, projectStatus));
            } while (cursorProjects.moveToNext());
        }
        cursorProjects.close();

        db.close();

        return projects;
    }

    public void updateProject(Project project){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE_COL, project.getTitle());
        values.put(DESCRIPTION_COL, project.getDescription());
        values.put(TASK_COUNT_COL, project.getTaskCount().getValue());
        values.put(PROJECT_STATUS_COL, project.getStatus());

        db.update(PROJECTS_TABLE_NAME, values, "ID=?", new String[]{project.getId()});

        //Remove old tasks from table
        db.delete(TASKS_TABLE_NAME, "project=?", new String[]{project.getId()});

        ContentValues valuesTasks = new ContentValues();
        project.getTasks().forEach(task -> {
            valuesTasks.put(TEXT_COL, task.getText());
            valuesTasks.put(TASK_STATUS_COL, task.getStatus());
            valuesTasks.put(PROJECT_ASSIGNED_COL, project.getId());
            db.insert(TASKS_TABLE_NAME, null, valuesTasks);
            valuesTasks.clear();
        });

        db.close();
    }

    public void deleteProject(Project project){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PROJECTS_TABLE_NAME, "ID=?", new String[]{project.getId()});
        db.delete(TASKS_TABLE_NAME, "project=?", new String[]{project.getId()});
        db.close();
    }

    public ArrayMap<String, Task> readTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME, null);

        ArrayMap<String, Task> tasks = new ArrayMap<>();

        if (cursor.moveToFirst()) {
            do {
                //Read task values
                String text = cursor.getString(1);
                String status = cursor.getString(2);
                String projectAssigned = cursor.getString(3);

                tasks.put(projectAssigned, new Task(text, status));
            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();

        return tasks;
    }
}
