package com.example.urduenglish;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_family);

        //Setting up the Audio Manager to get Audio Service
        audioManager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> arrayList=new ArrayList<Word>();
        arrayList.add(new Word("Father","Aabu",R.drawable.family_father,R.raw.father));
        arrayList.add(new Word("Mother","Aami",R.drawable.family_mother,R.raw.mother));
        arrayList.add(new Word("Son","Beeta",R.drawable.family_son,R.raw.son));
        arrayList.add(new Word("Daughter","Beeti",R.drawable.family_daughter,R.raw.daughter));
        arrayList.add(new Word("Older Brother","Bara Bahi",R.drawable.family_older_brother,R.raw.older_brother));
        arrayList.add(new Word("Younger Brother","Chota Bahi",R.drawable.family_younger_brother,R.raw.younger_brother));
        arrayList.add(new Word("Older Sister","Bari Bhen",R.drawable.family_older_sister,R.raw.older_sister));
        arrayList.add(new Word("Younger Sister","Choti Bhen",R.drawable.family_younger_sister,R.raw.younger_sister));
        arrayList.add(new Word("Grandmother","Dadi",R.drawable.family_grandmother,R.raw.grandmother));
        arrayList.add(new Word("Grandfather","Dada",R.drawable.family_grandfather,R.raw.grandfather));

        WordAdapter wordAdapter=new WordAdapter(this,arrayList,R.color.familyActivity);
        ListView listView=findViewById(R.id.listViewFamily);
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
                    //Assigning the resources to media player
                    mediaPlayer= MediaPlayer.create(FamilyActivity.this,word.getSoundResourceId());
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
        //This release media player resources since the activity is stopped
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

            //Abandon Audio Focus so that other apps can use it
            audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
