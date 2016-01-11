package com.localbay.anuragsingh.buyer;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.localbay.anuragsingh.R;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerAccountFragment extends Fragment {

    ImageView edit_userDetails;
    LinearLayout buyer_account_settings;
    Fragment fragment;
    FragmentManager fm;
    ParseUser buyer;

    public BuyerAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer_account, container, false);
        buyer = ParseUser.getCurrentUser();

        TextView user_name = (TextView) view.findViewById(R.id.user_name);
        user_name.setText(buyer.getString("fullname"));

        TextView phone_number = (TextView) view.findViewById(R.id.user_phone);
        phone_number.setText(buyer.getString("phoneNumber"));

        TextView email = (TextView) view.findViewById(R.id.user_email);
        email.setText(buyer.getString("email"));

        fm = getFragmentManager();
        /**
         * EDIT USER DETAILS
         * ON CLICK LISTENERS
         */
        edit_userDetails = (ImageView) view.findViewById(R.id.edit_user_details);
        edit_userDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new BuyerEditProfileFragment();
                fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("EditBuyerProfile").commit();
            }
        });

        buyer_account_settings = (LinearLayout) view.findViewById(R.id.buyer_account_settings);
        buyer_account_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new BuyerEditProfileFragment();
                fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("EditBuyerProfile").commit();
            }
        });


        return view;
    }

}
