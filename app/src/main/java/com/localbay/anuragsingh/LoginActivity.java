package com.localbay.anuragsingh;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.localbay.anuragsingh.Seller.SellerDashboardActivity;
import com.localbay.anuragsingh.Seller.SellerRegistrationActivity;
import com.localbay.anuragsingh.buyer.BuyerRegistrationActivity;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private Fragment fragment;
    private EditText sellerPhone;
    private EditText buyerPhone;
    ProgressBar progressBar;
    EditText otp_input;
    String otp;
    private String token = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        fragment = new LoginOptionFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("LoginOptionFragment").commit();

    }

    public void buyerLoginForm(View view) {
        fragment = new BuyerLoginFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("LoginOptionFragment").commit();
    }

    public void sellerLoginForm(View view) {
        fragment = new SellerLoginFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("loginFragment").commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void sellerRequestOTP(View view) {
        sellerPhone = (EditText) findViewById(R.id.sellerPhone);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String phone = sellerPhone.getText().toString();
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", phone);
        params.put("userType", "Seller");

        progressBar.setVisibility(View.VISIBLE);
        ParseCloud.callFunctionInBackground("requestOTPCode", params, new FunctionCallback<JSONObject>() {
            @Override
            public void done(JSONObject response, com.parse.ParseException e) {
                System.out.println(response);
                if (e == null) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("CLOUD RESPONSE", "There were no exceptions!");
                    // SUCCESSFUL - SHOW OTP LAYOUT
                    fragment = new SellerLoginOTPFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("RequestOTP").commit();
                } else {
                    Log.d("CLOUD RESPONSE", "EXCEPTION: " + e);
                    Toast.makeText(LoginActivity.this, "Something went wrong, please try again" + e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void buyerRequestOTP(View view) {
        buyerPhone = (EditText) findViewById(R.id.buyerPhone);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String phone = buyerPhone.getText().toString();
        HashMap<String, Object> params = new HashMap<>();
        params.put("phoneNumber", phone);
        params.put("userType", "Buyer");

        progressBar.setVisibility(View.VISIBLE);
        ParseCloud.callFunctionInBackground("requestOTPCode", params, new FunctionCallback<JSONObject>() {

            @Override
            public void done(JSONObject response, ParseException e) {
                if (e == null) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("CLOUD RESPONSE", "There were no exceptions!");
                    // SUCCESSFUL - SHOW OTP LAYOUT
                    fragment = new BuyerLoginOTPFragment();
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("RequestOTP").commit();
                } else {
                    Log.d("CLOUD RESPONSE", "EXCEPTION: " + e);
                    Toast.makeText(LoginActivity.this, "Something went wrong, please try again" + e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void sellerRegisterForm(View view) {
        Intent intent = new Intent(LoginActivity.this, SellerRegistrationActivity.class);
        startActivity(intent);
    }

    public void reset_otpUI() {
        otp_input.setText("");
        otp_input.setHint("Enter your One Time Password here");
    }

    public void sellerLogin(View view) {
        otp_input = (EditText) findViewById(R.id.sellerOTP);
        otp = otp_input.getText().toString();
        if (otp.length() != 4) {
            Toast.makeText(LoginActivity.this, "You must enter the 4 digit code provided to your phone number.", Toast.LENGTH_LONG).show();
            reset_otpUI();
        } else {
            Log.d("EVENT HANDLERS", "Submit OTP");

            otp = otp_input.getText().toString();

            HashMap<String, Object> params = new HashMap<>();
            params.put("phoneNumber", sellerPhone.getText().toString());
            params.put("otpCode", otp);

            progressBar.setVisibility(View.VISIBLE);
            ParseCloud.callFunctionInBackground("logIn", params, new FunctionCallback<String>() {
                @Override
                public void done(String response, ParseException e) {
                    progressBar.setVisibility(View.GONE);

                    if (e == null) {
                        Log.d("CLOUD RESPONSE", response);
                        token = response;
                        ParseUser.becomeInBackground(token, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                    Log.d("CLOUD RESPONSE", "There were no exceptions");

                                    Intent intent = new Intent(LoginActivity.this, SellerDashboardActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Log.d("Cloud Response", "Exception: " + e);
                                    Toast.makeText(getApplicationContext(),
                                            "Something went wrong.  Please try again." + e,
                                            Toast.LENGTH_LONG).show();
                                    fragment = new SellerLoginFragment();
                                    FragmentManager fm = getFragmentManager();
                                    fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("LoginFormOnError").commit();
                                }
                            }
                        });
                    } else {
                        Log.d("Cloud Response", "Exception: " + response + e);
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong.  Please try again.",
                                Toast.LENGTH_LONG).show();
                        fragment = new SellerLoginFragment();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("LoginFormOnError").commit();
                    }
                }
            });

        }
    }


    public void buyerLogin(View view) {
        otp_input = (EditText) findViewById(R.id.buyerOTP);
        otp = otp_input.getText().toString();
        if (otp.length() != 4) {
            Toast.makeText(LoginActivity.this, "You must enter the 4 digit code provided to your phone number.", Toast.LENGTH_LONG).show();
            reset_otpUI();
        } else {

            otp = otp_input.getText().toString();

            HashMap<String, Object> params = new HashMap<>();
            params.put("phoneNumber", sellerPhone.getText().toString());
            params.put("otpCode", otp);

            progressBar.setVisibility(View.VISIBLE);
            ParseCloud.callFunctionInBackground("logIn", params, new FunctionCallback<String>() {
                @Override
                public void done(String response, ParseException e) {
                    progressBar.setVisibility(View.GONE);

                    if (e == null) {
                        Log.d("CLOUD RESPONSE", response);
                        token = response;
                        ParseUser.becomeInBackground(token, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                    Log.d("CLOUD RESPONSE", "There were no exceptions");

                                    Intent intent = new Intent(LoginActivity.this, SellerDashboardActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Log.d("Cloud Response", "Exception: " + e);
                                    Toast.makeText(getApplicationContext(),
                                            "Something went wrong.  Please try again." + e,
                                            Toast.LENGTH_LONG).show();
                                    fragment = new SellerLoginFragment();
                                    FragmentManager fm = getFragmentManager();
                                    fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("LoginFormOnError").commit();
                                }
                            }
                        });
                    } else {
                        Log.d("Cloud Response", "Exception: " + response + e);
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong.  Please try again.",
                                Toast.LENGTH_LONG).show();
                        fragment = new SellerLoginFragment();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.loginFormFrame, fragment).addToBackStack("LoginFormOnError").commit();
                    }
                }
            });

        }
    }

    public void buyerRegisterForm(View view) {
        Intent intent = new Intent(LoginActivity.this, BuyerRegistrationActivity.class);
        startActivity(intent);
    }
}
