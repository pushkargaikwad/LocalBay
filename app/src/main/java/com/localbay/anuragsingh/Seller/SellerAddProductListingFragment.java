package com.localbay.anuragsingh.Seller;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.adapters.sellerAdapters.ProductListAdapter;
import com.localbay.anuragsingh.adapters.sellerAdapters.ProductSearchAdapter;
import com.localbay.anuragsingh.models.CatalogModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerAddProductListingFragment extends Fragment {

    AutoCompleteTextView searchProducts;
    ProgressBar progressBar;
    ListView productsHolder;
    View mView;


    Fragment fragment;

    CatalogModel iphone = new CatalogModel("Apple", "iPhone", "Electronics", "Mobiles", "Space Grey", "http://img6a.flixcart.com/image/mobile/f/2/j/apple-iphone-6-400x400-imaeymdqs5gm5xkz.jpeg");
    CatalogModel macbook = new CatalogModel("Apple", "Macbook", "Electronics", "Laptops", "Pro 128GB", "http://img6a.flixcart.com/image/computer/z/s/2/apple-macbook-pro-2015-ultrabook-125x125-imae6h59zqvdw8ph.jpeg");
    CatalogModel iPod = new CatalogModel("Apple", "iPod", "Electronics", "Music", "Shuffle", "http://img6a.flixcart.com/image/audioplayer/x/d/2/apple-ipod-touch-touch-6th-generation-400x400-imaeagh2ynqwfzmp.jpeg");
    CatalogModel nexus = new CatalogModel("Google", "Nexus", "Electronics", "Mobiles", "6", "http://img5a.flixcart.com/image/mobile/k/y/3/motorola-nexus-6-400x400-imaefmudvfwzs2fx.jpeg");
    CatalogModel zenphone = new CatalogModel("Asus", "Zenphone", "Electronics", "Mobiles", "Selfie", "http://img6a.flixcart.com/image/mobile/x/d/y/asus-zenfone-selfie-zd551kl-zd551kl-6j210ww-125x125-imaebpvwhwajrmrt.jpeg");
    CatalogModel motox = new CatalogModel("Motorola", "Moto", "Electronics", "Mobiles", "X Play", "http://img5a.flixcart.com/image/mobile/9/k/q/motorola-moto-x-play-xt1562-125x125-imaeb7fdsmje8bt5.jpeg");
    CatalogModel nexus6 = new CatalogModel("Huawei", "Nexus", "Electronics", "Mobiles", "6P", "http://img6a.flixcart.com/image/mobile/9/w/f/huawei-nexus-6p-nin-a2-400x400-imaebyyyhxgugwna.jpeg");

    ArrayList<CatalogModel> catalogModels = new ArrayList<CatalogModel>();
    ParseQuery<ParseObject> pq;

    public SellerAddProductListingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pq = ParseQuery.getQuery("Catalog");
        pq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject pco : list) {
                        CatalogModel pm = new CatalogModel(
                                pco.get("manufacturer").toString(),
                                pco.get("title").toString(),
                                pco.get("category").toString(),
                                pco.get("subCategory").toString(),
                                pco.get("variant").toString(),
                                pco.get("imageUrl").toString()
                        );

                        catalogModels.add(pm);
                        System.out.println("YO" + catalogModels.size());
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_seller_add_product_listing, container, false);

        searchProducts = (AutoCompleteTextView) mView.findViewById(R.id.productSearch);
        ProductSearchAdapter psa = new ProductSearchAdapter(this.getActivity().getBaseContext(), catalogModels);
        searchProducts.setThreshold(1);
        searchProducts.setAdapter(psa);

        searchProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CatalogModel selected = (CatalogModel) parent.getAdapter().getItem(position);
                searchProducts.setText(selected.getTitle());

                getProducts(mView, selected);
            }
        });

        return mView;
    }

    public void getProducts(View view, final CatalogModel catalogModel) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        productsHolder = (ListView) view.findViewById(R.id.products_holder);

        Log.d("PRODUCT count", String.valueOf(catalogModels.size()));

        ParseQuery<ParseObject> pq = new ParseQuery<ParseObject>("Catalog");
        pq.whereContains("title", catalogModel.getTitle());
        pq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                progressBar.setVisibility(View.GONE);
                if (e != null) {
                    Toast.makeText(getActivity().getBaseContext(), "Something Went Wrong.", LENGTH_LONG).show();
                } else {
                    ArrayList<CatalogModel> prox = new ArrayList<CatalogModel>();
                    for (ParseObject pco : list) {

                        CatalogModel pm = new CatalogModel(
                                pco.get("manufacturer").toString(),
                                pco.get("title").toString(),
                                pco.get("category").toString(),
                                pco.get("subCategory").toString(),
                                pco.get("variant").toString(),
                                pco.get("imageUrl").toString()
                        );
                        prox.add(pm);
                    }

                    ProductListAdapter pla = new ProductListAdapter(getActivity().getBaseContext(), R.layout.seller_product_listing_item, prox);
                    productsHolder.setAdapter(pla);
                    productsHolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            // GETTING THE SELECT CATALOG MODEL
                            CatalogModel selected = (CatalogModel) parent.getAdapter().getItem(position);
                            ParseObject selectedParseObject = list.get(position);

                            // PASSING THE SELECTED PRODUCT TO ADD PRODUCT STOCK DETAILS FRAGMENT
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("LB_SELECTED_PRODUCT", selected);
                            fragment = new SellerProductStockDetailsFragment();
                            fragment.setArguments(bundle);

                            FragmentManager fm = getActivity().getFragmentManager();
                            fm.beginTransaction().replace(R.id.contentFrame, fragment, "ADD_PRODUCT_STOCK_DETAILS").addToBackStack("AddProductStock").commit();
                        }
                    });

                }
            }
        });


    }

}
