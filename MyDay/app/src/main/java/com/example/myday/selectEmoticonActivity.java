package com.example.myday;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class selectEmoticonActivity extends MainActivity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_emoticon);
    }

    public void onClick(View view) {
        Intent write_diary = new Intent(getApplicationContext(), WorkActivity.class);

        switch (view.getId()){
            case R.id.button_smile:
                // smile버튼 눌렀을때
                write_diary.putExtra("emoticon","smile");
                startActivity(write_diary);
                break;

            case R.id.button_laughing:
                // laughing버튼 눌렀을때
                write_diary.putExtra("emoticon","laughing");
                startActivity(write_diary);
                break;

            case R.id.button_disappointment:
                // disappointment버튼 눌렀을때
                write_diary.putExtra("emoticon","disappointment");
                startActivity(write_diary);
                break;

            case R.id.button_cold:
                // cold버튼 눌렀을때
                write_diary.putExtra("emoticon","cold");
                startActivity(write_diary);
                break;

            case R.id.button_crying:
                // crying버튼 눌렀을때
                write_diary.putExtra("emoticon","crying");
                startActivity(write_diary);
                break;

            case R.id.button_angry:
                // angry버튼 눌렀을때
                write_diary.putExtra("emoticon","angry");
                startActivity(write_diary);
                break;
        }
    }

    //뒤로가기시 종료
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}