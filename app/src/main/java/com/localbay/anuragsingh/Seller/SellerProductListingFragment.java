package com.localbay.anuragsingh.Seller;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.adapters.sellerAdapters.CategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerProductListingFragment extends Fragment {

    ListView productCategories;
    RelativeLayout categoryHolder;
    String[] titles;
    String[] subtitles;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_product_listing, container, false);

        titles = getResources().getStringArray(R.array.category_items);
        subtitles = getResources().getStringArray(R.array.category_item_desc);

        // PRODUCT CATEGORY LAYOUT INIT
        productCategories = (ListView) view.findViewById(R.id.productCategories);
        categoryHolder = (RelativeLayout) view.findViewById(R.id.categoryParent);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this.getActivity().getBaseContext(), titles, subtitles, categoryHolder);
        productCategories.setAdapter(categoryAdapter);

        productCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getAdapter().getItem(position);

                fragment = new SellerCategoryProductsFragment();

                Bundle bundle = new Bundle();
                bundle.putString("SELLER_CATEGORY_PRODUCTS", category);
                fragment.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("CategoryProducts").commit();

            }
        });
        return view;
    }

}
