package com.localbay.anuragsingh;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.localbay.anuragsingh.Seller.SellerDashboardActivity;
import com.localbay.anuragsingh.buyer.BuyerDashboardActivity;
import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    ParseUser parseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (ParseUser.getCurrentUser() != null) {
                    parseUser = ParseUser.getCurrentUser();
                    System.out.println(parseUser);
                    String userType = parseUser.getString("userType");
                    if(userType.equals("Seller")){
                        Intent intent = new Intent(SplashActivity.this, SellerDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else if(userType.equals("Buyer")){
                        Intent intent = new Intent(SplashActivity.this, BuyerDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);

    }
}
