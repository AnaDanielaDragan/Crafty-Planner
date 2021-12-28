package com.craftyplanner.modules.dashboard;

import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.R;
import com.craftyplanner.objects.Project;
import org.jetbrains.annotations.NotNull;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private Context context;
    private ArrayMap<String, Project> projectArrayMap;

    public DashboardAdapter(Context context, ArrayMap<String, Project> projectArrayMap){
        this.context = context;
        this.projectArrayMap = projectArrayMap;
    }

    @NonNull
    @NotNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DashboardAdapter.ViewHolder holder, int position) {
        // Set data for the elements of each cardView
        Project project = projectArrayMap.valueAt(position);
        holder.projectTitle.setText(project.getTitle());
        holder.projectDescription.setText(project.getDescription());
        holder.projectId = project.getId();
    }

    @Override
    public int getItemCount() {
        // Shows the number of card items in the recyclerView
        return projectArrayMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Initializes the view elements of the cardView
        private TextView projectTitle;
        private TextView projectDescription;
        private String projectId;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            projectTitle = itemView.findViewById(R.id.id_cardview_projectTitle);
            projectDescription = itemView.findViewById(R.id.id_cardview_projectDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(context, ProjectActivity.class);
                    intent.putExtra("ProjectID", projectId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
