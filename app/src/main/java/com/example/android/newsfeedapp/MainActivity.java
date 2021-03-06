package com.example.android.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final int ARTICLE_LOADER_ID = 1;
    private static final String SECTION_TAG = "section";
    private static final String ORDER_BY = "order-by";

    //url that shows content that has to do with sports
    private final String REQUEST_URL = "https://content.guardianapis.com/search?q=sports&api-key=282a9924-3109-4e6d-80b8-4718b6b331da&show-fields=thumbnail";

    private ArticleAdapter articleAdapter;
    private TextView emptyView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checks for network sevice
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //set up listView with adapter
        ListView listView = (ListView) findViewById(R.id.list);

        emptyView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        if (isConnected) {

            articleAdapter = new ArticleAdapter(this, new ArrayList<Article>());
            listView.setAdapter(articleAdapter);

            //set up a listener so that when you tap on an article you go to the
            //appropriate website
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    //take Article being selected
                    Article currentArticle = articleAdapter.getItem(position);

                    //get its uri
                    Uri articleUri = Uri.parse(currentArticle.getUrl());

                    //create an intent
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                    //start the intent
                    startActivity(websiteIntent);
                }
            });

            //initialize loader
            android.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            emptyView.setText(R.string.network);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        String orderBy = sharedPref.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));


        //here we handle the country provided
        String topic = sharedPref.getString(getString(R.string.topic_key), getString(R.string.topic_default));

        //parse breaks the URi string that is passed into its parameters
        Uri baseUri = Uri.parse(REQUEST_URL);

        //buildupon prepares the uri we parsed so that we can add queries
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //append query parameter and its value
        uriBuilder.appendQueryParameter(SECTION_TAG, topic);
        uriBuilder.appendQueryParameter(ORDER_BY, orderBy);


        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {


        articleAdapter.clear();

        if (data != null && !data.isEmpty()) {
            articleAdapter.addAll(data);

        } else {
            emptyView.setText(R.string.empty);

        }

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);


    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        articleAdapter.clear();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
