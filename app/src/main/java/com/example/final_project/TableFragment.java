package com.example.final_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TableFragment extends Fragment {

    private TextView tableTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        tableTextView = view.findViewById(R.id.tableTextView);

        new FetchTableTask().execute();

        return view;
    }

    private class FetchTableTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Fetch the website content
                Document document = Jsoup.connect(" https://www.eurosport.com/football/liga/standings.shtml").get();
                //"https://api-football-v1.p.rapidapi.com/v3/players").get();
                // Find the table element by its ID or class name
                Element table = document.select("table.my-table").first();

                // Extract the table rows
                Elements rows = table.select("tr");

                // Create a StringBuilder to store the table data
                StringBuilder tableData = new StringBuilder();

                // Iterate over the rows and extract the cell values
                for (Element row : rows) {
                    Elements cells = row.select("td");
                    for (Element cell : cells) {
                        tableData.append(cell.text()).append("\t");
                    }
                    tableData.append("\n");
                }

                // Return the table data as a string
                return tableData.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String tableData) {
            if (tableData != null) {
                // Display the table data in the TextView
                tableTextView.setText(tableData);
            } else {
                // Handle the error case
                tableTextView.setText("Failed to fetch table data.");
            }
        }
    }
}
