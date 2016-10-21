package com.dada.realestatemanager.activity;

import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dada.realestatemanager.R;

public class SelectBuyerSellerActivity extends AppCompatActivity {
    TextView buyerButton, sellerButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buyer_seller);
        MultiDex.install(this);


        buyerButton = (TextView) findViewById(R.id.selectbuyerseller_textview_buyerbutton);
        sellerButton = (TextView) findViewById(R.id.selectbuyerseller_textview_sellerbutton);

        sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SelectBuyerSellerActivity.this, LoginSellerActivity.class);
                startActivity(intent);

            }
        });

        buyerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SelectBuyerSellerActivity.this, LoginBuyerActivity.class);
                startActivity(intent);

            }
        });

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectBuyerSellerActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
