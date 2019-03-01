package com.jaimedediego.flippableviewexample;

import android.os.Bundle;
import android.view.View;

import com.jaimedediego.flippableview.FlippableView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FlippableView flippableView = findViewById(R.id.flippable_view);
        flippableView.avoidClickOnFlip(true);

        flippableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flippableView.flip();
            }
        });
    }
}
