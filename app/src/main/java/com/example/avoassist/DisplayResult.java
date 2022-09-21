package com.example.avoassist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayResult extends AppCompatActivity {

    private MainActivity main = new MainActivity();
    private TextView resultText;
    private Button sharebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);
        resultText = findViewById(R.id.resultText);
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();
        String url = bundle.getString("Update");
        resultText.setText(url);
        resultText.setTextColor(getResources().getColor(R.color.black, null));
        sharebutton = findViewById(R.id.shareButton);
        try{
            sharebutton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent myIntent=new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    String shareBody=url;
                    String shareSub=url;
                    myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                    myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                    startActivity(Intent.createChooser(myIntent,"Share Using"));
                }
            });}catch(NullPointerException ne){
            Log.d("TAG1","nullpointer alert...");
        }
        setBackOperation();
    }

    public void setBackOperation() {
        ImageView backBtn = findViewById(R.id.back_image1);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayResult.this.finish();
            }
        });
    }
}





