package com.craftyplanner;

import android.app.Application;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.dao.ProjectDaoImplSQLite;

public class CustomApplication extends Application {
    private static ProjectDao projectDao;

    @Override
    public void onCreate() {
        super.onCreate();
        projectDao = new ProjectDaoImplSQLite(this);
    }

    public ProjectDao getProjectDao(){
        return projectDao;
    }
}
