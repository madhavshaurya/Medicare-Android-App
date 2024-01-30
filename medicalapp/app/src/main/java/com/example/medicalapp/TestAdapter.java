package com.example.medicalapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private List<TestItem> testItems;
    private OnTakeItClickListener onTakeItClickListener; // Listener for "Take it" button

    public TestAdapter(List<TestItem> testItems) {
        this.testItems = testItems;
    }

    public interface OnTakeItClickListener {
        void onTakeItClick(int position);
    }

    public void setOnTakeItClickListener(OnTakeItClickListener listener) {
        this.onTakeItClickListener = listener;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        TestItem testItem = testItems.get(position);
        holder.testNameTextView.setText(testItem.getTestName());
        holder.descriptionTextView.setText(testItem.getDescription());
        holder.priceTextView.setText("$" + testItem.getPrice());

        holder.takeItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTakeItClickListener != null) {
                    onTakeItClickListener.onTakeItClick(holder.getAdapterPosition());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return testItems.size();
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {

        private TextView testNameTextView;
        private TextView descriptionTextView;
        private TextView priceTextView;
        private Button takeItButton;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            testNameTextView = itemView.findViewById(R.id.testNameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            takeItButton = itemView.findViewById(R.id.takeItButton);
        }
    }
}
