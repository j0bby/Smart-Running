package com.example.user.jobby;

import android.os.AsyncTask;

import com.example.user.jobby.API.RoutesAPI;
import com.example.user.jobby.API.SignInAPI;
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
import java.util.concurrent.ExecutionException;

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
    public String connect(String pName, String pPwd, String pEmail ){

        SignInAPI signin = new SignInAPI();
        try {
            signin.execute(pName, pEmail,pPwd);
            token = signin.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "error";
        }
        if(!token.equals("error") && !token.equals("bad auth")){
            name = pName;
            email=pEmail;
            pwd = pPwd;
        }
        return token;
    }

    /**
     * make a get request to the server
     * @return the list of all routes available
     */
    public ArrayList<Route> getRoutesList() {

        ArrayList<Route> list = new ArrayList<Route>();

        RoutesAPI routesApi = new RoutesAPI();
        routesApi.execute("");
        try {
            String output = routesApi.get();
            if(output.equals("error") || output.equals("auth error")){
                return list;
            }
            JSONArray response = new JSONArray(output);

            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);

                JSONArray listMarkers = item.getJSONArray("markers");

                ArrayList<Marker> markers = new ArrayList<Marker>();

                for (int m = 0; m < listMarkers.length(); m++) {
                    Marker newMarker = new Marker(listMarkers.getString(m));
                    markers.add(newMarker);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date published = dateFormat.parse(item.getString("date_published"));
                Date lastUpdated = dateFormat.parse(item.getString("last_updated"));
                Route.Mode routeMode;
                switch (item.getString("mode")) {
                    case "TOURISTIC":
                        routeMode = Route.Mode.TOURISTIC;
                        break;
                    case "SPORTY":
                        routeMode = Route.Mode.SPORTY;
                        break;
                    default:
                        routeMode = Route.Mode.UNSPECIFIED;
                        break;

                }

                Route route = new Route(item.getString("id"), item.getString("title"), item.getString("description"), item.getInt("difficulty"), item.getDouble("rating"), routeMode, published, lastUpdated, markers);

                list.add(route);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] Args){
        RestAPI singleton = RestAPI.getINSTANCE();
        singleton.connect("matthew","whatamess","");
       /* ArrayList<Route> routes = singleton.getRoutesList();
        for(Route r : routes){
            System.out.println("id: "+ r.getId() + "title: " + r.getTitle() + "description: " + r.getDescription() + "difficulty: " + r.getDifficulty() + " rating : " + r.getRating() + "published: " + r.getPublished() + "last: " + r.getLastUpdated() );
            for(Marker m : r.getMarkers()){
                System.out.print("marker : " + m.getId());
            }
        }*/
    }

}


