package com.drc.multiselection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemAdapter adapter;

    private ArrayList<ItemModel> namesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        namesList = new ArrayList<>();
        adapter = new ItemAdapter(namesList, MainActivity.this);
        addNames();

        recyclerView = findViewById(R.id.rc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        TextView textView = findViewById(R.id.textView);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                textView.setText("Item Count Selected: " + adapter.getSelectedItems().size());
//            }
//        });

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ItemModel> selectedItems = adapter.getSelectedItems();
                ArrayList<ItemModel> itemsToRemove = new ArrayList<>();

                for (ItemModel item : selectedItems) {
                    Log.d("Selected Items", "Item name: " + item.getName());
                    itemsToRemove.add(item);
                }

                namesList.removeAll(itemsToRemove);
                selectedItems.clear();

                adapter.updateSelectionCount();

                textView.setText("Item Count Selected: " + adapter.getSelectedItems().size());

                adapter.notifyDataSetChanged();
                button.setVisibility(View.GONE);
            }
        });


    }


    private void addNames() {
        namesList.clear(); // Clear any existing data if necessary
        String[] nameArray = {
                "Layla", "John", "Emma", "Oliver", "Sophia",
                "Liam", "Olivia", "Noah", "Ava", "William",
                "Charlotte", "James", "Amelia", "Benjamin", "Mia",
                "Elijah", "Evelyn", "Lucas", "Harper", "Mason"
        };

        for (String name : nameArray) {
            ItemModel item = new ItemModel(name);
            namesList.add(item);
        }

        adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

}