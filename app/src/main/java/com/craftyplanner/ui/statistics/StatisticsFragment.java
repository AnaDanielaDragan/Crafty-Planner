package com.craftyplanner.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.concurrent.atomic.AtomicReference;

public class StatisticsFragment extends Fragment {

    private View view;

    private PieChart projectPieChart;
    private PieChart taskPieChart;

    private ProjectDao projectDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistics, container, false);

        CustomApplication application = (CustomApplication) getActivity().getApplication();
        projectDao = application.getProjectDao();

        projectPieChart = view.findViewById(R.id.pie_chart_project_status);
        taskPieChart = view.findViewById(R.id.pie_chart_tasks_status);

        setProjectStatistics();
        setTasksStatistics();

        return view;
    }

    private void setProjectStatistics() {
        AtomicReference<Integer> newProjectCount = new AtomicReference<>(0);
        AtomicReference<Integer> inProgressProjectCount = new AtomicReference<>(0);
        AtomicReference<Integer> doneProjectCount = new AtomicReference<>(0);
        projectDao.getProjects().forEach((s, project) -> {
            switch (project.getStatus()) {
                case "NEW":
                    newProjectCount.getAndSet(newProjectCount.get() + 1);
                    break;
                case "IN_PROGRESS":
                    inProgressProjectCount.getAndSet(inProgressProjectCount.get() + 1);
                    break;
                case "DONE":
                    doneProjectCount.getAndSet(doneProjectCount.get() + 1);
                    break;
            }
        });

        Integer total = projectDao.getProjects().size();
        Integer newProjectPercentage = (newProjectCount.get() * 100) / total;
        Integer inProgressProjectPercentage = (inProgressProjectCount.get() * 100) / total;
        Integer doneProjectPercentage = (doneProjectCount.get() * 100) / total;

        // Set the data and color to the pie chart
        projectPieChart.addPieSlice(new PieModel("New", newProjectPercentage, Color.parseColor("#75E401")));
        projectPieChart.addPieSlice(new PieModel("In progress", inProgressProjectPercentage, Color.parseColor("#F9A110")));
        projectPieChart.addPieSlice(new PieModel("Done", doneProjectPercentage, Color.parseColor("#3F0013")));

        // To animate the pie chart
        projectPieChart.startAnimation();
    }

    private void setTasksStatistics() {
        AtomicReference<Integer> checkedTasksCount = new AtomicReference<>(0);
        AtomicReference<Integer> uncheckedTasksCount = new AtomicReference<>(0);

        projectDao.getTasks().forEach((s, task) -> {
            switch (task.getStatus()) {
                case "CHECKED":
                    checkedTasksCount.getAndSet(checkedTasksCount.get() + 1);
                    break;
                case "UNCHECKED":
                    uncheckedTasksCount.getAndSet(uncheckedTasksCount.get() + 1);
                    break;
            }
        });

        Integer total = projectDao.getTasks().size();
        Integer checkedTasksPercentage = (checkedTasksCount.get() * 100) / total;
        Integer uncheckedTasksPercentage = (uncheckedTasksCount.get() * 100) / total;

        // Set the data and color to the pie chart
        taskPieChart.addPieSlice(new PieModel("Checked", checkedTasksPercentage, Color.parseColor("#F9A110")));
        taskPieChart.addPieSlice(new PieModel("Not checked", uncheckedTasksPercentage, Color.parseColor("#808080")));

        // To animate the pie chart
        taskPieChart.startAnimation();
    }
}