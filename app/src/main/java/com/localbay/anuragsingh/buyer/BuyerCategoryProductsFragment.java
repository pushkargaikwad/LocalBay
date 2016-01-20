package com.localbay.anuragsingh.buyer;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.adapters.buyerAdapters.BuyerProductListAdapter;
import com.localbay.anuragsingh.models.CatalogModel;
import com.localbay.anuragsingh.models.StockProductModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerCategoryProductsFragment extends Fragment {

    String selectedCategory;
    String selectedType;
    ProgressBar progressBar;

    Boolean isCategory;
    ListView categoryProducts;
    BuyerProductListAdapter bpla;
    List<CatalogModel> catalogModels;

    List<StockProductModel> stockProducts;
    Fragment buyerProductFragment;


    public BuyerCategoryProductsFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCategory = true;

        selectedCategory = getArguments().getString("lb_selectedCategory");
        selectedType = getArguments().getString("lb_selectedType");

        if (selectedType.contains("subCategory")) {
            isCategory = false;
        }

        catalogModels = new ArrayList<CatalogModel>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer_category_products, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        /**
         * INITIALISING LIST VIEW
         */
        categoryProducts = (ListView) view.findViewById(R.id.categoryProducts);
        bpla = new BuyerProductListAdapter(getActivity().getBaseContext(), R.id.categoryProductsParent, catalogModels);
        categoryProducts.setAdapter(bpla);

        /**
         * GETTING PRODUCT LISTS
         */

        ParseQuery getCategoryItems = new ParseQuery("Catalog");
        System.out.println(isCategory);
        if (isCategory)
            getCategoryItems.whereEqualTo("category", selectedCategory);
        else
            getCategoryItems.whereEqualTo("subCategory", selectedCategory);

        getCategoryItems.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                progressBar.setVisibility(View.GONE);

                if (e == null) {

                    for (ParseObject item : list) {

                        final CatalogModel currentProduct = new CatalogModel(
                                item.get("manufacturer").toString(),
                                item.get("title").toString(),
                                item.get("category").toString(),
                                item.get("subCategory").toString(),
                                item.get("variant").toString(),
                                item.get("imageUrl").toString()
                        );

                        catalogModels.add(currentProduct);
                        bpla.notifyDataSetChanged();
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });

        /**
         * GET ONCLICK LISTENER FOR LISTVIEW
         */
        categoryProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CatalogModel selectedCatalogModel = (CatalogModel) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("LB_SELECTED_BUYER_PRODUCT", selectedCatalogModel);

                buyerProductFragment = new BuyerProductFragment();
                buyerProductFragment.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.contentFrame, buyerProductFragment).addToBackStack("ProductFragment").commit();
            }
        });

        return view;
    }

}
