package com.example.final_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<TableRow> tableDataList;

    public RecyclerViewAdapter(List<TableRow> tableDataList) {
        this.tableDataList = tableDataList;
    }

    public void setTableDataList(List<TableRow> tableDataList) {
        this.tableDataList = tableDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TableRow tableRow = tableDataList.get(position);

        holder.teamTextView.setText(tableRow.getTeam());
        holder.pTextView.setText(String.valueOf(tableRow.getP()));
        holder.wTextView.setText(String.valueOf(tableRow.getW()));
        holder.lTextView.setText(String.valueOf(tableRow.getL()));
        holder.dTextView.setText(String.valueOf(tableRow.getD()));
        holder.ptsTextView.setText(String.valueOf(tableRow.getPts()));
    }

    @Override
    public int getItemCount() {
        return tableDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView teamTextView;
        public TextView pTextView;
        public TextView wTextView;
        public TextView lTextView;
        public TextView dTextView;
        public TextView ptsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            teamTextView = itemView.findViewById(R.id.team);
            pTextView = itemView.findViewById(R.id.played);
            wTextView = itemView.findViewById(R.id.wins);
            lTextView = itemView.findViewById(R.id.loses);
            dTextView = itemView.findViewById(R.id.draws);
            ptsTextView = itemView.findViewById(R.id.pts);
        }
    }
}


