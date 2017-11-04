package com.jasonngo.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jasonngo.views.JNImageView;

import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JNImageView imageView = findViewById(R.id.mJNImageView);
        imageView.setImageUrl("http://i.vimeocdn.com/video/662615489_3120x1756.jpg");

    }
}
