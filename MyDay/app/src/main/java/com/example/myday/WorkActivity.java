package com.example.myday;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.codec.net.URLCodec;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkActivity extends AppCompatActivity {

    private TextView text_year,text_month,text_day;
    private EditText text_content;
    private TextView text_weather;
    private ImageButton back_btn;
    private ImageButton file_btn;
    private Button save_btn;
    private ImageView file_img;
    private VideoView file_video;

    public String content;
    private String weekDay,day,month,year,emoticon,weather;
    private String encrypt_day,encrypt_month,encrypt_year,encrypt_content,encrypt_emoticon,encrypt_weather,fileURI;
    private static final String KEY = "aes256-test-key!!";
    private int num;

    private Uri filePath;

    //firebase 연결
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        //emoticon 받아오기
        Intent intent = getIntent(); /*데이터 수신*/
        emoticon = intent.getExtras().getString("emoticon");

        file_img = findViewById(R.id.file_img);

        //뒤로가기 버튼 동작
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //날짜 받아오기
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        weekDay = weekdayFormat.format(currentTime);
        year = yearFormat.format(currentTime);
        month = monthFormat.format(currentTime);
        day = dayFormat.format(currentTime);

        text_year = findViewById(R.id.year);
        text_month = findViewById(R.id.month);
        text_day = findViewById(R.id.day);

        text_year.setText(year);
        text_month.setText(month+"월");
        text_day.setText(day);

        //파일버튼 동작
        file_btn = findViewById(R.id.fileBtn);
        file_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(WorkActivity.this, file_btn);
                popup.getMenuInflater().inflate(R.menu.file_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.gallery:
                                checkSelfPermission();
                                Intent intent = new Intent(); //기기 기본 갤러리 접근
                                intent.setType(MediaStore.Images.Media.CONTENT_TYPE); //구글 갤러리 접근
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 101);
                                break;

                            case R.id.camera:
                                Intent takepictureintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takepictureintent, 0);
                                file_img.setImageURI(takepictureintent.getData());
                                break;

                            case R.id.movie:
                                checkSelfPermission();
                                Intent movieintent = new Intent(); //기기 기본 갤러리 접근
                                movieintent.setType(MediaStore.Images.Media.CONTENT_TYPE); //구글 갤러리 접근
                                movieintent.setType("video/*");
                                movieintent.setAction(Intent.ACTION_GET_CONTENT);
                                movieintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivityForResult(movieintent, 102);
                                break;

                            case R.id.draw:
                                Toast.makeText(getApplication(), "그리기모드", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;

                    }
                });
                popup.show();
            }
        });

//        저장버튼 구현
        save_btn =  findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                text_weather = findViewById(R.id.weather);
                weather = text_weather.getText().toString();

                text_content = (EditText)findViewById(R.id.work_view);
                content = text_content.getText().toString();

                if(filePath!=null){
                    fileURI = filePath.toString();
                }

                //암호화
                try {
                    AES256Util AES256Util  = new AES256Util(KEY);
                    encrypt_day = AES256Util.aesEncode(day);
                    encrypt_month = AES256Util.aesEncode(month);
                    encrypt_year = AES256Util.aesEncode(year);
                    encrypt_content = AES256Util.aesEncode(content);
                    encrypt_weather = AES256Util.aesEncode(weather);
                    encrypt_emoticon = AES256Util.aesEncode(emoticon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //db에 저장
                String tokenID = FirebaseInstanceId.getInstance().getToken();
                // Firebase Database 에 등록된 Key 값

                if (!TextUtils.isEmpty(tokenID)) {
                    UserDiaries userDiaries = new UserDiaries(encrypt_day,encrypt_month,encrypt_year,encrypt_content,encrypt_weather,encrypt_emoticon);
                    databaseReference.child("User-Diaries").child(tokenID).push().setValue(userDiaries);
                }
                //storage에 저장
                uploadFile();

                finish();
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("WorkActivity","권한 허용 : " + permissions[i]); }
            }
        }
    }

    public void checkSelfPermission() {
        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }
        else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    //이미지 첨부 기능 함수 ************다시 수정 요망
    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 101 && resultCode == RESULT_OK){
            try{
                filePath = data.getData();
                InputStream is = getContentResolver().openInputStream(filePath);
                Bitmap bm = BitmapFactory.decodeStream(is);
                is.close();
                file_img.setImageBitmap(bm);
            }
            catch (Exception e){ e.printStackTrace();
            }
        } else if(requestCode == 101 && resultCode == RESULT_CANCELED){
            Toast.makeText(this,"취소", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //사용자별 token값 받아오기
            String tokenID = FirebaseInstanceId.getInstance().getToken();

            //날짜로 파일명 생성
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://myday-b6bcc.appspot.com/").child(tokenID).child(filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    //뒤로가기시
    @Override
    public void onBackPressed() {
        finish();
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
    }

}






