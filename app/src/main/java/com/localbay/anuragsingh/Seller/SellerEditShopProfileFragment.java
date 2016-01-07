package com.localbay.anuragsingh.Seller;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
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
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerEditShopProfileFragment extends Fragment {


    public SellerEditShopProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_edit_shop_profile, container, false);

        final EditText user_name_input = (EditText) view.findViewById(R.id.user_name);
        final EditText pincode_input = (EditText) view.findViewById(R.id.shop_pincode);
        final EditText shop_address_input = (EditText) view.findViewById(R.id.shop_address);
        final EditText shop_landmark_input = (EditText) view.findViewById(R.id.landmark);
        final EditText shop_city_input = (EditText) view.findViewById(R.id.city);
        final EditText shop_state_input = (EditText) view.findViewById(R.id.state);
        final CheckBox is_primary_input = (CheckBox) view.findViewById(R.id.isPrimary);

        pincode_input.setFocusable(false);
        shop_address_input.setFocusable(false);
        shop_landmark_input.setFocusable(false);
        shop_city_input.setFocusable(false);
        shop_state_input.setFocusable(false);
        user_name_input.setFocusable(false);

        final Button updateButton = (Button) view.findViewById(R.id.update_button);
        updateButton.setClickable(false);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ParseUser parseUser = ParseUser.getCurrentUser();
        user_name_input.setText(parseUser.get("fullname").toString());

        ParseRelation pr = parseUser.getRelation("addresses");
        ParseQuery pq = pr.getQuery();
        pq.whereEqualTo("isPrimary", true);

        pq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List list, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    pincode_input.setFocusable(true);
                    shop_address_input.setFocusable(true);
                    shop_landmark_input.setFocusable(true);
                    shop_city_input.setFocusable(true);
                    shop_state_input.setFocusable(true);
                    user_name_input.setFocusable(true);
                    updateButton.setClickable(true);

                    pincode_input.setFocusableInTouchMode(true);
                    shop_address_input.setFocusableInTouchMode(true);
                    shop_landmark_input.setFocusableInTouchMode(true);
                    shop_city_input.setFocusableInTouchMode(true);
                    shop_state_input.setFocusableInTouchMode(true);
                    user_name_input.setFocusableInTouchMode(true);


                    if (list.size() != 0) {
                        ParseObject parseObject = (ParseObject) list.get(0);
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

        // Inflate the layout for this fragment
        return view;
    }

}
