package com.example.urduenglish;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Color_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Color_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Color_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Color_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Color_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Color_Fragment newInstance(String param1, String param2) {
        Color_Fragment fragment = new Color_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onStop() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_color_, container, false);

        //Setting up the Audio Manager to get Audio Service
        audioManager= (AudioManager)
                getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> arrayList=new ArrayList<Word>();
        arrayList.add(new Word("Black","Kala",R.drawable.color_black,R.raw.black));
        arrayList.add(new Word("Brown","Badami",R.drawable.color_brown,R.raw.brown));
        arrayList.add(new Word("Gray","Surmai",R.drawable.color_gray,R.raw.gray));
        arrayList.add(new Word("Green","Hara",R.drawable.color_green,R.raw.green));
        arrayList.add(new Word("Red","Lal",R.drawable.color_red,R.raw.red));
        arrayList.add(new Word("White","Suffed",R.drawable.color_white,R.raw.white));
        arrayList.add(new Word("Yellow","peela",R.drawable.color_mustard_yellow,R.raw.yellow));

        WordAdapter wordAdapter=new WordAdapter(getActivity(),arrayList,R.color.colorsActivity);
        ListView listView=rootView.findViewById(R.id.list);
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
                    mediaPlayer= MediaPlayer.create(getActivity(),word.getSoundResourceId());
                    //Start the playing sound
                    mediaPlayer.start();
                    //Release media player when sound is played completely
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        releaseMediaPlayer();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
