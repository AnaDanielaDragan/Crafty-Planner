package com.craftyplanner.modules.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.R;
import com.craftyplanner.objects.Task;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>{

    private ArrayList<Task> tasks;

    public TasksAdapter(ArrayList<Task> tasks){
        this.tasks = tasks;
    }

    @NonNull
    @NotNull
    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_task_entry_new_project_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TasksAdapter.ViewHolder holder, int position) {
        Task currentTask = tasks.get(position);

        holder.text.setText(currentTask.getText());
        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tasks.remove(currentTask);
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), tasks.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView text;
        private Button deleteButton;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_task);
            deleteButton = itemView.findViewById(R.id.delete_task_button);
        }
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }
}
