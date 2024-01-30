package com.example.medicalapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduledTestAdapter extends RecyclerView.Adapter<ScheduledTestAdapter.ScheduledTestViewHolder> {

    private List<TestItem> scheduledTests;

    public ScheduledTestAdapter(List<TestItem> scheduledTests) {
        this.scheduledTests = scheduledTests;
    }

    @NonNull
    @Override
    public ScheduledTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scheduled_test, parent, false);
        return new ScheduledTestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduledTestViewHolder holder, int position) {
        TestItem testItem = scheduledTests.get(position);
        holder.testNameTextView.setText(testItem.getTestName());
        holder.descriptionTextView.setText(testItem.getDescription());
        holder.priceTextView.setText("$" + testItem.getPrice());
    }

    @Override
    public int getItemCount() {
        return scheduledTests.size();
    }

    public class ScheduledTestViewHolder extends RecyclerView.ViewHolder {

        private TextView testNameTextView;
        private TextView descriptionTextView;
        private TextView priceTextView;

        public ScheduledTestViewHolder(@NonNull View itemView) {
            super(itemView);
            testNameTextView = itemView.findViewById(R.id.testNameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}
