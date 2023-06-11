package com.example.final_project;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        String data = "";

        try {
            data = downloadUrl(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Process the directions data here
        // ...
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return data;
    }
}
