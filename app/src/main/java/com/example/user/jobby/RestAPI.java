package com.example.user.jobby;

import com.example.user.jobby.model.Marker;
import com.example.user.jobby.model.Route;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Mathieu Virsolvy on 14/04/2017.
 */

public class RestAPI {

    /**
     * authentication token
     */
    private String token;

    private String name, email, pwd;

    private RestAPI() {
        name = "";
        email = "";
        pwd = "";
        token = "";
    }

    /**
     * the singleton
     */
    private static RestAPI INSTANCE = new RestAPI();

    /**
     * get the singleton
     * @return the singleton
     */
    public static RestAPI getINSTANCE() {
        return INSTANCE;
    }

    /**
     * connect to the server and get the authentication token
     * @param pName username of the user ( not required if email is given)
     * @param pPwd password (required)
     * @param pEmail email of the user (not required if username is given)
     * @return 0 if everything ran smoothly. >0 otherwise.
     */
    public int connect(String pName, String pPwd, String pEmail ){

        if (name.isEmpty()){
            name =pName;
            pwd = pPwd;
            email=pEmail;
        }
        try {

            URL url = new URL("https://smart.domwillia.ms/auth/login/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

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

            System.out.println(input);

            OutputStream os = connection.getOutputStream();
            os.write(input.toString().getBytes("UTF-8"));
            os.flush();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));


            String output = br.readLine();
            JSONObject response = new JSONObject(output);
            token = response.getString("key");

            System.out.println("token = " + token);
            connection.disconnect();


        } catch (MalformedURLException e) {

            e.printStackTrace();
            return 2;

        } catch (IOException e) {

            e.printStackTrace();
            return 3;

        } catch (JSONException e) {
            e.printStackTrace();
            return 4;
        }
        return 0;
    }

    /**
     * make a get request to the server
     * @return the list of all routes available
     */
    public ArrayList<Route> getRoutesList(){

        ArrayList<Route> list = new ArrayList<Route>();

        try {
            URL url = new URL("https://smart.domwillia.ms/markers/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            //add request header

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("token", token);

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            String output;
            output = br.readLine();
            JSONArray response = new JSONArray(output);

            for ( int i =0 ; i <response.length(); i++) {
                JSONObject item= response.getJSONObject(i);

                JSONArray listMarkers = item.getJSONArray("markers");

                ArrayList<Marker> markers = new ArrayList<Marker>();

                for(int m = 0; m<listMarkers.length(); m++){
                    Marker newMarker = new Marker(listMarkers.getJSONObject(m).toString());
                    markers.add(newMarker);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date published = dateFormat.parse(item.getString("date_published"));
                Date lastUpdated = dateFormat.parse(item.getString("last_updated"));
                Route.Mode routeMode;
                switch (item.getString("mode")){
                    case "TOURISTIC" :
                        routeMode = Route.Mode.TOURISTIC;
                        break;
                    case "SPORTY" :
                        routeMode = Route.Mode.SPORTY;
                        break;
                    default:
                        routeMode = Route.Mode.UNSPECIFIED;
                        break;

                }

                Route route = new Route(item.getString("id"), item.getString("title"), item.getString("description"), item.getInt("difficulty"),item.getDouble("rating"), routeMode, published, lastUpdated, markers);

                list.add(route);
            }
            connection.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return list;
    }

    public static void main(String[] Args){
        RestAPI singleton = RestAPI.getINSTANCE();
        singleton.connect("matthew","whatamess","");
        ArrayList<Route> routes = singleton.getRoutesList();
        for(Route r : routes){
            System.out.println("id: "+ r.getId() + "title: " + r.getTitle() + "description: " + r.getDescription() + "difficulty: " + r.getDifficulty() + " rating : " + r.getRating() + "published: " + r.getPublished() + "last: " + r.getLastUpdated() );
            for(Marker m : r.getMarkers()){
                System.out.print("marker : " + m.getId());
            }
        }
    }

}


