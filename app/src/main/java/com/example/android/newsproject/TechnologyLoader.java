package com.example.android.newsproject;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.List;

public class TechnologyLoader extends AsyncTaskLoader<List<Technology>> {

    // Log message
    private static final String LOG_TAG = TechnologyLoader.class.getName();

    // query url
    private String mQueryUrl;

    /**
     * @param context of the activity
     * @param queryUrl to load data
     */
    public TechnologyLoader(Context context, String queryUrl) {
        super(context);
        mQueryUrl = queryUrl;
    }

    /**
     * Starts the loading of the data in the  background
     */
    @Override
    protected void  onStartLoading(){
        forceLoad();
    }

    /**
     *  performs in a background thread fetches the data and return it.
     */
    @Override
    public List<Technology> loadInBackground(){
        if(mQueryUrl == null) {
            return null;
        }
        // parse the response and extract it
        List<Technology> technologies = QueryUtils.fetchTechnologyData(mQueryUrl);
        return technologies;
    }

}
