package com.example.textdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView img_id;
    TextView txt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_id = findViewById(R.id.img_id);
        txt_id = findViewById(R.id.txt_id);

        // 카메라에 대해 앱 수준 권한이 부여되었는지 확인
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            // 권한 부여
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }

    public void doProcess(View view) {
        // 카메라 실행 => 인텐트 생성
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        // bundle로 이미지를 추출
        Bitmap bitmap = (Bitmap) bundle.get("data");
        // imageView 에  이미지 설정
        img_id.setImageBitmap(bitmap);
        // 이미지 처리

    }
}