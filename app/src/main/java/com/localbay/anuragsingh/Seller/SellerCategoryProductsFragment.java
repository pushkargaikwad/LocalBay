package com.localbay.anuragsingh.Seller;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.adapters.sellerAdapters.StockProductsAdapter;
import com.localbay.anuragsingh.models.CatalogModel;
import com.localbay.anuragsingh.models.PriceModel;
import com.localbay.anuragsingh.models.StockProductModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerCategoryProductsFragment extends Fragment {

    String category;
    ParseUser parseUser;
    ListView categoryProductsView;
    StockProductsAdapter spa;

    /**
     * FRAGMENT TO OPEN THE STOCK DETAILS
     */
    Fragment fragment;
    List<StockProductModel> stockProducts;
    ProgressBar progressBar;

    public SellerCategoryProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GETTING CURRENTLY SELECT CATEGORY - category
        category = getArguments().getString("SELLER_CATEGORY_PRODUCTS");
        // GETTING CURRENT USER
        parseUser = ParseUser.getCurrentUser();
        stockProducts = new ArrayList<StockProductModel>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((SellerDashboardActivity) getActivity()).setActionBarTitle(category);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_category_products, container, false);
        categoryProductsView = (ListView) view.findViewById(R.id.categoryProducts);
        spa = new StockProductsAdapter(getActivity().getBaseContext(), R.id.categoryProductsParent, stockProducts);
        categoryProductsView.setAdapter(spa);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        // GET ALL STOCK PRODUCTS FOR USER WITH CATEGORY - category
        // CATEGORY IS AN ATTRIBUTE OF PRDOUCT CATALOG CLASS
        ParseQuery pq = ParseQuery.getQuery("StockProduct");
        pq.whereEqualTo("sold_by", parseUser);
        pq.include("price");
        pq.include("catalog_product");

        /**
         * LOADING PRODUCTS FROM PARSE, WHICH FALL IN THE CATEGORY FOR THE SELLER
         */

        progressBar.setVisibility(View.VISIBLE);
        pq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e == null) {
                    if (list.size() != 0) {
                        for (ParseObject sellerStockProduct : list) {
                            ParseObject catalogProduct = (ParseObject) sellerStockProduct.get("catalog_product");
                            ParseObject stockPrice = (ParseObject) sellerStockProduct.get("price");

                            if (catalogProduct.getString("category").equals(category)) {
                                int costPrice = (int) stockPrice.get("costPrice");
                                int shippingCost = (int) stockPrice.get("shippingCost");

                                PriceModel priceModel = new PriceModel(
                                        costPrice,
                                        shippingCost
                                );

                                CatalogModel currentCatalogProduct = new CatalogModel(
                                        catalogProduct.getString("manufacturer"),
                                        catalogProduct.getString("title"),
                                        catalogProduct.getString("category"),
                                        catalogProduct.getString("subCategory"),
                                        catalogProduct.getString("variant"),
                                        catalogProduct.getString("imageUrl")
                                );

                                StockProductModel currentStockProduct = new StockProductModel();
                                currentStockProduct.setCatalogModel(currentCatalogProduct);
                                currentStockProduct.setStock_units(sellerStockProduct.getInt("stock_units"));
                                currentStockProduct.setCondition(sellerStockProduct.getString("condition"));
                                currentStockProduct.setDispatchTime(Integer.parseInt(sellerStockProduct.get("dispatch_time").toString()));
                                currentStockProduct.setPriceModel(priceModel);

                                stockProducts.add(currentStockProduct);
                                spa.notifyDataSetChanged();
                            }
                        }

                    }
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Error fetching products", Toast.LENGTH_LONG);
                }
                progressBar.setVisibility(View.GONE);

            }
        });

        
        // ================ LISTVIEW ON CLICK HANDLER ==================
        categoryProductsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockProductModel selectedProduct = (StockProductModel) parent.getAdapter().getItem(position);
                CatalogModel selectedCatalog = selectedProduct.getCatalogModel();
                PriceModel selectedProductPrice = selectedProduct.getPriceModel();
                FragmentManager fm = getFragmentManager();
                fragment = new SellerProductStockDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelable("LB_SELECTED_PRODUCT", selectedCatalog);
                bundle.putParcelable("LB_SELECTED_STOCK_PRODUCT", selectedProduct);
                bundle.putParcelable("LB_SELECTED_PRODUCT_PRICE", selectedProductPrice);

                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("UPDATE_STOCK_DETAILS").commit();

            }
        });
        // ================ ON CLICK LISTENER ENDS =====================
        return view;
    }


}
