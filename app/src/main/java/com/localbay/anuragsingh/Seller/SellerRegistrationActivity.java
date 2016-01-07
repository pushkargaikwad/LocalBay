package com.localbay.anuragsingh.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.localbay.anuragsingh.R;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SellerRegistrationActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private ProgressBar progressBar_otp;
    private EditText otp_input;

    EditText sellerNameInput;
    EditText sellerEmailInput;
    EditText sellerPhoneInput;

    private String sellerName;
    private String sellerEmail;
    private String sellerPhone;

    private String otp = null;
    private String token = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        getSupportActionBar().setTitle("Localbay Seller Registration");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void registerSeller(View view) {
        sellerNameInput = (EditText) findViewById(R.id.sellerName);
        sellerName = sellerNameInput.getText().toString();

        sellerEmailInput = (EditText) findViewById(R.id.sellerEmail);
        sellerEmail = sellerEmailInput.getText().toString();

        sellerPhoneInput = (EditText) findViewById(R.id.sellerPhone);
        sellerPhone = sellerPhoneInput.getText().toString();

        StringBuilder errorMessage = new StringBuilder("");
        int flag_error = 0;
        if (sellerName.length() == 0) {
            flag_error = 1;
            errorMessage.append("Invalid Name");
        }
        if (!isEmailValid(sellerEmail)) {
            flag_error = 1;
            if (errorMessage == null || errorMessage.toString().equals("")) {
                errorMessage.append("Invalid Email");
            } else errorMessage.append(" and email");
        }
        if (sellerPhone.length() != 10) {
            flag_error = 1;
            if (errorMessage == null || errorMessage.toString().equals("")) {
                errorMessage.append("Invalid Phone");
            } else errorMessage.append(" and phone");
        }


        if (flag_error == 1) {
            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_LONG).show();
        } else {

            // SUBMIT FOR RECEIVING OTP
            findViewById(R.id.registerSeller).setClickable(false);
            HashMap<String, Object> params = new HashMap<>();
            params.put("userName", sellerName);
            params.put("emailAddress", sellerEmail);
            params.put("phoneNumber", sellerPhone);
            params.put("userType", "Seller");

            progressBar.setVisibility(View.VISIBLE);
            ParseCloud.callFunctionInBackground("sendCode", params, new FunctionCallback<JSONObject>() {
                @Override
                public void done(JSONObject object, ParseException e) {
                    if (e == null) {
                        Log.d("CLOUD RESPONSE", "There were no exceptions!");
                        // SUCCESSFUL - SHOW OTP LAYOUT
                        setContentView(R.layout.verify_otp);
                        otp_input = (EditText) findViewById(R.id.otpInput);
                        progressBar_otp = (ProgressBar) findViewById(R.id.progressBar_otp);
                    } else {
                        Log.d("CLOUD RESPONSE", "EXCEPTION: " + e);
                        Toast.makeText(SellerRegistrationActivity.this, "Something went wrong, please try again" + e, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public void reset_otpUI() {
        otp_input.setText("");
        otp_input.setHint("ENTER OTP");
    }

    public void submitOtp(View view) {
        otp = otp_input.getText().toString();
        if (otp.length() != 4) {
            Toast.makeText(SellerRegistrationActivity.this, "You must enter the 4 digit code provided to your phone number.", Toast.LENGTH_LONG).show();
            reset_otpUI();
        } else {
            Log.d("EVENT HANDLERS", "Submit OTP");

            HashMap<String, Object> params = new HashMap<>();
            params.put("phoneNumber", sellerPhone);
            params.put("otpCode", otp);

            ParseCloud.callFunctionInBackground("logIn", params, new FunctionCallback<String>() {
                @Override
                public void done(String response, ParseException e) {
                    progressBar_otp.setVisibility(View.GONE);

                    if (e == null) {
                        Log.d("CLOUD RESPONSE", response);
                        token = response;
                        ParseUser.becomeInBackground(token, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                    Log.d("CLOUD RESPONSE", "There were no exceptions");

                                    Intent intent = new Intent(SellerRegistrationActivity.this, SellerDashboardActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Log.d("Cloud Response", "Exception: " + e);
                                    Toast.makeText(getApplicationContext(),
                                            "Something went wrong.  Please try again." + e,
                                            Toast.LENGTH_LONG).show();
                                    setContentView(R.layout.activity_seller_registration);
                                }
                            }
                        });
                    } else {
                        Log.d("Cloud Response", "Exception: " + response + e);
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong.  Please try again.",
                                Toast.LENGTH_LONG).show();
                        setContentView(R.layout.activity_seller_registration);
                    }

                }
            });
        }
    }
}
