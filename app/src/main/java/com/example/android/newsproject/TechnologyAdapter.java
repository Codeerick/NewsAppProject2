package com.example.android.newsproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TechnologyAdapter extends ArrayAdapter<Technology> {

   private static  final String DATE_SEPARATOR= "T";
    private TextView title;
    private TextView section;
    private TextView author;
    private TextView dateView;

   public TechnologyAdapter(Activity context, ArrayList<Technology> technologies){
       super(context, 0,technologies);
   }
   @Override
    // checks if the objects going too be inflated
    public View getView (int position, View convertView, ViewGroup parent) {


       View listItemView = convertView;


       if (listItemView == null) {
           listItemView = LayoutInflater.from(getContext()).inflate(R.layout.technology_list, parent, false);
       }
       Technology currentTechnology = getItem(position);

       title = (TextView) listItemView.findViewById(R.id.article_name);
       section = (TextView) listItemView.findViewById(R.id.name_of_section);
       author = (TextView) listItemView.findViewById(R.id.name_of_author);
       dateView = (TextView) listItemView.findViewById(R.id.date);

       String titleText = currentTechnology.getTitle();

       String sectionText = currentTechnology.getSection();

       String authorText = currentTechnology.getAuthor();
       if (authorText == null) {
           author.setVisibility(View.GONE);
       } else {
           title.setText(titleText);
           section.setText(sectionText);
          author.setText(authorText);

       }
       String originalDate = currentTechnology.getDate();
       String date = null;
      // sets the date information
       if (originalDate.contains(DATE_SEPARATOR)) {
           String[] parts = originalDate.split(DATE_SEPARATOR);
           date = parts[0];
       }

       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
       Date newDate = null;
       try {
           newDate = spf.parse(date);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       spf = new SimpleDateFormat("MMM dd,yyy", Locale.ENGLISH);
       date = spf.format(newDate);

       dateView.setText(date);

       return listItemView;
   }
}

