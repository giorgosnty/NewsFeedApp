package com.example.android.newsfeedapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by giorgosnty on 26/4/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, ArrayList<Article> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }


        //get currentArtcile and fill the textView appropriately
        Article currentArticle = getItem(position);

        //set up the title
        String currentTitle = currentArticle.getTitle();

        String str = "";
        int i = 0;
        int c = 0;


//        if(currentTitle.length() > 60) {
//
//            String[] splitted = currentTitle.split(" ");
//
//
//            while (c<60)
//            {
//                c += splitted[i].length();
//                i++;
//                str = (String) str + splitted[i];
//
//            }
//
//            currentTitle = str;
//        }

        TextView titleTextview = (TextView) listItemView.findViewById(R.id.title);
        titleTextview.setText(currentTitle);

        //set up the author
        String currentAuthor = currentArticle.getAuthor();
        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentAuthor);


        //set up the section
        String currentSection = currentArticle.getSection();
        TextView sectionTextview = (TextView) listItemView.findViewById(R.id.section);
        sectionTextview.setText(currentSection);


        //set up the date

        //find the date textview
        TextView dateTextview = (TextView) listItemView.findViewById(R.id.date);

        //take the actual date
        String currentDate = currentArticle.getDate().substring(0, 10);

        //update
        dateTextview.setText(currentDate);


        return listItemView;
    }


}
