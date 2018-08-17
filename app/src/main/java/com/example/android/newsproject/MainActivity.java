package com.example.android.newsproject;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Technology>> {
     // Log messages tag
     private  static final String Log_Tag = MainActivity.class.getName();
     // news data
     private static final String USGS_REQUEST_URL
             ="https://content.guardianapis.com/technology?show-tags=contributor&page-size=15&api-key=54f3f3ab-48c6-4ac3-886a-e4bb5f26254b";
    // the static value
     private static final int TECHNOLOGY_LOADER_ID = 1;

    // list of articles
     private TechnologyAdapter mTechnologyAdapter;
   // empty object
     private TextView mEmptyState;

     //display a loading circle when opening the app
     private ProgressBar mProgressBar;

     // refresh the data
     private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.technology_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        ListView technologyListView = (ListView) findViewById(R.id.list);

        mTechnologyAdapter = new TechnologyAdapter(this, new ArrayList<Technology>());

        technologyListView.setAdapter(mTechnologyAdapter);

        technologyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Technology currentTechnology = mTechnologyAdapter.getItem(position);

                Uri technologyUri = Uri.parse(currentTechnology.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, technologyUri);

                startActivity(websiteIntent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                checkNetwork();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

            }

        });

        mEmptyState = (TextView) findViewById(R.id.empty_view);
        technologyListView.setEmptyView(mEmptyState);

        checkNetwork();
    }
    private  void checkNetwork() {

        ConnectivityManager connectManger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectManger.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(TECHNOLOGY_LOADER_ID, null, this);
        } else {

            mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
            mProgressBar.setVisibility(View.GONE);

            mEmptyState.setText(R.string.No_internet_found);
        }
    }
    @Override
    public android.content.Loader<List<Technology>> onCreateLoader(int i, Bundle bundle){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String fromDate = sharedPrefs.getString(
                getString(R.string.settings_begin_date_key),
                getString(R.string.settings_begin_date_default));


        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy= sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // parse breaks apart the URU
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("q", "tech");
        uriBuilder.appendQueryParameter("from-date", fromDate);
        uriBuilder.appendQueryParameter("orderby", "orderBy");
        uriBuilder.appendQueryParameter("show-tags", "contributor");


        // Return the completed uri `http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=minMagnitude&orderby=time
        return new TechnologyLoader(this, uriBuilder.toString());
    }




    @Override
    public void onLoadFinished(android.content.Loader<List<Technology>> loader, List<Technology> data) {

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        mEmptyState.setText(R.string.No_news_found);

        mTechnologyAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mTechnologyAdapter.addAll(data);
        }

    }
    @Override
    public void onLoaderReset(android.content.Loader<List<Technology>> loader){
        Log.i(Log_Tag,"onLoaderReset() called");
        mTechnologyAdapter.clear();
    }
    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
