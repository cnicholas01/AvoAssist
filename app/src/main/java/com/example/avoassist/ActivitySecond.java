package com.example.avoassist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivitySecond extends AppCompatActivity {

    private static final float SCAN_FRAME_SIZE = 300;
    private ImageView flashBtn;
    private RemoteView remoteView;
    private TextView quote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        flashBtn = findViewById(R.id.flash_btn);
        quote=findViewById(R.id.textView);
        int unicode=0x1F951;   //avocado emoji unicode
        String avocado=getEmoji(unicode);
        quote.setText(avocado+"'s scanning in progress...");
        //1.get screen density to calculate viewfinder's rect
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float density = dm.density;
        //2.get screen size
        int mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        int mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);
        //3.calculate viewfinder's rect,it's in the middle of the layout
        //set scanning area(Optional, rect can be null,If not configure,default is in the center of layout)
        Rect rect = new Rect();
        rect.left = mScreenWidth / 2 - scanFrameSize / 2;
        rect.right = mScreenWidth / 2 + scanFrameSize / 2;
        rect.top = mScreenHeight / 2 - scanFrameSize / 2;
        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2;

        //initialize RemoteView instance, and set calling back for scanning result
        remoteView = new RemoteView.Builder().setContext(this).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build();
        remoteView.onCreate(savedInstanceState);
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {

                if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                    Intent intent = new Intent();
                    intent.putExtra("SCAN_RESULT", String.valueOf(result[0].getOriginalValue()));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

        });

        // Add the defined RemoteView to the page layout.
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        FrameLayout frameLayout = findViewById(R.id.square_box);
        frameLayout.addView(remoteView, params);
        setFlashOperation();
        setBackOperation();

    }

    public String getEmoji(int uni){
        return new String(Character.toChars(uni));
    }

    @Override
    protected void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        remoteView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        remoteView.onStop();
    }

    private void setFlashOperation() {

        boolean hasFlash = this.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            flashBtn.setVisibility(View.INVISIBLE);
        } else {
            flashBtn.setVisibility(View.VISIBLE);
        }
        flashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (remoteView.getLightStatus()) {
                    remoteView.switchLight();
                    flashBtn.setImageResource(R.drawable.scankit_flashlight_off);
                } else {
                    remoteView.switchLight();
                    flashBtn.setImageResource(R.drawable.scankit_flashlight_on);
                }
            }
        });

    }

    private void setBackOperation() {
        ImageView backBtn = findViewById(R.id.back_image);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySecond.this.finish();
            }
        });
    }


}