package com.example.urduenglish;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                mediaPlayer.start();
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                //When Audio Focus is lost we release media player resources
                releaseMediaPlayer();
            }
            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }

        }
    };
    //we release the media player resources when sound is completely played
    private MediaPlayer.OnCompletionListener mOnCompletionListener= new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        //Setting up the Audio Manager to get Audio Service
        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> arrayList=new ArrayList<Word>();
        arrayList.add(new Word("Black","Kala",R.drawable.color_black,R.raw.black));
        arrayList.add(new Word("Brown","Badami",R.drawable.color_brown,R.raw.brown));
        arrayList.add(new Word("Gray","Surmai",R.drawable.color_gray,R.raw.gray));
        arrayList.add(new Word("Green","Hara",R.drawable.color_green,R.raw.green));
        arrayList.add(new Word("Red","Lal",R.drawable.color_red,R.raw.red));
        arrayList.add(new Word("White","Suffed",R.drawable.color_white,R.raw.white));
        arrayList.add(new Word("Yellow","peela",R.drawable.color_mustard_yellow,R.raw.yellow));

        WordAdapter wordAdapter=new WordAdapter(this,arrayList,R.color.colorsActivity);
        ListView listView=findViewById(R.id.listViewColors);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Release any media player resources before playing any sound
                releaseMediaPlayer();

                //Initialize the object of type Word to get the item clicked
                Word word=arrayList.get(position);

                //Here we request audio focus to play sound (STREAM_MUSIC) for small duration (AUDIOFOCUS_GAIN_TRANSIENT)
                int result = audioManager.requestAudioFocus( mOnAudioFocusChangeListener,audioManager.STREAM_MUSIC,audioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if(result==AudioManager.AUDIOFOCUS_GAIN){
                    //Play Sound
                    //Assigning the resources to media player
                    mediaPlayer= MediaPlayer.create(ColorActivity.this,word.getSoundResourceId());
                    //Start the playing sound
                    mediaPlayer.start();
                    //Release media player when sound is played completely
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
    }

    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            //Abandon Audio Focus so that other apps k use it
            audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
