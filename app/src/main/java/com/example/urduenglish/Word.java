package com.example.urduenglish;

public class Word {

    private String urduTranslastion;
    private String englishTranslation;
    private int ImageId;
    private int soundResourceId;

    //Setter
    public Word(String English,String Urdu,int ImageID,int SoundId){
        urduTranslastion=Urdu;
        englishTranslation=English;
        ImageId=ImageID;
        soundResourceId=SoundId;
    }

    //Getter Method

    public String getUrduTranslastion() {
        return urduTranslastion;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public int getImageId() {
        return ImageId;
    }

    public int getSoundResourceId(){
        return soundResourceId;
    }
}
