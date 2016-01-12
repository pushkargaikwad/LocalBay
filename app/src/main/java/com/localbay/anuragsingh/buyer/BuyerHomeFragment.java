package com.localbay.anuragsingh.buyer;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.localbay.anuragsingh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerHomeFragment extends Fragment {


    public BuyerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer_home, container, false);
    }

}
