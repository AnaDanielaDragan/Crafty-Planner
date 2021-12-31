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
import java.util.Arrays;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "PROJECTS.DB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "PROJECTS";
    private static final String ID_COL = "ID";

    private static final String TITLE_COL = "title";
    private static final String DESCRIPTION_COL = "description";
    private static final String TASKS_COL = "tasks";
    private static final String TASK_COUNT_COL = "taskCount";

    private static final String CREATE_DB_QUERY = "CREATE TABLE " + TABLE_NAME + " ( "
            + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE_COL + " TEXT, "
            + DESCRIPTION_COL + " TEXT, "
            + TASKS_COL + " TEXT, "
            + TASK_COUNT_COL + " INTEGER)";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);
    }

    private void initializeDB() {
        addNewProject("Project 1", "This is the description of Project 1.", "Task 1, Task 2, Task 3");
        addNewProject("Project 2", "This is the description of Project 2.", "Task 1, Task 2, Task 3");
        addNewProject("Project 3", "This is the description of Project 3.", "Task 1, Task 2, Task 3");
        addNewProject("Project 4", "This is the description of Project 4.", "Task 1, Task 2, Task 3");
        addNewProject("Project 5", "This is the description of Project 5.", "Task 1, Task 2, Task 3");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNewProject(String title, String description, String tasks) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE_COL, title);
        values.put(DESCRIPTION_COL, description);
        values.put(TASKS_COL, tasks);
        values.put(TASK_COUNT_COL, 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayMap<String, Project> readProjects(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorProjects = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayMap<String, Project> projects = new ArrayMap<>();

        if (cursorProjects.moveToFirst()) {
            do {
                String id = cursorProjects.getString(0);
                String title = cursorProjects.getString(1);
                String description = cursorProjects.getString(2);
                String tasks = cursorProjects.getString(3);
                String taskCount = cursorProjects.getString(4);

                ArrayList<Task> taskList = new ArrayList<>();
                Arrays.stream(tasks.split(",")).forEach(task ->taskList.add(new Task(task, "UNCHECKED")));

                projects.put(id, new Project(id, title, description, taskList, taskCount));
            } while (cursorProjects.moveToNext());
        }
        cursorProjects.close();

        return projects;
    }

    public void updateProject(String originalTitle, String title, String description, String tasks, Integer taskCount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE_COL, title);
        values.put(DESCRIPTION_COL, description);
        values.put(TASKS_COL, tasks);
        values.put(TASK_COUNT_COL, taskCount);

        db.update(TABLE_NAME, values, "title=?", new String[]{originalTitle});
        db.close();
    }

    public void deleteProject(String title){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "title=?", new String[]{title});
        db.close();
    }

}
