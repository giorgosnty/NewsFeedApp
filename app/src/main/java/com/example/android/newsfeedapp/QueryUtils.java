package com.example.android.newsfeedapp;

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

/**
 * Created by giorgosnty on 27/4/2018.
 */

public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }

    public static List<Article> fetchArticlesData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        ArrayList<Article> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }


    private static URL createUrl(String requestUrl) {

        URL url = null;

        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }

        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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


    public static ArrayList<Article> extractFeatureFromJson(String articleJson) {

        if (TextUtils.isEmpty(articleJson)) {
            return null;
        }

        //empty arrayList
        ArrayList<Article> articles = new ArrayList<>();


        //try to parse json and catxh if thre is any problem
        try {
            JSONObject baseJsonResponse = new JSONObject(articleJson);

            JSONObject response = baseJsonResponse.getJSONObject("response");

            //make array based on response jsonarray results
            JSONArray articleArray = response.getJSONArray("results");

            for (int i = 1; i < articleArray.length(); i++) {

                JSONObject currentArticle = articleArray.getJSONObject(i);


                //if there is not a pillarname or webtitle, optString will return " "  and the app will continue
                String section = currentArticle.optString("pillarName");

                String title = currentArticle.optString("webTitle");

                String date;

                try {
                    date = currentArticle.getString("webPublicationDate");
                } catch (JSONException js) {
                    Log.e(LOG_TAG, "Date not found", js);
                    date = "Unknown Date";
                }


                String url = currentArticle.getString("webUrl");

                String author;

                try {
                    author = currentArticle.getString("contributor");
                } catch (JSONException js) {
                    Log.e(LOG_TAG, "Author not found", js);
                    author = "Unknown Author";
                }

                JSONObject fields = currentArticle.getJSONObject("fields");


                String thumbnail  ;

                try {
                    thumbnail = fields.getString("thumbnail");
                } catch (JSONException js) {
                    Log.e(LOG_TAG, "Thumbnail not found", js);
                    thumbnail = Integer.toString(R.drawable.news_app_launcher);
                }



                Article a = new Article(title, author, date, section, url,thumbnail);

                articles.add(a);

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the Article JSON results", e);
        }

        return articles;
    }


}
