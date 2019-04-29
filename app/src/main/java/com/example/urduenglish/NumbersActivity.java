package com.example.urduenglish;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_numbers);

        //Setting up the Audio Manager to get Audio Service
        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //Create list of words
        final ArrayList<Word> arrayList=new ArrayList<Word>();
        arrayList.add(new Word("One","Ek",R.drawable.number_one,R.raw.one));
        arrayList.add(new Word("Two","Do",R.drawable.number_two,R.raw.two));
        arrayList.add(new Word("Three","Teen",R.drawable.number_three,R.raw.three));
        arrayList.add(new Word("Four","Char",R.drawable.number_four,R.raw.four));
        arrayList.add(new Word("Five","Panch",R.drawable.number_five,R.raw.five));
        arrayList.add(new Word("Six","Chhy",R.drawable.number_six,R.raw.six));
        arrayList.add(new Word("Seven","Saat",R.drawable.number_seven,R.raw.seven));
        arrayList.add(new Word("Eight","Aath",R.drawable.number_eight,R.raw.eight));
        arrayList.add(new Word("Nine","Nou",R.drawable.number_nine,R.raw.nine));
        arrayList.add(new Word("Ten","Daas",R.drawable.number_ten,R.raw.ten));

        //Creating & Setting up WordAdapter to populate list view
        WordAdapter wordAdapter=new WordAdapter(this,arrayList,R.color.numbersActivity);
        ListView listView=findViewById(R.id.listViewNumbers);
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
                    mediaPlayer= MediaPlayer.create(NumbersActivity.this,word.getSoundResourceId());
                    //Start the playing sound
                    mediaPlayer.start();
                    //Release media player when sound is played completely
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
    }

    //On stop method to define what to do when activity is minimized
    @Override
    protected void onStop() {
        super.onStop();
        //Release media player resources
        releaseMediaPlayer();
    }
    /**
     * Clean up the media player by releasing its resources.
     */
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
