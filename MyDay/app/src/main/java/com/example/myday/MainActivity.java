package com.example.myday;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Button;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


// ★★★★★★메인 캘린더랑 플로팅버튼(메뉴이동부분)★★★★★★
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // 플로팅버튼1, 설정버튼, 일기작성버튼/플로팅버튼 열리고 닫히는거, 플로팅버튼 상태
    FloatingActionButton fab, fab2, fab3;
    Animation fab_open, fab_close;
    Boolean openFlag = false;

    //firebase 연결
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    //날짜 형식
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //유저 등록
        String tokenID = FirebaseInstanceId.getInstance().getToken();
        Users users = new Users(tokenID);
        databaseReference.child("Users").child(tokenID).setValue(users);

        //fab = 메뉴버튼, fab2 = 설정,메뉴버튼, fab3 = 작성버튼
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);

        fab2.startAnimation(fab_close);
        fab3.startAnimation(fab_close);
        fab2.setClickable(false);
        fab3.setClickable(false);

        fab.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
    }


    //플로팅버튼 닫혔다 열렸다하는 애니메이션
    public void anim() {
        if (openFlag) {
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab2.setClickable(false);
            fab3.setClickable(false);
            openFlag = false;
        } else {
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab2.setClickable(true);
            fab3.setClickable(true);
            openFlag = true;
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab:
                anim();
                break;
            case R.id.fab2:
                anim();
                //텍스트 잠시 띄우는건 안헷갈리려고 잠시 넣었습니다아
//                Toast.makeText(this, "설정 메뉴로 이동", Toast.LENGTH_SHORT).show();
                Intent settings = new Intent(getApplicationContext(), MainSetting.class);
                startActivity(settings);
                break;
            case R.id.fab3:
                anim();
                Intent select_emoticon = new Intent(getApplicationContext(), selectEmoticonActivity.class);
                startActivity(select_emoticon);
//                Toast.makeText(this, "일기 작성", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //뒤로가기시 종료
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
