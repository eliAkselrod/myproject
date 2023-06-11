package com.example.final_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class GamesFragment extends Fragment {
    public GamesFragment() {
    }

    public static GamesFragment newInstance() {
        GamesFragment fragment = new GamesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            DownLoadText t = new DownLoadText(getActivity());
            t.execute("http://www.ilemon.mobi/site1/asaf.txt");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games, container, false);

    }


    public class DownLoadText extends AsyncTask<String, Integer, String> {
        Context context;
        ProgressDialog p;
        TextView tv;

        public DownLoadText (Context context){
            this.context=context;
        }

        @Override
        protected void onPreExecute() {
            p = ProgressDialog.show(context, "title", "Loading please wait...", true);
            p.setCancelable(false);
            p.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            p.setMessage("Loading...");
            p.show();
            this.context = context;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String line = "";
            HttpURLConnection urlConnection = null;
            URL url = null;
            try {
                URL myURL = new URL(params[0]);
                URLConnection ucon = myURL.openConnection();
                InputStream in = ucon.getInputStream();
                byte[] buffer = new byte[4096];
                in.read(buffer);
                line = new String(buffer);
            } catch (Exception e) {
                line = e.getMessage();
            }
            return line;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null)
                tv.setText(result);
            p.dismiss();
        }
    }
}






