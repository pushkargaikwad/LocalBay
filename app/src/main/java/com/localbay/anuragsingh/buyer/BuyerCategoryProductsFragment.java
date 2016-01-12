package com.localbay.anuragsingh.buyer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.localbay.anuragsingh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerCategoryProductsFragment extends Fragment {


    public BuyerCategoryProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer_category_products, container, false);
    }

}
