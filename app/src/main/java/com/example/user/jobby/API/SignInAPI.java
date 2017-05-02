package com.example.user.jobby.API;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Mathieu Virsolvy on 02/05/2017.
 */

public class SignInAPI extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String name = params[0];
        String email = params[1];
        String pwd = params[2];

        String token = "vide";
        try {

            URL url = new URL("https://smart.domwillia.ms/auth/login/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            //add request header
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");


            /**JSONObject input = new JSONObject();
             input.put("username", name);
             input.put("email", email);
             input.put("password", pwd);*/
            String input = "{\"username\": \""+ name +"\",\"email\": \""+ email + "\",\"password\": \""+pwd+"\"}";


            OutputStream os = connection.getOutputStream();
            os.write(input.toString().getBytes("UTF-8"));
            os.flush();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                if(connection.getResponseCode() == HttpsURLConnection.HTTP_BAD_REQUEST){
                    return "bad auth";
                }else{
                    return "error";
                }

            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));


            String output = br.readLine();
            JSONObject response = new JSONObject(output);
            token = response.getString("key");
            //token=output;

            connection.disconnect();


        } catch (MalformedURLException e) {

            e.printStackTrace();
            return "error";

        } catch (IOException e) {

            e.printStackTrace();
            return "error";

        } catch (JSONException e) {
            e.printStackTrace();
            return "error";
        }
        return token;
    }
}

