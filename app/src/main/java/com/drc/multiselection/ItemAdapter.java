package com.drc.multiselection;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private String TAG = "Tag";
    private ArrayList<ItemModel> names;
    private ArrayList<ItemModel> selectedItems = new ArrayList<>();
    private boolean multiSelectEnabled = false; // Flag to indicate multi-selection mode
    private Context context;

    public ItemAdapter(ArrayList<ItemModel> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        ItemModel item = names.get(position);
        holder.name.setText(item.getName());

        int selectedColor = ContextCompat.getColor(context, R.color.success);
        int defaultColor = ContextCompat.getColor(context, R.color.bgCard);

        // Highlight item if selected, else reset
        if (item.isSelected()) {
            holder.cardView.setStrokeColor(selectedColor);
            holder.cardView.setStrokeWidth(12);
        } else {
            holder.cardView.setStrokeColor(defaultColor);
            holder.cardView.setStrokeWidth(0);
        }

        // Handle expand/collapse logic based on item state
        if (item.isExpanded()) {
            holder.cardView.setMinimumHeight(300);
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.cardView.setMinimumHeight(100);
            holder.imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return names != null ? names.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView name;
        private MaterialCardView cardView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            // Enable multi-selection mode and toggle selection
            multiSelectEnabled = true;
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                toggleSelection(position);
            }
            return true; // Indicate that the long-click was handled
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (multiSelectEnabled) {
                    // If multi-selection mode is active, toggle selection
                    toggleSelection(position);
                } else {
                    // Normal click: expand/collapse card view
                    ItemModel item = names.get(position);
                    item.setExpanded(!item.isExpanded());
                    notifyItemChanged(position); // Update the card view
                }
            }
        }

        private void toggleSelection(int position) {
            ItemModel item = names.get(position);
            item.setSelected(!item.isSelected());

            if (item.isSelected()) {
                selectedItems.add(item);
                Log.d(TAG, "Item selected: " + item.getName());
            } else {
                selectedItems.remove(item);
                Log.d(TAG, "Item deselected: " + item.getName());
            }

            // Update selection count and UI in the parent activity
            updateSelectionCount();

            // Refresh the item view to reflect selection changes
            notifyItemChanged(position);
        }


    }

    public void updateSelectionCount() {
        if (context instanceof Activity) {
            int selectedCount = getSelectedCount();
            Activity activity = (Activity) context;

            TextView textView = activity.findViewById(R.id.textView);
            textView.setText("Item Count Selected: " + selectedCount);

            if (selectedCount > 0) {
                activity.findViewById(R.id.button).setVisibility(View.VISIBLE);
            } else {
                activity.findViewById(R.id.button).setVisibility(View.GONE);
                multiSelectEnabled = false;
            }
        }
    }

    public ArrayList<ItemModel> getSelectedItems() {
        return selectedItems;
    }

    public int getSelectedCount() {
        return selectedItems.size();
    }

    public void clearSelection() {
        // Reset all items' selected state
        for (ItemModel item : selectedItems) {
            item.setSelected(false);
        }
        selectedItems.clear();
        multiSelectEnabled = false;
        notifyDataSetChanged(); // Refresh the whole RecyclerView
    }
}
