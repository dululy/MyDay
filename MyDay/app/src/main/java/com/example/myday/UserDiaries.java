package com.example.myday;

public class UserDiaries {
    public String day;
    public String month;
    public String year;
    public String content;
    public String weather;
    public String emoticon;
    public String diary_imgs;

    public UserDiaries() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserDiaries(String day,String month,String year,String content,String weather,String emoticon,String diary_imgs) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.content = content;
        this.weather = weather;
        this.emoticon = emoticon;
        this.diary_imgs = diary_imgs;
    }
}
