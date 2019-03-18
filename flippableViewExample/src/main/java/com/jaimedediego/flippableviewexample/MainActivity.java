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

        final FlippableView instantFlippableView = findViewById(R.id.instant_flippable_view);
        instantFlippableView.avoidClickOnFlip(true);

        flippableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flippableView.flip();
            }
        });

        instantFlippableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instantFlippableView.isFrontVisible()){
                    instantFlippableView.setBackFaceVisible();
                } else {
                    instantFlippableView.setFrontFaceVisible();
                }
            }
        });
    }
}
