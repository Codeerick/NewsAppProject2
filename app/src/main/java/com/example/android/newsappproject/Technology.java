package com.example.android.newsappproject;

public class Technology {

// Title of the article
    private String mTitle;

    private String mSection;

    private String mAuthor;

    private  String mDate;

    private String mUrl;


    /** creat an News object
     *@param title news
     *@param section news
     *@param author news
     * @param date news
     *@param url article
     */
    public Technology(String title, String section, String author,String date, String url){
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate =date;
        mUrl = url;


    }
   // gets the title of the news
    public String getTitle() {
        return mTitle;
    }

    //get the information of the news
    public String getSection() {
        return mSection;
    }

    // gets the author of the news
    public String getAuthor() {
        return mAuthor;
    }
   // gets the date of the news
    public String getDate() {
        return mDate;
    }

   // gets the url of the news
    public String getUrl() {
        return mUrl;
    }
}
