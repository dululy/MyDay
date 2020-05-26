package com.example.myday;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myday.applock.App;
import com.example.myday.applock.HomePage;
import com.google.firebase.iid.FirebaseInstanceId;


public class MainSetting extends AppCompatActivity {

    private Button setting_notification;
    private Button setting_password;
    private ImageButton back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);

        setting_notification = findViewById(R.id.setting_notification);
        setting_password = findViewById(R.id.setting_password);


        //뒤로가기 버튼 동작
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        setting_notification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), NotiSettingsActivity.class);
            }
        });

        setting_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

    }
}
//public class MainSetting extends PreferenceActivity implements Preference.OnPreferenceClickListener {
//
//    Preference pPassword;
//    Preference pNotification;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.setting_preference);
//
//        //암호설정메뉴, 알림설정메뉴 데려오기
//        pNotification = findPreference("notification_setting");
//        pPassword= findPreference("password_setting");
//
//
//
//        pNotification.setOnPreferenceClickListener(this);
//        pPassword.setOnPreferenceClickListener(this);
//    }
//
////누르면 페이지 넘어가게하는 부분
//    public boolean onPreferenceClick(Preference preference) {
//
//        if (preference.hasKey()) {
//
//            String preferenceKey = preference.getKey();
//
//            if ("notification_setting".equals(preferenceKey)) {
////                Toast.makeText(this, "알림설정", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(getApplicationContext(), NotiSettingsActivity.class);
////                startActivity(intent);
//            } else if ("password_setting".equals(preferenceKey)) {
////                Toast.makeText(this, "암호설정", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), HomePage.class);
//                startActivity(intent);
//            }
//        }
//
//        return false;
//
//    }
//
//}


