package com.craftyplanner.ui.storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craftyplanner.R;;
import com.craftyplanner.modules.storage.StorageAdapter;
import com.craftyplanner.objects.Item;

import java.util.ArrayList;

public class StorageFragment extends Fragment {

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_storage, container, false);

        recyclerView = view.findViewById(R.id.id_recyclerView_storage);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new StorageAdapter(view.getContext(), createItemArrayList()));

        return view;
    }

    private ArrayList<Item> createItemArrayList(){

        ArrayList<Item> itemArrayList = new ArrayList<>();

        itemArrayList.add(new Item("Item 1"));
        itemArrayList.add(new Item("Item 2"));
        itemArrayList.add(new Item("Item 3"));
        itemArrayList.add(new Item("Item 4"));
        itemArrayList.add(new Item("Item 5"));
        itemArrayList.add(new Item("Item 6"));
        itemArrayList.add(new Item("Item 7"));
        itemArrayList.add(new Item("Item 8"));
        itemArrayList.add(new Item("Item 9"));
        itemArrayList.add(new Item("Item 10"));
        itemArrayList.add(new Item("Item 11"));
        itemArrayList.add(new Item("Item 12"));
        itemArrayList.add(new Item("Item 13"));
        itemArrayList.add(new Item("Item 14"));
        itemArrayList.add(new Item("Item 15"));
        itemArrayList.add(new Item("Item 16"));
        itemArrayList.add(new Item("Item 17"));
        itemArrayList.add(new Item("Item 18"));

        return itemArrayList;
    }
}