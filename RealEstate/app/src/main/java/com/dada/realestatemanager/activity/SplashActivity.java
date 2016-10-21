package com.dada.realestatemanager.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dada.realestatemanager.R;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.romainpiel.shimmer.ShimmerTextView;

public class SplashActivity extends AppCompatActivity {
    ShimmerTextView shimmerTitle;
    ShimmerTextView shimmerSubtitle;
    ShimmerButton shimmerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        shimmerTitle = (ShimmerTextView)findViewById(R.id.splash_shimmertextview_title);
        shimmerSubtitle=(ShimmerTextView)findViewById(R.id.splash_shimmertextview_subtitle);
        shimmerButton=(ShimmerButton)findViewById(R.id.splash_shimmerbutton_begin);

        Shimmer shimmer1 = new Shimmer();
        shimmer1.setRepeatCount(5).setStartDelay(500).setDuration(2000);
        shimmer1.start(shimmerTitle);

        Shimmer shimmer2=new Shimmer();
        shimmer2.setRepeatCount(5).setStartDelay(400).setDuration(2000);
        shimmer2.start(shimmerSubtitle);

        Shimmer shimmer3=new Shimmer();
        shimmer3.setRepeatCount(5).setStartDelay(300).setDuration(2000);
        shimmer3.start(shimmerButton);

        shimmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this,SelectBuyerSellerActivity.class);
                startActivity(intent);
            }
        });
    }
}
