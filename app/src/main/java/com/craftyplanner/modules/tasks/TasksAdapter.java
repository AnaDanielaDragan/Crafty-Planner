package com.craftyplanner.modules.tasks;

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

    //TODO: this class displays a list of projectSortedTasksCard (name, count, divider, task ckeckbox list)

    private Context context;
    private ArrayMap<String, Project> projects;
    private ProjectDao projectDao;

    public TasksAdapter(Context context, CustomApplication application){
        this.context = context;
        this.projectDao = application.getProjectDao();
        this.projects = projectDao.getProjects();
    }

    @NonNull
    @NotNull
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tasks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TasksAdapter.ViewHolder holder, int position) {
        // Set data for the elements of each cardView
        Project project = projects.valueAt(position);
        holder.projectName.setText(project.getTitle());
        holder.projectTaskCount.setText(project.getTaskCount().getValue() + "/" + project.getTasks().size());
        holder.projectTaskListRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.projectTaskListRecyclerView.setAdapter(new TaskListAdapter(context, project, projectDao));
    }

    @Override
    public int getItemCount() {
        // Shows the number of card items in the recyclerView
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Initializes the view elements of the cardView
        private TextView projectName;
        private TextView projectTaskCount;
        private RecyclerView projectTaskListRecyclerView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            projectName = itemView.findViewById(R.id.id_cardView_projectName);
            projectTaskCount = itemView.findViewById(R.id.id_cardview_projectTaskCount);
            projectTaskListRecyclerView = itemView.findViewById(R.id.id_recyclerView_taskList);
        }
    }
}
