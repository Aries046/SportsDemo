package com.example.sports.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sports.R;
import com.example.sports.model.ActionRecord;

import java.util.List;

public class ActionRecordAdapter extends RecyclerView.Adapter<ActionRecordAdapter.ActionRecordViewHolder> {

    private List<ActionRecord> actionRecords;

    public ActionRecordAdapter(List<ActionRecord> actionRecords) {
        this.actionRecords = actionRecords;
    }

    @NonNull
    @Override
    public ActionRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action_record, parent, false);
        return new ActionRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionRecordViewHolder holder, int position) {
        ActionRecord record = actionRecords.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return actionRecords.size();
    }

    class ActionRecordViewHolder extends RecyclerView.ViewHolder {
        private TextView timestampTextView;
        private TextView playerNameTextView;
        private TextView actionTextView;

        public ActionRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            playerNameTextView = itemView.findViewById(R.id.playerNameTextView);
            actionTextView = itemView.findViewById(R.id.actionTextView);
        }

        public void bind(ActionRecord record) {
            timestampTextView.setText(record.getFormattedTime());
            playerNameTextView.setText(record.getPlayer().getName());
            actionTextView.setText(record.getAction().getDisplayName());
        }
    }
}