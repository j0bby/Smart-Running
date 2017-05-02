package com.example.user.jobby.API;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mathieu Virsolvy on 02/05/2017.
 */

public class MarkersAPI extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String token = params[0];
        String id = params[1];
        try {
            URL url = new URL("https://smart.domwillia.ms/markers/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            //add request header

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("token", token);
            connection.setRequestProperty("id", id);

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                if(connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST){
                    return "auth error";
                }
                return "error";
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output;
            output = br.readLine();
            connection.disconnect();
            return output;

        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
