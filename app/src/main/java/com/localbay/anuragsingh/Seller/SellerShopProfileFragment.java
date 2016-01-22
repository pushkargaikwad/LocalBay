package com.localbay.anuragsingh.Seller;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.localbay.anuragsingh.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerShopProfileFragment extends Fragment {

    private ParseObject SellerAddress;

    public SellerShopProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_shop_profile, container, false);

        TextView user_name = (TextView) view.findViewById(R.id.user_name);
        TextView user_phone = (TextView) view.findViewById(R.id.user_phone);
        TextView user_email = (TextView) view.findViewById(R.id.user_email);
        final TextView shop_address = (TextView) view.findViewById(R.id.shop_address);


        ParseUser parseUser = ParseUser.getCurrentUser();
        user_name.setText(parseUser.get("fullname").toString());
        user_phone.setText(parseUser.get("phoneNumber").toString());
        user_email.setText(parseUser.get("email").toString());


        ParseQuery getAddress = new ParseQuery("Address");
        getAddress.whereEqualTo("resident", parseUser);

        getAddress.whereEqualTo("isPrimary", true);
        getAddress.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List list, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    StringBuilder address_str = new StringBuilder();
                    if (list.size() != 0) {
                        ParseObject pobj = (ParseObject) list.get(0);
                        address_str.append(pobj.get("address").toString());
                        address_str.append(", near ");
                        address_str.append(pobj.get("landmark").toString());
                        address_str.append(", ");
                        address_str.append(pobj.get("city").toString());
                        address_str.append(", ");
                        address_str.append(pobj.get("state").toString());
                        address_str.append(" - ");
                        address_str.append(pobj.get("pincode").toString());

                        shop_address.setText(address_str.toString());
                    }
                }
            }
        });

        return view;
    }

}
