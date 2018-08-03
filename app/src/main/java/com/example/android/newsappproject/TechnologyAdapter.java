package com.example.android.newsappproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TechnologyAdapter extends ArrayAdapter<Technology> {

   private static  final String DATE_SEPARATOR= "T";

   public TechnologyAdapter(Activity context, ArrayList<Technology> technologies){
       super(context, 0,technologies);
   }

   //class to hold the the Arraylist objects
    static  class TechnologyViewHolder{
       private TextView title;
       private TextView section;
       private TextView author;
       private TextView date;
   }

   @Override
    // checks if the objects going too be inflated
    public View getView (int position, View convertView, ViewGroup parent) {
       Technology currentTechnology = getItem(position);

       TechnologyViewHolder holder;

       if (convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.technology_list, parent, false);
           holder = new TechnologyViewHolder();
           //finds title, section,author,date,TextViews.
           holder.title = (TextView) convertView.findViewById(R.id.title);
           holder.section = (TextView) convertView.findViewById(R.id.name_of_section);
           holder.author = (TextView) convertView.findViewById(R.id.name_of_author);
           holder.date = (TextView) convertView.findViewById(R.id.date);
       } else {
           holder = (TechnologyViewHolder) convertView.getTag();
       }

       String sectionText = currentTechnology.getSection();

       String authorText = currentTechnology.getAuthor();
       if (authorText == null) {
           holder.author.setVisibility(View.GONE);
       } else {
           holder.author.setText(authorText);
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

       holder.date.setText(date);

       return convertView;
   }
}

