package com.craftyplanner;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import com.craftyplanner.connectivity.ProjectContentParser;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.objects.Project;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.craftyplanner.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private ProjectDao projectDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomApplication application = (CustomApplication) getApplication();
        projectDao = application.getProjectDao();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_tasks, R.id.navigation_dashboard, R.id.navigation_statistics)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sync_button_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.sync) {
            handleProjectSyncAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleProjectSyncAction() {

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS);

        Arrays.stream(directory.listFiles()).forEach(file -> {
            if(file.getName().equals("CraftyPlanner007")){
                createProjectSyncAlertDialog(file);
            }
        });
    }

    private void createProjectSyncAlertDialog(File file) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New project available");
        alert.setMessage("Save project to personal Dashboard?");
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {

            StringBuilder contentBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines( Paths.get(file.getPath()), StandardCharsets.UTF_8))
            {
                stream.forEach(contentBuilder::append);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String projectContent = contentBuilder.toString();
            Project importedProject = ProjectContentParser.parseStringToProject(projectContent);
            projectDao.addProject(importedProject);

            file.delete();
        });
        alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
        alert.show();
    }
}