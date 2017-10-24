package com.jasonngo.networking;

import android.content.Context;
import android.os.AsyncTask;

import com.jasonngo.interfaces.OnApiCallback;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DataAsyncTask extends AsyncTask<String, Integer, StringBuilder> {
    private Context context;
    private OnApiCallback callback;

    public DataAsyncTask(Context context, OnApiCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected StringBuilder doInBackground(String... params) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            String urlString = params[0];
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000 /* milliseconds */);
            conn.setConnectTimeout(60000 /* milliseconds */);
            conn.setDoInput(true);
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return jsonResults;
        } catch (IOException e) {
            e.printStackTrace();
            return jsonResults;
        } catch (Exception e){
            e.printStackTrace();
            return jsonResults;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return jsonResults;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        callback.onCompleted();
        if(stringBuilder == null ) callback.onFailure(null);
        else callback.onSuccess(stringBuilder);
    }
}
