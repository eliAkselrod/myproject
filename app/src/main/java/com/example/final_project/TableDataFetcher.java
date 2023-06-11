package com.example.final_project;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TableDataFetcher extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "TableDataFetcher";

    @Override
    protected Void doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://www.eurosport.com/football/liga/standings.shtml"); // Replace with your API endpoint
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                System.out.println("NO TABLE");
                // Handle the error, if any
                return null;
            }

            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line+"eli");
                buffer.append(line).append("\n");
            }

            String tableDataJsonStr = buffer.toString();
            parseTableData(tableDataJsonStr);

        } catch (IOException e) {
            Log.e(TAG, "Error retrieving table data: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing table data: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing reader: " + e.getMessage());
                }
            }
        }

        return null;
    }

    private void parseTableData(String jsonData) throws JSONException {
        JSONArray tableDataArray = new JSONArray(jsonData);

        for (int i = 0; i < tableDataArray.length(); i++) {
            JSONObject rowData = tableDataArray.getJSONObject(i);
            // Parse and process each row of data
            String standings = rowData.getString("standings");
            String teams = rowData.getString("teams");
            String last_results = rowData.getString("last_results");
            String played = rowData.getString("played");
            String wins = rowData.getString("oses");
            String loses = rowData.getString("team");
            String draws = rowData.getString("draws");
            String gd = rowData.getString("goal_difference");
            String pts = rowData.getString("points");
            // Process the data as per your requirement
            Log.d(TAG, "Row " + (i + 1) + ": column1=" + standings + ", column2=" + teams+ ", column3=" + last_results+ ", column4=" +played
                    + ", column5=" +wins + ", column6=" +loses+ ", column7=" +draws+ ", column8=" +gd+ ", column9=" +pts);
        }
    }
}
