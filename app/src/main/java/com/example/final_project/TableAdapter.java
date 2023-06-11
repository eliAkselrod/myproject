package com.example.final_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private List<String[]> tableData;

    public TableAdapter(List<String[]> tableData) {
        this.tableData = tableData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] rowData = tableData.get(position);
        holder.teamTextView.setText(rowData[0]);
        holder.playedTextView.setText(rowData[1]);
        holder.wonTextView.setText(rowData[2]);
        holder.drawnTextView.setText(rowData[3]);
        holder.lostTextView.setText(rowData[4]);
        holder.pointsTextView.setText(rowData[5]);
    }

    @Override
    public int getItemCount() {
        return tableData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView teamTextView;
        public TextView playedTextView;
        public TextView wonTextView;
        public TextView drawnTextView;
        public TextView lostTextView;
        public TextView pointsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            teamTextView = itemView.findViewById(R.id.team);
            playedTextView = itemView.findViewById(R.id.played);
            wonTextView = itemView.findViewById(R.id.wins);
            drawnTextView = itemView.findViewById(R.id.draws);
            lostTextView = itemView.findViewById(R.id.loses);
            pointsTextView = itemView.findViewById(R.id.pts);
        }
    }
}