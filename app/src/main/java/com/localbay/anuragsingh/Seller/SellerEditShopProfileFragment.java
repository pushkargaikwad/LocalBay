package com.localbay.anuragsingh.Seller;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.localbay.anuragsingh.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerEditShopProfileFragment extends Fragment {

    private Button update_button;
    private ParseObject sellerAddress;


    private EditText user_name_input;
    private EditText pincode_input;
    private EditText shop_address_input;
    private EditText shop_landmark_input;
    private EditText shop_city_input;
    private EditText shop_state_input;
    private CheckBox is_primary_input;
    private ProgressBar progressBar;

    private Fragment fragment;

    public SellerEditShopProfileFragment() {
        // Required empty public constructor
        sellerAddress = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_edit_shop_profile, container, false);

        user_name_input = (EditText) view.findViewById(R.id.user_name);
        pincode_input = (EditText) view.findViewById(R.id.shop_pincode);
        shop_address_input = (EditText) view.findViewById(R.id.shop_address);
        shop_landmark_input = (EditText) view.findViewById(R.id.landmark);
        shop_city_input = (EditText) view.findViewById(R.id.city);
        shop_state_input = (EditText) view.findViewById(R.id.state);
        is_primary_input = (CheckBox) view.findViewById(R.id.isPrimary);

        pincode_input.setFocusable(false);
        shop_address_input.setFocusable(false);
        shop_landmark_input.setFocusable(false);
        shop_city_input.setFocusable(false);
        shop_state_input.setFocusable(false);
        user_name_input.setFocusable(false);

        update_button = (Button) view.findViewById(R.id.update_button);
        update_button.setClickable(false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ParseUser parseUser = ParseUser.getCurrentUser();
        user_name_input.setText(parseUser.get("fullname").toString());

        ParseQuery getAddress = new ParseQuery("Address");
        getAddress.whereEqualTo("resident", parseUser);
        getAddress.whereEqualTo("isPrimary", true);

        getAddress.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    pincode_input.setFocusable(true);
                    shop_address_input.setFocusable(true);
                    shop_landmark_input.setFocusable(true);
                    shop_city_input.setFocusable(true);
                    shop_state_input.setFocusable(true);
                    user_name_input.setFocusable(true);
                    update_button.setClickable(true);

                    pincode_input.setFocusableInTouchMode(true);
                    shop_address_input.setFocusableInTouchMode(true);
                    shop_landmark_input.setFocusableInTouchMode(true);
                    shop_city_input.setFocusableInTouchMode(true);
                    shop_state_input.setFocusableInTouchMode(true);
                    user_name_input.setFocusableInTouchMode(true);


                    if (list.size() != 0) {
                        ParseObject parseObject = (ParseObject) list.get(0);
                        sellerAddress = parseObject;
                        pincode_input.setText(parseObject.get("pincode").toString());
                        shop_address_input.setText(parseObject.get("address").toString());
                        shop_landmark_input.setText(parseObject.get("landmark").toString());
                        shop_city_input.setText(parseObject.get("city").toString());
                        shop_state_input.setText(parseObject.get("state").toString());

                        is_primary_input.setChecked(parseObject.get("isPrimary").toString().equals("true"));
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

        });


        /**
         * UPDATE BUTTON CLICK EVENT HANDLER
         */
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = user_name_input.getText().toString();
                String pincode = pincode_input.getText().toString();
                String shop_address = shop_address_input.getText().toString();
                String shop_landmark = shop_landmark_input.getText().toString();
                String shop_city = shop_city_input.getText().toString();
                String shop_state = shop_state_input.getText().toString();
                Boolean isPrimary = is_primary_input.isChecked();

                // SAVE NEW ADDRESS
                if (sellerAddress == null) {
                    sellerAddress = new ParseObject("Address");
                }

                sellerAddress.put("pincode", pincode);
                sellerAddress.put("address", shop_address);
                sellerAddress.put("landmark", shop_landmark);
                sellerAddress.put("city", shop_city);
                sellerAddress.put("state", shop_state);
                sellerAddress.put("isPrimary", isPrimary);
                sellerAddress.put("resident", ParseUser.getCurrentUser());

                sellerAddress.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null)
                            e.printStackTrace();
                        else {
                            progressBar.setVisibility(View.GONE);
                            fragment = new SellerShopProfileFragment();
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("shopProfile").commit();
                        }
                    }
                });

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
