package com.example.textdemo;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

public class MainActivity extends AppCompatActivity {

    ImageView img_id;
    EditText edt_id;
    TextView txt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_id = findViewById(R.id.img_id);
        edt_id = findViewById(R.id.edt_id);
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

    public void doText(View view) {
        txt_id.setText(edt_id.getText().toString());
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
        // 1. 비트맵 객체로 FirebaseVisionImage 객체 생성하기
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        // 2. FirebaseVision 인스턴스 얻기
        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        // 3. FirebaseVisionTextRecognizer의 인스턴스 생성
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
        // 4. 이미지 처리 task 생성
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        // 5. task 성공했을 경우
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String s = firebaseVisionText.getText();
                edt_id.setText(s);
            }
        });
        // 6. task 실패했을 경우
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}