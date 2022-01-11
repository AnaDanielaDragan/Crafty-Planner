package com.craftyplanner.modules.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.R;
import com.craftyplanner.dao.ProjectDao;
import com.craftyplanner.objects.Project;
import com.craftyplanner.objects.Task;
import org.jetbrains.annotations.NotNull;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder>{

    private Context context;
    private Project currentProject;
    private ProjectDao projectDao;

    public ProjectAdapter(Context context, Project currentProject, ProjectDao projectDao){
        this.context = context;
        this.currentProject = currentProject;
        this.projectDao = projectDao;
    }

    @NonNull
    @NotNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_activity_project, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProjectAdapter.ViewHolder holder, int position) {
        Task currentTask = currentProject.getTasks().get(position);
        String currentStatus = currentTask.getStatus();
        holder.task.setText(currentTask.getText());
        holder.task.setChecked(currentStatus.equals("CHECKED"));

        //Count checked tasks (x/y)
        holder.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.task.isChecked()){
                    currentTask.setStatus("CHECKED");
                }
                else{
                    currentTask.setStatus("UNCHECKED");
                }
                currentProject.updateTaskCount();
                projectDao.updateProject(currentProject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return currentProject.getTasks().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox task;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.checkbox_task);
            //TODO: move task to end of the list when checked
        }
    }
}
