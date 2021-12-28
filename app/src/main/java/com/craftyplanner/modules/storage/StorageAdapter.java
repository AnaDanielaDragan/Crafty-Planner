package com.craftyplanner.modules.storage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.R;
import com.craftyplanner.objects.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> itemArrayList;

    public StorageAdapter(Context context, ArrayList<Item> itemArrayList){
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public StorageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_storage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StorageAdapter.ViewHolder holder, int position) {
        // Set data for the elements of each cardView
        Item item = itemArrayList.get(position);
        holder.itemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        // Shows the number of card items in the recyclerView
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Initializes the view elements of the cardView
        private TextView itemName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.id_itemName);
        }
    }
}
