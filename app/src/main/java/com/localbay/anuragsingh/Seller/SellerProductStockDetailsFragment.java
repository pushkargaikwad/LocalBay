package com.localbay.anuragsingh.Seller;


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.localbay.anuragsingh.R;
import com.localbay.anuragsingh.models.CatalogModel;
import com.localbay.anuragsingh.models.PriceModel;
import com.localbay.anuragsingh.models.StockProductModel;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerProductStockDetailsFragment extends Fragment {

    CatalogModel catalogModel;
    View view;
    Fragment fragment;
    StockProductModel spm;
    ParseObject parseCatalogModel;
    PriceModel priceModel;

    ProgressBar progressBar;
    Button saveStockProductBtn;

    ParseObject parseStockProduct;

    /**
     * UPDATE FLAG, SET TO TRUE WHEN OPENED FROM LISTING
     * SET TO FLAG, WHEN A NEW ITEM IS CREATED
     */
    Boolean update;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Required empty public constructor
        super.onCreate(savedInstanceState);
        update = false;

        catalogModel = getArguments().getParcelable("LB_SELECTED_PRODUCT");
        if (getArguments().getParcelable("LB_SELECTED_PRODUCT_PRICE") != null) {

            priceModel = getArguments().getParcelable("LB_SELECTED_PRODUCT_PRICE");

            if (getArguments().getParcelable("LB_SELECTED_STOCK_PRODUCT") != null) {
                spm = getArguments().getParcelable("LB_SELECTED_STOCK_PRODUCT");
                spm.setCatalogModel(catalogModel);
                spm.setPriceModel(priceModel);
                update = true;
            }
        } else {
            spm = new StockProductModel();
            update = false;
        }
    }

    public CatalogModel getCatalogModel() {
        return catalogModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_product_stock_details, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        TextView productTitle = (TextView) view.findViewById(R.id.productTitle);
        productTitle.setText(catalogModel.getTitle());

        TextView subCategory = (TextView) view.findViewById(R.id.subCategory);
        subCategory.setText(catalogModel.getCategory());

        TextView brand = (TextView) view.findViewById(R.id.brand);
        brand.setText(catalogModel.getManufacturer());

        TextView variant = (TextView) view.findViewById(R.id.variant);
        variant.setText(catalogModel.getVariant());

        ImageView imageView = (ImageView) view.findViewById(R.id.productImage);
        String productUrl = catalogModel.getImageUrl();

        Ion.with(imageView)
                .placeholder(R.drawable.ic_image_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .load(productUrl);

        EditText stockunits = (EditText) view.findViewById(R.id.stockunits);
        EditText condition = (EditText) view.findViewById(R.id.condition);
        EditText dispatchTime = (EditText) view.findViewById(R.id.dispatchTime);
        EditText price = (EditText) view.findViewById(R.id.costPrice);
        EditText shippingCost = (EditText) view.findViewById(R.id.shippingCost);

        if (spm != null) {
            if(spm.getStock_units() != null)
            stockunits.setText(Integer.toString(spm.getStock_units()));
            if(spm.getCondition() != null)
            condition.setText(spm.getCondition());
            if(spm.getDispatchTime() != -1)
            dispatchTime.setText(Integer.toString(spm.getDispatchTime()));
            if (priceModel != null) {
                if (priceModel.getCostPrice() != -1)
                price.setText(Integer.toString(priceModel.getCostPrice()));
                if (priceModel.getShippingCost() != -1)
                shippingCost.setText(Integer.toString(priceModel.getShippingCost()));
            }
        }

        /**
         * GETTING PARSEOBJECT FOR THE CURRENT CATALOG-MODEL
         * -COULD NOT PASS THE PARSE OBJECT FROM THE CALLING FRAGMENT - FIX
         */

        final ParseObject[] object = new ParseObject[1];
        ParseQuery pq = ParseQuery.getQuery("Catalog");

        pq.whereEqualTo("manufacturer", catalogModel.getManufacturer());
        pq.whereEqualTo("title", catalogModel.getTitle());
        pq.whereEqualTo("variant", catalogModel.getVariant());

        saveStockProductBtn = (Button) view.findViewById(R.id.saveStockProductBtn);
        saveStockProductBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        pq.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    progressBar.setVisibility(View.GONE);
                    saveStockProductBtn.setVisibility(View.VISIBLE);
                    if (list.size() != 0) object[0] = list.get(0);

                    parseCatalogModel = object[0];
                    spm.setParseCatalogModel(parseCatalogModel);
                }
            }
        });


        saveStockProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);

                EditText stockunits = (EditText) view.findViewById(R.id.stockunits);
                spm.setStock_units(Integer.parseInt(stockunits.getText().toString()));

                final EditText condition = (EditText) view.findViewById(R.id.condition);
                spm.setCondition(condition.getText().toString());

                final EditText dispatchTime = (EditText) view.findViewById(R.id.dispatchTime);
                spm.setDispatchTime(Integer.parseInt(dispatchTime.getText().toString()));

                final EditText price = (EditText) view.findViewById(R.id.costPrice);
                final EditText shippingCost = (EditText) view.findViewById(R.id.shippingCost);

                PriceModel pmodel = new PriceModel(
                        Integer.parseInt(price.getText().toString()),
                        Integer.parseInt(shippingCost.getText().toString())
                );

                spm.setPriceModel(pmodel);

                if (!update) {

                    ParseObject productObject = null;
                    try {
                        productObject = spm.saveStockProduct();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (productObject != null) {
                        progressBar.setVisibility(View.GONE);

                        fragment = new SellerProductListingFragment();
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("registerShop").commit();
                    }
                } else {

                    /**
                     * 1. get parse user
                     * 2. get stock product
                     * 3. get parse catalog product
                     * 4. get parse price product
                     */

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    // parse object for catalog parseCatalogModel

                    ParseQuery getStockProducts = ParseQuery.getQuery("StockProduct");
                    getStockProducts.whereEqualTo("sold_by", currentUser);
                    getStockProducts.whereEqualTo("catalog_product", spm.getParseCatalogModel());

                    getStockProducts.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null)
                                parseStockProduct = objects.get(0);


                            if (parseStockProduct != null) {
                                parseStockProduct.put("condition", spm.getCondition());
                                parseStockProduct.put("dispatch_time", spm.getDispatchTime());

                                ParseRelation priceRelation = (ParseRelation) parseStockProduct.getRelation("prices");
                                ParseQuery getPriceParse = priceRelation.getQuery();
                                getPriceParse.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            ParseObject parsePrice = objects.get(0);

                                            parsePrice.put("shippingCost", spm.getPriceModel().getShippingCost());
                                            parsePrice.put("costPrice", spm.getPriceModel().getCostPrice());

                                            parsePrice.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null)
                                                        parseStockProduct.saveInBackground(new SaveCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if (e == null) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                    fragment = new SellerProductListingFragment();
                                                                    FragmentManager fm = getFragmentManager();
                                                                    fm.beginTransaction().replace(R.id.contentFrame, fragment).addToBackStack("registerShop").commit();
                                                                }
                                                            }
                                                        });
                                                }
                                            });
                                        }
                                    }

                                });

                            } else {
                                Toast.makeText(getActivity().getBaseContext(), "Error Updating the Stock, Please Try again", Toast.LENGTH_LONG);
                            }
                        }
                    });

                }
            }
        });
        return view;
    }


}
