package com.example.exp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyTotalAdapter extends RecyclerView.Adapter<DailyTotalAdapter.ViewHolder> {
    private final List<DailyTotal> dailyTotals;

    public DailyTotalAdapter(List<DailyTotal> dailyTotals) {
        this.dailyTotals = dailyTotals;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textTotal = itemView.findViewById(R.id.textTotal);
        }
    }

    @NonNull
    @Override
    public DailyTotalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_total, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTotalAdapter.ViewHolder holder, int position) {
        DailyTotal item = dailyTotals.get(position);
        holder.textDate.setText(item.getDate());
        holder.textTotal.setText("Rs" + item.getTotal());
    }

    @Override
    public int getItemCount() {
        return dailyTotals.size();
    }
}
