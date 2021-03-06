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

public class AddTaskAdapter extends RecyclerView.Adapter<AddTaskAdapter.ViewHolder>{

    private final ArrayList<Task> tasks;

    public AddTaskAdapter(ArrayList<Task> tasks){
        this.tasks = tasks;
    }

    @NonNull
    @NotNull
    @Override
    public AddTaskAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_task_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddTaskAdapter.ViewHolder holder, int position) {
        Task currentTask = tasks.get(position);

        holder.text.setText(currentTask.getText());
        holder.deleteButton.setOnClickListener(v -> {
            tasks.remove(currentTask);
            notifyItemRemoved(holder.getAdapterPosition());
            notifyItemRangeChanged(holder.getAdapterPosition(), tasks.size());
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView text;
        private final Button deleteButton;

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
