package com.example.android.newsproject;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    // The helper method
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * private constructor
     */
    private QueryUtils() {

    }

    public static List<Technology> fetchTechnologyData(String requestUrl) {
        URL url = createUrl(requestUrl);

        //calls the HTTPRequest
        String jasonResponse = null;
        try {
            jasonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making the HTTP request", e);
        }
        // call the jason parsing outputs a List
        List<Technology> technologies = extractFeatureFromJson(jasonResponse);

        return technologies;

    }

    // returns a new URL object
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "problem building the URL", e);
            return null;

        }

        return url;
    }

    // makes an HTTP request and returns the response
    private static String makeHttpRequest(URL url) throws IOException {
        String jasonResponse = "";

        // return the response early
        if (url == null) {
            return jasonResponse;
        }


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        //opens a connection to the data.
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(100000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // if successful , input stream is called and the response is parsed.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jasonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem retrieving the article JSON results.", e);
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
        }
        if (inputStream != null) {
            inputStream.close();


             }
        }
            return jasonResponse;

    }



        //convert the link to a string containing the JASON response.

        private static String readFromStream (InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();

        }

// return a list of objects built from parsing the JSON response.
        private static List<Technology> extractFeatureFromJson (String technologyJSON){

            // returns early is the json string is empty.
            if (TextUtils.isEmpty(technologyJSON)) {
                return null;

            }

            //creat an empty Arraylist that will contain the data
            List<Technology> technologies = new ArrayList<>();

            try {
                // Creat a jsonobject
                JSONObject baseJsonResponse = new JSONObject(technologyJSON);
                // extract the JSONObject
                JSONObject technologyObject = baseJsonResponse.getJSONObject("response");
                // extract the JSONArray
                JSONArray results = technologyObject.getJSONArray("results");

                //each technologie in the results  JSONArray
                for (int i = 0; i < results.length(); i++) {
                    //get an article at position i from the list of technologies
                    JSONObject currentTechnology = results.getJSONObject(i);

                    //extract the values for the keys "webTitle","websection", "webDate", and "webUrl".

                    String title = currentTechnology.getString("webTitle");
                    String section = currentTechnology.getString("sectionName");
                    String date = currentTechnology.getString("webPublicationDate");
                    String url = currentTechnology.getString("webUrl");

                    //Extract the JSONArray associated with key tags
                    JSONArray tags = currentTechnology.getJSONArray("tags");
                    String contributer = "";

                    if (tags.length() == 0) {
                        contributer = "No Author";

                    } else {
                        for (int x = 0; x < tags.length(); x++) {
                            // get a contributor at a position
                            JSONObject currentContributor = tags.getJSONObject(x);

                            // Extract the value of the key
                            contributer = currentContributor.getString("webTitle");
                        }


                    }
                    //Create a new object
                    Technology technology = new Technology(title, contributer, section, date, url);

                    //add the new object into the list of article
                    technologies.add(technology);
                }
            } catch (JSONException e) {
                Log.e("QueryUtils", "problem parsing the article JSON results.", e);
            }

            // return the list of technology
            return technologies;
        }
    }
