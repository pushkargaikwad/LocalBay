package com.localbay.anuragsingh.buyer;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.localbay.anuragsingh.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerCategoryProductsFragment extends Fragment {

    String selectedCategory;
    ProgressBar progressBar;

    Boolean isCategory;


    public BuyerCategoryProductsFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCategory = true;

        selectedCategory = getArguments().getString("lb_selectedCategory");
        if (selectedCategory.contains("subCategory")) {
            isCategory = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer_category_products, container, false);
        ((TextView) view.findViewById(R.id.catefrag)).setText(selectedCategory);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ParseQuery getCategoryItems = new ParseQuery("Catalog");
        if (isCategory)
            getCategoryItems.whereEqualTo("category", selectedCategory);
        else
            getCategoryItems.whereEqualTo("subCategory", selectedCategory);

        getCategoryItems.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

            }
        });

        return view;
    }

}
