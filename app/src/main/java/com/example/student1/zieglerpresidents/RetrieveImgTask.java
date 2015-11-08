package com.example.student1.zieglerpresidents;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RetrieveImgTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... presName) {

        try {
            URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAHAWO41fKs4WV5zGJSW0oorj7GjvZ-vgE&cx=006673732848801311126:8h5ipa25bf08w&q=" + presName[0]);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            String json = IOUtils.toString(in);
            SearchPicResults results = new Gson().fromJson(json, SearchPicResults.class);
            return results.getItems()[0].getPagemap().getCse_image()[0].getSrc();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    protected void onPostExecute(String result) {
        Log.w("result", result);
    }
}
