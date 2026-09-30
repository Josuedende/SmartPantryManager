package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    private final List<PantryItem> items;
    private final OnItemClickListener listener;

    public PantryAdapter(List<PantryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.textName.setText(item.getName());

        String quantityText = formatQuantity(item.getQuantity()) + " " + safe(item.getUnit());
        holder.textDetails.setText(quantityText.trim());

        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            holder.textExpiry.setVisibility(View.VISIBLE);
            holder.textExpiry.setText("Expires: " + item.getExpiryDate());
        } else {
            holder.textExpiry.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    private String formatQuantity(double q) {
        if (q == Math.floor(q)) return String.valueOf((int) q);
        return String.valueOf(q);
    }

    private String safe(String s) { return s == null ? "" : s; }

    @Override
    public int getItemCount() { return items.size(); }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDetails, textExpiry;
        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textItemName);
            textDetails = itemView.findViewById(R.id.textItemDetails);
            textExpiry = itemView.findViewById(R.id.textItemExpiry);
        }
    }
}