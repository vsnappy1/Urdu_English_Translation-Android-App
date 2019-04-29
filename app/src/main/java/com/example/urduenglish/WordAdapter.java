package com.example.urduenglish;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    //This holds the background color for linear layout/ Textviews
    private int colorResourceId;

    public WordAdapter(Activity context, ArrayList<Word> wordList, int BackgroundColorId) {
        super(context,0, wordList);
        colorResourceId=BackgroundColorId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View ListItemView = convertView;
        if(ListItemView==null){
            ListItemView= LayoutInflater.from(getContext()).inflate(R.layout.custom_view,parent,false);
        }

        //This Gets the position or word
        Word currentWord=getItem(position);

        //Finds the TextView in custom_view XML file
        TextView urdu=ListItemView.findViewById(R.id.textViewUrdu);
        //Get the urdu Translation from currentWord object
        //Set this text on urdu TextView
        urdu.setText(currentWord.getUrduTranslastion());

        //Finds the TextView in custom_view XML file
        TextView english=ListItemView.findViewById(R.id.textViewEnglish);
        //Get the english Translation from currentWord object
        //Set this text on english TextView
        english.setText(currentWord.getEnglishTranslation());

        //Finds the ImageView in custom_view XML file
        ImageView image=ListItemView.findViewById(R.id.imageView);
        //Get the Image from currentWord object
        //Set this Image on imageView
        image.setImageResource(currentWord.getImageId());



        //Finds the text container in the custom_view
        View textContainer=ListItemView.findViewById(R.id.linarLayoutTextContainter);
        //Finds the color that Resource Id maps to
        int color= ContextCompat.getColor(getContext(),colorResourceId);
        //Sets the background color of the text container view
        textContainer.setBackgroundColor(color);


        return ListItemView;
    }
}
