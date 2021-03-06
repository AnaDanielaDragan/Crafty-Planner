package com.craftyplanner.modules.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.CustomApplication;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.modules.dashboard.TaskListAdapter;
import com.craftyplanner.objects.Project;
import org.jetbrains.annotations.NotNull;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private final Context context;
    private final ArrayMap<String, Project> projects;
    private final ProjectDao projectDao;

    public TasksAdapter(Context context, CustomApplication application, ArrayMap<String, Project> filteredProjects){
        this.context = context;
        this.projectDao = application.getProjectDao();
        this.projects = filteredProjects;
    }

    @NonNull
    @NotNull
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tasks, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull TasksAdapter.ViewHolder holder, int position) {
        // Set data for the elements of each cardView
        Project project = projects.valueAt(position);
        holder.projectName.setText(project.getTitle());
        holder.projectTaskCount.setText(project.getTaskCount().getValue() + "/" + project.getTasks().size());
        holder.projectTaskListRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.projectTaskListRecyclerView.setAdapter(new TaskListAdapter(project, projectDao));
    }

    @Override
    public int getItemCount() {
        // Shows the number of card items in the recyclerView
        return projects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Initializes the view elements of the cardView
        private final TextView projectName;
        private final TextView projectTaskCount;
        private final RecyclerView projectTaskListRecyclerView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.id_cardView_projectName);
            projectTaskCount = itemView.findViewById(R.id.id_cardview_projectTaskCount);
            projectTaskListRecyclerView = itemView.findViewById(R.id.id_recyclerView_taskList);
        }
    }
}
